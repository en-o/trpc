package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.LoadBalancer;
import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.api.Router;
import cn.tannn.trpc.core.cluster.RoundRibonLoadBalancer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 将自己的类加载进 spring 容器
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@AutoConfiguration
public class ConsumerConfig {

    @Value("${trpc.providers}")
    String servers;

    @Bean
    ConsumerBootstrap createConsumerBootstrap(){
       return new ConsumerBootstrap(new String[]{"cn.tannn.trpc.demo.consumer"});
//       return new ConsumerBootstrap(new String[]{"cn.tannn.trpc.demo.consumer.controller"});
//       return new ConsumerBootstrap(new String[]{"cn.tannn.trpc.demo.consumer.runner"});
//       return new ConsumerBootstrap(new String[]{"cn.tannn.trpc.demo.consumer.controller",
//               "cn.tannn.trpc.demo.consumer.runner"});
    }

    /**
     * 负载均衡
     */
    @Bean
    LoadBalancer loadBalancer(){
//       return LoadBalancer.Default;
//       return new RandomLoadBalancer();
       return new RoundRibonLoadBalancer();
    }

    /**
     * 路由
     */
    @Bean
    Router router(){
        return Router.Default;
    }

    /**
     * 静态注册中心
     * <pr>
     *  启动执行 start
     *  销毁执行 stop
     * </pr>
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    RegistryCenter.StaticRegistryCenter consumer_rc(){
        return new RegistryCenter.StaticRegistryCenter(List.of(servers.split(",")));
    }


}
