package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.LoadBalancer;
import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.api.Router;
import cn.tannn.trpc.core.cluster.RandomLoadBalancer;
import cn.tannn.trpc.core.cluster.RoundRibbonLoadBalancer;
import cn.tannn.trpc.core.config.ConsumerProperties;
import cn.tannn.trpc.core.enums.LoadBalancerEnum;
import cn.tannn.trpc.core.registry.ZkRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * 将自己的类加载进 spring 容器
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@AutoConfiguration
@Slf4j
public class ConsumerConfig {


    /**
     * 配置信息
     */
    @Bean
    ConsumerProperties consumerProperties(){
        return new ConsumerProperties();
    }

    /**
     * applicationContext
     * @param consumerProperties 设置扫描路径
     */
    @Bean
    ConsumerBootstrap createConsumerBootstrap(ConsumerProperties consumerProperties){
       return new ConsumerBootstrap(consumerProperties.getScanPackages());
    }

    /**
     * 在 applicationRunner后主动调用，确保实例全部加载完成，防止初始化的过程中被注册使用导致ClassNotFoundException
     * @param consumerBootstrap ConsumerBootstrap
     * @return ApplicationRunner
     */
    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner consumerBootstrapRunner(ConsumerBootstrap consumerBootstrap) {
        return x -> consumerBootstrap.start();
    }


    /**
     * 加载负载均衡器
     */
    @Bean
    LoadBalancer loadBalancer(ConsumerProperties consumerProperties){
        // return LoadBalancer.Default;
        if(consumerProperties.getLoadBalancer().equals(LoadBalancerEnum.ROUND_RIBBON)){
            return new RoundRibbonLoadBalancer();
        }else {
            return new RandomLoadBalancer();
        }
    }

    /**
     * 加载路由处理器
     */
    @Bean
    Router router(){
        return Router.Default;
    }

    /**
     * 加载注册中心
     * <pr>
     *  启动自动执行 RegistryCenter#start
     *  销毁自动执行 RegistryCenter#stop
     * </pr>
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    RegistryCenter consumerRc(ConsumerProperties consumerProperties){
//        return new RegistryCenter.StaticRegistryCenter(List.of(consumerProperties.getProviders()));
        return new ZkRegistryCenter();
    }


}
