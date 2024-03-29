package cn.tannn.trpc.core.api;

import cn.tannn.trpc.core.properties.RpcProperties;
import cn.tannn.trpc.core.filter.FilterChain;
import lombok.Data;

/**
 * rpc上下文 - 参数传递
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 20:26
 */
@Data
public class RpcContext {

    /**
     * 过滤器
     */
    FilterChain filters;

    /**
     * 路由
     */
    Router router;

    /**
     * 负载均衡器
     */
    LoadBalancer loadBalancer;

    /**
     * 注册中心
     */
    RegistryCenter registryCenter;

    /**
     * 服务初始化配置 来自于配置文件
     */
    RpcProperties rpcProperties;
}
