package cn.tannn.trpc.core.api;

import cn.tannn.trpc.core.filter.FilterChain;
import cn.tannn.trpc.core.properties.RpcProperties;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc上下文 - 参数传递
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-04-03 20:26
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
     * 服务初始化配置 来自于配置文件
     */
    RpcProperties rpcProperties;

    /**
     * 注册中心
     */
    RegistryCenter registryCenter;


    /**
     * 线程绑定参数
     */
    public static ThreadLocal<Map<String,String>> ContextParameters = ThreadLocal.withInitial(HashMap::new);

    public static void setContextParameter(String key, String value) {
        ContextParameters.get().put(key, value);
    }

    public static String getContextParameter(String key) {
        return ContextParameters.get().get(key);
    }

    public static void removeContextParameter(String key) {
        ContextParameters.get().remove(key);
    }
}
