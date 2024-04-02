package cn.tannn.trpc.fromwork.spring.provider;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.providers.ProviderBootstrap;
import cn.tannn.trpc.core.providers.ProvidersInvoker;
import cn.tannn.trpc.fromwork.spring.properties.RpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RequestPredicates.*;

/**
 * 服务提供者配置类 - springboot 集成
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/1 下午11:15
 */
@AutoConfiguration
@Slf4j
public class ProvidersConfig {

    /**
     * 启动服务提供者信息扫描并存储
     */
    @Bean
    ProviderBootstrap providersBootstrap(){
        return new ProviderBootstrap();
    }

    /**
     * 注册 : RPC服务调用的核心方法
     */
    @Bean
    ProvidersInvoker providersInvoker(ProviderBootstrap providersBootstrap){
        return new ProvidersInvoker(providersBootstrap);
    }



    /**
     * 使用 HTTP + JSON 来实现序列化和通信
     * <p>
     *     // @param RpcRequest 需要调用的接口信息 （通过接口信息去调用接口，然后返回接口执行结果
     *     // @RequestMapping("/")
     *     public RpcResponse<Object> invoke(@RequestBody RpcRequest request) {
     *        return providersInvoker.invoke(request);
     *     }
     * </p>
     * <p>
     *     trpc.api.enabled = true 时加载，并且默认加载
     * </p>
     * @return RpcResponse
     */
    @Bean
    @ConditionalOnProperty(
            value = "trpc.api.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public RouterFunction<ServerResponse> invoke(@Autowired ProvidersInvoker providersInvoker
            , @Autowired RpcProperties rpcProperties) {
        String context = rpcProperties.getApi().getContext();
        String prefix = "/";
        if("/".equals(context)){
            prefix = "";
        }
        log.debug("rpc api name http://ip:port{}{}",prefix,context);
        return RouterFunctions
                .route(POST(context)
                                .and(accept(APPLICATION_JSON)
                                        .and(contentType(APPLICATION_JSON))),
                        request -> {
                            // 来提取body 中的JSON 对象
                            RpcRequest rpcRequest = request.body(RpcRequest.class);
                            // 处理请求体，这里仅作为示例直接返回请求体
                            RpcResponse<Object> invoke = providersInvoker.invoke(rpcRequest);
                            // 构建响应体
                            return ServerResponse.ok().body(invoke);
                        });
    }

}
