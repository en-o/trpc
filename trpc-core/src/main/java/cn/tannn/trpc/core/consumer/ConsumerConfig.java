package cn.tannn.trpc.core.consumer;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * 将自己的类加载进 spring 容器
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@Configurable
public class ConsumerConfig {

    @Bean
    private ConsumerBootstrap createConsumerBootstrap(){
//       return new ConsumerBootstrap(new String[]{"cn.tannn.trpc.demo.consumer.controller"});
//       return new ConsumerBootstrap(new String[]{"cn.tannn.trpc.demo.consumer.runner"});
       return new ConsumerBootstrap(new String[]{"cn.tannn.trpc.demo.consumer.controller",
               "cn.tannn.trpc.demo.consumer.runner"});
    }

}
