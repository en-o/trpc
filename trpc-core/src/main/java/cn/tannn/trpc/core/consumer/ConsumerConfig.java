package cn.tannn.trpc.core.consumer;


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
     * applicationContext
     */
    @Bean
    ConsumerBootstrap createConsumerBootstrap() {
        // todo 提到 starter 并完成参数配置化或者注册中心化
        return new ConsumerBootstrap(new String[]{"cn.tannn.trpc"}, "127.0.0.1", 8081, "/trpc");
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





}
