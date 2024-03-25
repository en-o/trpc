package cn.tannn.trpc.demo.consumer;

import cn.tannn.trpc.core.test.TestZKServer;
import cn.tannn.trpc.demo.provider.TrpcDemoProviderApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = TrpcDemoConsumerApplication.class)
class TrpcDemoConsumerApplicationTests {

    static ApplicationContext context;

    static TestZKServer zkServer = new TestZKServer();

    @BeforeAll
    static void init() {
        System.out.println(" ====================================== ");
        zkServer.start();
        context = SpringApplication.run(TrpcDemoProviderApplication.class,
                "--server.port=8089",
                "--trpc.rc.connect[0].port=2182",
                "--trpc.rc.connect[0].ip=127.0.0.1",
                "--trpc.rc.name=zk",
                "--trpc.rc.namespace=trpc",
                "--trpc.app.appid=provider",
                "--trpc.app.env=dev",
                "--trpc.app.namespace=dev",
                "--trpc.app.version=0.0.1",
                "--logging.level.cn.tannn.trpc=info");
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> abcd  .... ");
    }

    @AfterAll
    static void destory() {
//        SpringApplication.exit(context, () -> 1);
        zkServer.stop();
    }

}
