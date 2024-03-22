
package cn.tannn.trpc.demo.consumer;

import cn.tannn.trpc.core.annotation.EnableConsumer;
import cn.tannn.trpc.core.annotation.EnableProvider;
import cn.tannn.trpc.demo.consumer.runner.StartUpCheck;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

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
