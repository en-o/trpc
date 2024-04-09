package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.common.api.RpcRequest;
import cn.tannn.trpc.common.api.RpcResponse;
import cn.tannn.trpc.core.providers.ProviderInvoker;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.fromwork.spring.annotation.EnableProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(TrpcDemoProviderApplication.class, args);
    }

    @RequestMapping("/ports")
    public RpcResponse<String> ports(@RequestParam("ports") String ports) {
        userService.setTimeoutPorts(ports);
        RpcResponse<String> response = new RpcResponse<>();
        response.setStatus(true);
        response.setData("OK:" + ports);
        return response;
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
