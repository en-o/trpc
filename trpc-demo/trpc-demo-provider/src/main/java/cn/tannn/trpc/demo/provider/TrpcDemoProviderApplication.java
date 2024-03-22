package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.annotation.EnableProvider;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.providers.ProvidersInvoker;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.demo.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tnnn
 */
@RestController
@SpringBootApplication
@EnableProvider
public class TrpcDemoProviderApplication {

    @Autowired
    private ProvidersInvoker providersInvoker;

    public static void main(String[] args) {
        SpringApplication.run(TrpcDemoProviderApplication.class, args);
    }


    /**
     * 使用 HTTP + JSON 来实现序列化和通信
     *
     * @param request 需要调用的接口信息 （通过接口信息去调用接口，然后返回接口执行结果
     * @return RpcResponse
     */
    @RequestMapping("/")
    public RpcResponse<Object> invoke(@RequestBody RpcRequest request) {
        return providersInvoker.invoke(request);
    }


    /**
     * 启动 模拟调用一次请求测试下 （接口测试：demo.http）
     * @return ApplicationRunner
     */
    @Bean
    public ApplicationRunner provideRun(){
        return x -> {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setService("cn.tannn.trpc.demo.api.UserService");
            rpcRequest.setMethodSign(MethodUtils.methodSign(UserService.class.getMethod("findById",Integer.class)));
            rpcRequest.setArgs(new Object[]{100});
            RpcResponse<Object> rpcResponse = providersInvoker.invoke(rpcRequest);
            System.out.println("return: " + rpcResponse.getData());
        };
    }

}
