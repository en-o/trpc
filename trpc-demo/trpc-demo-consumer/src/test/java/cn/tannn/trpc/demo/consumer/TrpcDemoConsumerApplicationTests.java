package cn.tannn.trpc.demo.consumer;

import cn.tannn.trpc.core.annotation.EnableProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestTrpcDemoConsumerApplication.class)
class TrpcDemoConsumerApplicationTests {

    @Test
    void contextLoads() {
    }

}
