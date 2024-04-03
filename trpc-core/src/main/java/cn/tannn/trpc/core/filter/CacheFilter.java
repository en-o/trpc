package cn.tannn.trpc.core.filter;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.api.RpcRequest;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  缓存过滤器  - 处理重复，重复直接返回结果
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/3 下午10:44
 */
@Order
public class CacheFilter  implements Filter {

    /**
     * todo : 优化 -> 容量，淘汰策略, 参数一致性策略
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

    @Override
    public int getOrder() {
        return 1;
    }
}
