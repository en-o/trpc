package cn.tannn.trpc.fromwork.spring.consumer;


import cn.tannn.trpc.common.api.Filter;
import cn.tannn.trpc.common.api.LoadBalancer;
import cn.tannn.trpc.common.api.Router;
import cn.tannn.trpc.common.chain.FilterChain;
import cn.tannn.trpc.common.enums.LoadBalancerEnum;
import cn.tannn.trpc.common.properties.RpcProperties;
import cn.tannn.trpc.core.cluster.GrayRouter;
import cn.tannn.trpc.core.cluster.RandomLoadBalancer;
import cn.tannn.trpc.core.cluster.RoundRibbonLoadBalancer;
import cn.tannn.trpc.core.consumer.ConsumerBootstrap;
import cn.tannn.trpc.core.util.EnumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.util.List;

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
     * applicationContext
     */
    @Bean
    ConsumerBootstrap createConsumerBootstrap(@Autowired RpcProperties rpcProperties) {
        return new ConsumerBootstrap(rpcProperties);
    }

    /**
     * 在 applicationRunner后主动调用，确保实例全部加载完成，防止初始化的过程中被注册使用导致ClassNotFoundException
     *
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
    LoadBalancer loadBalancer(RpcProperties rpcProperties) {
        if (rpcProperties.getConsumer().getLoadBalancer().equals(LoadBalancerEnum.ROUND_RIBBON)) {
            return new RoundRibbonLoadBalancer();
        } else {
            return new RandomLoadBalancer();
        }
    }

    /**
     * 加载过滤器
     */
    @Bean
    FilterChain filterChain(RpcProperties rpcProperties) {
        List<Filter> filters = EnumUtils.findFilter(rpcProperties.getConsumer().getFilter());
        return new FilterChain(filters);
    }

    /**
     * 加载路由处理器
     */
    @Bean
    Router router(RpcProperties rpcProperties) {
        return new GrayRouter(rpcProperties.getConsumer());
    }

}
