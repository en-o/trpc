package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.meta.ProviderMeta;
import cn.tannn.trpc.core.util.MethodUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 服务端核心类
 * <p>
 *     服务处理器
 *     1. 完成服务提供者的扫描
 *     2. 完整服务的注册中心注册
 * </p>
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("ProviderBootstrap init...");
        this.context = applicationContext;
        // 所有标记了 @TProvider 注解的类
        Map<String, Object> providers = context.getBeansWithAnnotation(TProvider.class);
        providers.forEach((x,y) -> log.debug(x));
        providers.values().forEach(this::storageSkeleton);
        log.info("ProviderBootstrap initialized.");
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
     * @param anInterface 接口类
     * @param impl 接口实现类
     * @param method 接口方法
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
