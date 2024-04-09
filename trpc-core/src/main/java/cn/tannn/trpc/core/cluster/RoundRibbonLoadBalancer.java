package cn.tannn.trpc.core.cluster;

import cn.tannn.trpc.common.api.LoadBalancer;
import cn.tannn.trpc.common.meta.InstanceMeta;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡算法 - 轮询（均匀）
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/3 下午10:08
 */
public class RoundRibbonLoadBalancer implements LoadBalancer {

    /**
     * int 小心超过上限，出溢出问题 变成负数
     *  & 0x7fffffff  （保证溢出也是正数）
     */
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public InstanceMeta choose(List<InstanceMeta> providers) {
        if (providers == null || providers.isEmpty()) {
            return null;
        }
        if (providers.size() == 1) {
            return providers.get(0);
        }
        // 取模   只有 0,1 所以轮询（均匀）  （ & 0x7fffffff保证溢出也是正数）
        return providers.get((index.getAndIncrement()&0x7fffffff) % providers.size());
    }
}
