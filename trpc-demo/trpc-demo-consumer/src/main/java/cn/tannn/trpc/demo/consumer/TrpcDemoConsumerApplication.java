
package cn.tannn.trpc.demo.consumer;

import cn.tannn.trpc.core.annotation.EnableConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tnnn
 */
@SpringBootApplication
@EnableConsumer
public class TrpcDemoConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrpcDemoConsumerApplication.class, args);
    }

}
