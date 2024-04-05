package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.meta.ProviderMeta;
import cn.tannn.trpc.core.meta.ServiceMeta;
import cn.tannn.trpc.core.properties.AppProperties;
import cn.tannn.trpc.core.properties.RpcProperties;
import cn.tannn.trpc.core.properties.meta.GrayMetas;
import cn.tannn.trpc.core.util.MethodUtils;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Map;

/**
 * 服务端核心类 - 后面要把 ApplicationContextAware 挪到 trpc-spring-starter , 因为这是spring的能力
 * <p> 服务处理器
 * <p>   1. 完成服务提供者的扫描
 * <p>   2. 完整服务的注册中心注册
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/1 下午10:24
 */
@Slf4j
@Getter
public class ProviderBootstrap implements ApplicationContextAware {

    /**
     * 本地缓存 - 存储的提供者[类全限定名,Provider的映射关系]
     */
    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

    /**
     * spring 上下文
     */
    private ApplicationContext context;

    /**
     * 注册中心
     */
    private final RegistryCenter registryCenter;

    /**
     * 注册配置
     */
    private final AppProperties appProperties;

    /**
     * 灰度
     */
    private final GrayMetas grayMetas;

    /**
     * 当前服务的RPC接口前缀
     */
    private final String rpcApiContextPath;


    /**
     * ip+port：start()的时候组装。unregisterService/registerService 的时候使用
     */
    private InstanceMeta instance;


    public ProviderBootstrap(RegistryCenter registryCenter
            , RpcProperties rpcProperties) {
        this.registryCenter = registryCenter;
        this.appProperties = rpcProperties.getApp();
        this.grayMetas = rpcProperties.getApp().getGray();
        this.rpcApiContextPath = rpcProperties.getApi().getContext();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        // 开始解析和扫描provider服务接口
        init();
    }

    /**
     * 加载 providers
     */
    public void init() {
        log.info("ProviderBootstrap init...");
        // 所有标记了 @TProvider 注解的类
        Map<String, Object> providers = context.getBeansWithAnnotation(TProvider.class);
        providers.forEach((x, y) -> log.debug(x));
        providers.values().forEach(this::storageSkeleton);
        log.info("ProviderBootstrap initialized.");
    }


    /**
     * 开始注册
     * <pr>为了包装所有实例都已经加载完成，在 runner后主动调用，跟上面的 skeleton 分开操作做到单一职责，遇到错误好处理</pr>
     * @param port 注册当前项目的启动端口
     */
    @SneakyThrows
    public void start(Integer port) {
        log.info("ProviderBootstrap start...");
        // 注册中心开始工作
        registryCenter.start();
        String ip = InetAddress.getLocalHost().getHostAddress();
        // 服务信息
        instance = InstanceMeta.http(ip, port, rpcApiContextPath);
        instance.setGray(grayMetas);
        skeleton.keySet().forEach(this::registerService);
        log.info("ProviderBootstrap started.");
    }

    /**
     * 注册服务 - 注册中心
     *
     * @param serviceName serviceName
     */
    private void registerService(String serviceName) {
        ServiceMeta meta = new ServiceMeta();
        meta.setAppid(appProperties.getAppid());
        meta.setName(serviceName);
        meta.setNamespace(appProperties.getNamespace());
        meta.setEnv(appProperties.getEnv());
        meta.setVersion(appProperties.getVersion());
        registryCenter.register(meta, instance);
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
        ServiceMeta meta = new ServiceMeta();
        meta.setAppid(appProperties.getAppid());
        meta.setName(serviceName);
        meta.setNamespace(appProperties.getNamespace());
        meta.setEnv(appProperties.getEnv());
        meta.setVersion(appProperties.getVersion());
        registryCenter.unregister(meta, instance);
    }


    /**
     * 存储提供者 - 解析Object
     *
     * @param impl 提供者对象
     */
    public void storageSkeleton(Object impl) {
        // 处理实现类上的多个接口,存储接口名和接口方法等数据
        Arrays.stream(impl.getClass().getInterfaces()).forEach(
                service -> Arrays.stream(service.getMethods())
                        // [Object]本地方法不代理
                        .filter(method -> !MethodUtils.checkLocalMethod(method))
                        .forEach(method -> createProvider(service, impl, method))
        );
    }


    /**
     * 存储提供者 - 创建 ProviderMeta
     *
     * @param anInterface 接口类
     * @param impl        接口实现类
     * @param method      接口方法
     */
    private void createProvider(Class<?> anInterface, Object impl, Method method) {
        ProviderMeta providerMeta = new ProviderMeta();
        providerMeta.setMethod(method);
        providerMeta.setMethodSign(MethodUtils.methodSign(method));
        providerMeta.setServiceImpl(impl);
        log.info(" create a provider: {}", providerMeta);
        skeleton.add(anInterface.getCanonicalName(), providerMeta);
    }
}
