package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.exception.ProviderException;
import cn.tannn.trpc.core.meta.ProviderMeta;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.core.util.TypeUtils;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    /**
     * 当前项目的端口 - 用于注册到rc
     */
    @Value("${server.port}")
    private String port;

    /**
     * ip+port：start()的时候组装。unregisterService/registerService 的时候使用
     */
    private String instance;

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
        instance = ip + "_" + port;
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
     * @param service service
     */
    private void unregisterService(String service) {
        registryCenter.unregister(service, instance);
    }

    /**
     * 注册服务 - 注册中心
     *
     * @param service service
     */
    private void registerService(String service) {
        registryCenter.register(service, instance);
    }


    /**
     * 产出提供者
     *
     * @param x 接口
     */
    public void genInterface(Object x) {
        // 默认只拿一个接口
//        Class<?> anInterface = x.getClass().getInterfaces()[0];]
        // 处理多个接口
        Arrays.stream(x.getClass().getInterfaces()).forEach(itfer -> {
            // todo 这里可以拦截某些接口不做处理 ps: spring不支持多个实现类的bean,非要弄的话需要做特殊处理
            for (Method method : itfer.getMethods()) {
                // todo 这里可以对方法进行白名单处理

                //  这里过滤 Object的一些方法
                if (MethodUtils.checkLocalMethod(method)) {
                    continue;
                }
                createProvider(itfer, x, method);
            }
        });


    }

    /**
     * 存储提供者
     */
    private void createProvider(Class<?> anInterface, Object aclass, Method method) {
        ProviderMeta providerMeta = new ProviderMeta();
        providerMeta.setMethod(method);
        providerMeta.setServiceImpl(aclass);
        providerMeta.setMethodSign(MethodUtils.methodSign(method));
        log.info(" create a provider: " + providerMeta);
        skeleton.add(anInterface.getCanonicalName(), providerMeta);
    }


}
