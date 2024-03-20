package cn.tannn.trpc.core.api;

import cn.tannn.trpc.core.meta.InstanceMeta;

import java.util.List;

/**
 * 负载均衡器 - 选择一个有效的路由 [AAWR-自适应，weightedRR-权重]
 * erma: 指数加权平均
 * @link <a href="https://cn.dubbo.apache.org/zh-cn/blog/2023/01/30/%E5%90%AF%E5%8F%91%E5%BC%8F%E6%B5%81%E6%8E%A7%E5%88%B6/">自适应负载均衡，启发式负载均衡</a>
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 19:12
 */
public interface LoadBalancer {

    /**
     * 选择路由
     * @param providers 服务提供者路由集合
     * @return 路由地址
     */
    InstanceMeta choose(List<InstanceMeta> providers);



    /**
     * 默认实现
     */
    LoadBalancer Default = providers -> {
        // 默认正常返回 provides 的第一个，为空就返回空
        return providers == null || providers.isEmpty() ? null : providers.get(0);
    };

}
