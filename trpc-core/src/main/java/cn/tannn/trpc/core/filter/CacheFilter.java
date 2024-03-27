package cn.tannn.trpc.core.filter;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.api.RpcRequest;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存过滤器  - 处理重复，重复直接返回结果
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/3/23 下午8:28
 */
@Order
public class CacheFilter implements Filter {
    /**
     * todo : 优化 -> 容量，淘汰策略, filter 排序
     * {request.toString(): result}
     */
    static Map<String, Object> cache = new ConcurrentHashMap();

    @Override
    public Object prefilter(RpcRequest request) {
        return cache.get(request.toString());
    }

    @Override
    public Object postFilter(RpcRequest request, Object result) {
        cache.putIfAbsent(request.toString(), result);
        return result;
    }
}
