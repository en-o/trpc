package cn.tannn.trpc.core.api;

import java.util.List;

/**
 * 负载均衡器 - 选择一个有效的路由 [AAWR-自适应，weightedRR-权重]
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 19:12
 */
public interface LoadBalancer<T> {

    /**
     * 选择路由
     * @param providers 服务提供者路由集合
     * @return 路由地址
     */
    T choose(List<T> providers);



    /**
     * 默认实现
     */
    LoadBalancer Default = providers -> {
        // 默认正常返回 provides 的第一个，为空就返回空
        return providers == null || providers.isEmpty() ? null : providers.get(0);
    };

}
