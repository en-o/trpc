package cn.tannn.trpc.demo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tnnn
 */
@RestController
@SpringBootApplication
public class TrpcDemoProviderApplication {


    public static void main(String[] args) {
        SpringApplication.run(TrpcDemoProviderApplication.class, args);
    }



}
