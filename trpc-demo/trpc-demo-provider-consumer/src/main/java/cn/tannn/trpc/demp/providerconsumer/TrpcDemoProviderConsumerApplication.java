package cn.tannn.trpc.demp.providerconsumer;

import cn.tannn.trpc.core.annotation.EnableConsumer;
import cn.tannn.trpc.core.annotation.EnableProvider;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.providers.ProvidersInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tnnn
 */
@RestController
@SpringBootApplication
@EnableConsumer
@EnableProvider
public class TrpcDemoProviderConsumerApplication {

    @Autowired
    private ProvidersInvoker providersInvoker;

    public static void main(String[] args) {
        SpringApplication.run(TrpcDemoProviderConsumerApplication.class, args);
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



}
