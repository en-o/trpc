package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.LoadBalancer;
import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.api.Router;
import cn.tannn.trpc.core.api.RpcContext;
import cn.tannn.trpc.core.config.ConsumerProperties;
import cn.tannn.trpc.core.config.RpcProperties;
import cn.tannn.trpc.core.exception.ConsumerException;
import cn.tannn.trpc.core.util.ProxyUtils;
import cn.tannn.trpc.core.util.ScanPackagesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Set;

/**
 * 消费者处理器
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 19:47
 */
@Slf4j
public class ConsumerBootstrap implements ApplicationContextAware {

    private final RpcProperties rpcProperties;

    private ApplicationContext context;




    public ConsumerBootstrap(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }


    /**
     * applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }



    /**
     * init  init : 拿到 所有标记了TConsumer注解的类（所有的提供者接口元数据），并设置代理
     * <pr>为了包装所有实例都已经加载完成，在 使用 runner 主动调用，确保实例都加载完成</pr>
     */
    public void start() {
        log.info("consumerBootstrap start...");

        LoadBalancer loadBalancer = context.getBean(LoadBalancer.class);
        Router router = context.getBean(Router.class);
        RegistryCenter registryCenter = context.getBean(RegistryCenter.class);
        // 启动 rc todo consumer 没有设置stop
        registryCenter.start();
        ConsumerProperties consumerProperties = rpcProperties.getConsumer();
        if(consumerProperties.getScanPackages() == null || consumerProperties.getScanPackages().length==0){
            throw new ConsumerException("consumer请设置扫描包路径");
        }
        // 扫描指定路径的类
        Set<BeanDefinition> beanDefinitions = ScanPackagesUtils.scanPackages(consumerProperties.getScanPackages());
        scanConsumerAndProxy(beanDefinitions,
                loadBalancer,
                router,
                registryCenter);
        log.info("consumerBootstrap started.");
    }



    /**
     * 扫描拥有注解的类并设置动态代理
     *
     * @param candidateComponents BeanDefinition
     * @param loadBalancer 负载均衡
     * @param router 路由机制
     */
    private void scanConsumerAndProxy(Set<BeanDefinition> candidateComponents,
                                      LoadBalancer loadBalancer,
                                      Router router,
                                      RegistryCenter registryCenter) {


        RpcContext rpcContext = new RpcContext();
        rpcContext.setLoadBalancer(loadBalancer);
        rpcContext.setRouter(router);
        rpcContext.setRegistryCenter(registryCenter);
        rpcContext.setRpcProperties(rpcProperties);
        for (BeanDefinition beanDefinition : candidateComponents) {
            try {
                Object bean = ScanPackagesUtils.getBean(context, beanDefinition);
                ProxyUtils.rpcApiProxy(bean,rpcContext);
            } catch (Exception e) {
                throw new ConsumerException(e);
            }
        }
    }


}
