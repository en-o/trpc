package cn.tannn.trpc.demo.consumer;

import cn.tannn.trpc.demo.provider.TrpcDemoProviderApplication;
import cn.tannn.trpc.registry.zk.test.TestZKServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class TrpcDemoConsumerApplicationTests {

    static ApplicationContext context1;
    static ApplicationContext context2;

    static TestZKServer zkServer = new TestZKServer();

    @BeforeAll
    static void init() {
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        System.out.println(" =============     ZK2182    ========== ");
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        zkServer.start();

        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");

        System.out.println(" =============      P8094    ========== ");
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        context1 = SpringApplication.run(TrpcDemoProviderApplication.class,
                "--server.port=8094",
                "--trpc.rc.connect[0].port=2182",
                "--trpc.rc.connect[0].ip=127.0.0.1",
                "--trpc.rc.name=zk",
                "--trpc.rc.namespace=trpc",
                "--trpc.api.context=trpc",
                "--trpc.app.env=test",
                "--trpc.app.gray.dc=cqs",
                "--trpc.app.gray.gray=false",
                "--trpc.app.gray.unit=A001",
                "--logging.level.cn.tannn.trpc=info");

        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        System.out.println(" =============      P8095    ========== ");
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        context2 = SpringApplication.run(TrpcDemoProviderApplication.class,
                "--server.port=8095",
                "--trpc.rc.connect[0].port=2182",
                "--trpc.rc.connect[0].ip=127.0.0.1",
                "--trpc.rc.name=zk",
                "--trpc.rc.namespace=trpc",
                "--trpc.api.context=trpc",
                "--trpc.app.env=test",
                "--trpc.app.gray.dc=cqs",
                "--trpc.app.gray.gray=false",
                "--trpc.app.gray.unit=A002",
                "--logging.level.cn.tannn.trpc=info");
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> abcd  .... ");
    }

    @AfterAll
    static void destory() {
        SpringApplication.exit(context1, () -> 1);
        SpringApplication.exit(context2, () -> 1);
        zkServer.stop();
    }
}
