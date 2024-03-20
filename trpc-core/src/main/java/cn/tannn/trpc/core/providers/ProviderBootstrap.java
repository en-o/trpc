package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.config.RpcProperties;
import cn.tannn.trpc.core.config.ServiceProperties;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.meta.ProviderMeta;
import cn.tannn.trpc.core.meta.ServiceMeta;
import cn.tannn.trpc.core.util.MethodUtils;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

/**
 * 提供者处理器
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@Slf4j
@Data
public class ProviderBootstrap implements ApplicationContextAware {

    private final RpcProperties rpcProperties;

    public ProviderBootstrap(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    /**
     * 存储所有的提供者 , 其中包含了[全限定名，提供者元数据]
     */
    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

    /**
     * spring 上下文
     */
    private ApplicationContext context;

    /**
     * 注册中心缓存起来
     */
    RegistryCenter registryCenter;


    @Value("${server.port}")
    private Integer port;


    /**
     * ip+port：start()的时候组装。unregisterService/registerService 的时候使用
     */
    private InstanceMeta instance;

    /**
     * init : 拿到 所有标记了TProvider注解的类（所有的提供者），并存储
     */
    @SneakyThrows
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("ProviderBootstrap init...");
        this.context = applicationContext;
        registryCenter = context.getBean(RegistryCenter.class);
        // 所有标记了 @TProvider 注解的类
        Map<String, Object> providers = context.getBeansWithAnnotation(TProvider.class);
        providers.forEach((x,y) -> log.info(x));
        providers.values().forEach(this::genInterface);
        log.info("ProviderBootstrap initialized.");
    }

    /**
     * 开始注册
     * <pr>为了包装所有实例都已经加载完成，在 runner后主动调用，跟上面的 skeleton 分开操作做到单一职责，遇到错误好处理</pr>
     */
    @SneakyThrows
    public void start() {
        log.info("ProviderBootstrap start...");
        // 注册中心开始工作
        registryCenter.start();
        String ip = InetAddress.getLocalHost().getHostAddress();
        instance = InstanceMeta.http(ip,port);
        skeleton.keySet().forEach(this::registerService);
        log.info("ProviderBootstrap started.");
    }

    /**
     * spring boot 生命完结时自动销毁
     */
    @PreDestroy
    public void stop(){
        log.info("ProviderBootstrap stop...");
        skeleton.keySet().forEach(this::unregisterService);
        // 注册中心工作结束下班
        registryCenter.stop();
        log.info("ProviderBootstrap stopped.");
    }


    /**
     *  注销服务  - 注册中心
     *
     * @param serviceName serviceName
     */
    private void unregisterService(String serviceName) {
        ServiceProperties app = rpcProperties.getApp();
        ServiceMeta meta = new ServiceMeta(serviceName);
        meta.setAppid(app.getAppid());
        meta.setNamespace(app.getNamespace());
        meta.setEnv(app.getEnv());
        registryCenter.unregister(meta, instance);
    }

    /**
     * 注册服务 - 注册中心
     *
     * @param serviceName serviceName
     */
    private void registerService(String serviceName) {
        ServiceProperties app = rpcProperties.getApp();
        ServiceMeta meta = new ServiceMeta(serviceName);
        meta.setAppid(app.getAppid());
        meta.setNamespace(app.getNamespace());
        meta.setEnv(app.getEnv());
        registryCenter.register(meta, instance);
    }


    /**
     * 产出提供者
     *
     * @param impl 接口
     */
    public void genInterface(Object impl) {
        // todo 1. getInterfaces可以拦截某些接口不做处理 ps: spring不支持多个实现类的bean,非要弄的话需要做特殊处理
        //      2. 还可以对方法进行白名单处理
        // 处理多个接口
        Arrays.stream(impl.getClass().getInterfaces()).forEach(
                service -> Arrays.stream(service.getMethods())
                        .filter(MethodUtils::checkLocalMethod)
                        .forEach(method -> createProvider(service, impl, method))
                );
    }

    /**
     * 存储提供者
     */
    private void createProvider(Class<?> anInterface, Object impl, Method method) {
        ProviderMeta providerMeta = new ProviderMeta();
        providerMeta.setMethod(method);
        providerMeta.setMethodSign(MethodUtils.methodSign(method));
        providerMeta.setServiceImpl(impl);

        log.info(" create a provider: " + providerMeta);
        skeleton.add(anInterface.getCanonicalName(), providerMeta);
    }


}
