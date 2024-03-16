package cn.tannn.trpc.core.cluster;

import cn.tannn.trpc.core.api.LoadBalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡算法 - 轮询（均匀）
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 19:53
 */
public class RoundRibonLoadBalancer<T> implements LoadBalancer<T> {

    /**
     * int 小心超过上限，出溢出问题 变成负数
     *  & 0x7fffffff  （保证溢出也是正数）
     */
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public T choose(List<T> providers) {
        if (providers == null || providers.isEmpty()) {
            return null;
        }
        if (providers.size() == 1) {
            return providers.get(0);
        }
        // 取模   只有 0,1 所以轮询（均匀）  （ & 0x7fffffff保证溢出也是正数）
        return providers.get(index.getAndIncrement()  % providers.size());
    }
}
