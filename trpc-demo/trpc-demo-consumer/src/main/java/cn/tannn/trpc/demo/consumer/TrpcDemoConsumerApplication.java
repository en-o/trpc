
package cn.tannn.trpc.demo.consumer;

import cn.tannn.trpc.core.annotation.EnableConsumer;
import cn.tannn.trpc.demo.consumer.runner.StartUpCheck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author tnnn
 */
@SpringBootApplication
@EnableConsumer
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
