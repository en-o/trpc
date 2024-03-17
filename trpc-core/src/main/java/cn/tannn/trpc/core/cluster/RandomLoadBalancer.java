package cn.tannn.trpc.core.cluster;

import cn.tannn.trpc.core.api.LoadBalancer;

import java.security.SecureRandom;
import java.util.List;

/**
 * 负载均衡算法 - 随机  流量规模不大的清空下随机已经可以满足大部分场景
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 19:53
 */
public class RandomLoadBalancer<T> implements LoadBalancer<T> {
    SecureRandom random = new SecureRandom();
    @Override
    public T choose(List<T> providers) {
         if(providers == null || providers.isEmpty()) {
             return null;
         }
         if(providers.size() == 1) {
             return providers.get(0);
         }
         // 随机取一个
         return providers.get(random.nextInt(providers.size()));
    }
}
