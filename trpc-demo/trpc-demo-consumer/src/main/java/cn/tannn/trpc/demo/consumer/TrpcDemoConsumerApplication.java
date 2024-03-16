
package cn.tannn.trpc.demo.consumer;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.core.consumer.ConsumerConfig;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import cn.tannn.trpc.demo.consumer.runner.StartUpCheck;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author tnnn
 */
@SpringBootApplication
@Import(ConsumerConfig.class)
public class TrpcDemoConsumerApplication {


    public static void main(String[] args) {
        SpringApplication.run(TrpcDemoConsumerApplication.class, args);
    }


    /**
     * 启动测试用的
     */
    @Bean
    public StartUpCheck startUpCheck() {
        return new StartUpCheck();
    }

}
