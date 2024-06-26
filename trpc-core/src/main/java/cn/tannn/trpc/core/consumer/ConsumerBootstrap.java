package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.common.api.LoadBalancer;
import cn.tannn.trpc.common.api.RegistryCenter;
import cn.tannn.trpc.common.api.Router;
import cn.tannn.trpc.common.api.RpcContext;
import cn.tannn.trpc.common.chain.FilterChain;
import cn.tannn.trpc.common.exception.TrpcException;
import cn.tannn.trpc.common.properties.RpcProperties;
import cn.tannn.trpc.core.util.ProxyUtils;
import cn.tannn.trpc.core.util.ScanPackagesUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Set;

import static cn.tannn.trpc.common.exception.ExceptionCode.SCAN_PACKAGE_EX;

/**
 * 消费端核心类 - 后面要把 ApplicationContextAware 挪到 trpc-spring-starter , 因为这是spring的能力
 *
 * <p> 消费处理
 * <p>   1. 完成消费接口的扫描和动态代理
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/2 14:16
 */
@Slf4j
@Getter
public class ConsumerBootstrap implements ApplicationContextAware {

    private ApplicationContext context;

    private final RpcProperties rpcProperties;

    public ConsumerBootstrap(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     * init  init : 拿到 所有标记了TConsumer注解的类（所有的提供者接口元数据），并设置代理
     * <p>
     * 为了包装所有实例都已经加载完成，在 使用 runner 主动调用，确保实例都加载完成
     * </p>
     */
    public void start() {
        log.info("consumerBootstrap start...");
        // 扫描并设置代理
        scanConsumerAndProxy();
        log.info("consumerBootstrap started.");
    }

    /**
     * 扫描拥有注解的类并设置动态代理
     */
    private void scanConsumerAndProxy() {
        // 扫描指定路径的类
        String[] scanPackages = rpcProperties.getScanPackages();
        if (scanPackages == null || scanPackages.length == 0) {
            throw new TrpcException(SCAN_PACKAGE_EX);
        }

        Set<BeanDefinition> beanDefinitions = ScanPackagesUtils.scanPackages(scanPackages);

        // - 非 spring boot项目需要把这个提成参数传入
        // 拿到过滤器集
        FilterChain filterChain = context.getBean(FilterChain.class);
        // 拿到负载均衡
        LoadBalancer loadBalancer = context.getBean(LoadBalancer.class);
        // 拿到注册中心
        RegistryCenter registryCenter = context.getBean(RegistryCenter.class);
        // 拿到路由器
        Router router = context.getBean(Router.class);
        // 启动 rc  ，stop在 ConsumerPreDestroy
        registryCenter.start();


        // 透传 context 数据 - 这样做的是,新加参数用在中环境修改了,只改两头
        RpcContext rpcContext = new RpcContext();
        rpcContext.setLoadBalancer(loadBalancer);
        rpcContext.setRpcProperties(rpcProperties);
        rpcContext.setRegistryCenter(registryCenter);
        rpcContext.setRouter(router);
        rpcContext.setFilters(filterChain);

        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object bean = ScanPackagesUtils.getBean(context, beanDefinition);
            ProxyUtils.rpcApiProxy(bean, rpcContext);
        }
    }

}
