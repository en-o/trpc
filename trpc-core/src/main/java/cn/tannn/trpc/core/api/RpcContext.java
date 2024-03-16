package cn.tannn.trpc.core.api;

import lombok.Data;

import java.util.List;

/**
 * rpc上下文 - 参数传递
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 20:26
 */
@Data
public class RpcContext<T> {

    /**
     * 过滤器
     */
    List<Filter> filters;

    /**
     * 路由
     */
    Router<T> router;

    /**
     * 负载均衡器
     */
    LoadBalancer<T> loadBalancer;
}
