package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.providers.ProviderInvoker;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.fromwork.spring.annotation.EnableProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tnnn
 */
@RestController
@SpringBootApplication
@EnableProvider
public class TrpcDemoProviderApplication {

    @Autowired
    private ProviderInvoker providerInvoker;

    public static void main(String[] args) {
        SpringApplication.run(TrpcDemoProviderApplication.class, args);
    }


    /**
     * 启动 模拟调用一次请求测试下 （接口测试：demo.http）
     * @return ApplicationRunner
     */
    @Bean
    public ApplicationRunner provideRun(){
        return x -> {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setService(OrderService.class.getCanonicalName());
            rpcRequest.setMethodSign(MethodUtils.methodSign(OrderService.class.getMethod("findById",Integer.class)));
            rpcRequest.setArgs(new Object[]{100});
            RpcResponse<Object> rpcResponse = providerInvoker.invoke(rpcRequest);
            // Order(id=100, amount=15.0)
            System.out.println("return: " + rpcResponse.getData());
        };
    }
}
