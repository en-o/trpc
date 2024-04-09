package cn.tannn.trpc.core.cluster;

import cn.tannn.trpc.common.api.LoadBalancer;
import cn.tannn.trpc.common.meta.InstanceMeta;

import java.security.SecureRandom;
import java.util.List;

/**
 *  负载均衡算法 - 随机  流量规模不大的清空下随机已经可以满足大部分场景
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/3 下午10:08
 */
public class RandomLoadBalancer implements LoadBalancer {

    SecureRandom random = new SecureRandom();
    @Override
    public InstanceMeta choose(List<InstanceMeta> providers) {
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
