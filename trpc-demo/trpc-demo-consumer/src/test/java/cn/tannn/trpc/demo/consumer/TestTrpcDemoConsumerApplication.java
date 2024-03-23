
package cn.tannn.trpc.demo.consumer;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author tnnn
 */
@SpringBootApplication
public class TestTrpcDemoConsumerApplication {
    @Bean
    public ApplicationRunner startUpCheck() {
        return args -> {
            // This ApplicationRunner will only be included in tests
        };
    }
}
