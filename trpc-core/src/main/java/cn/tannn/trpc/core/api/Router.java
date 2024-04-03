package cn.tannn.trpc.core.api;

import cn.tannn.trpc.core.meta.InstanceMeta;

import java.util.List;

/**
 * 路由器 - 针对负载均衡的前置处理， 比如某些引用需要走本地机房的节点服务，这个是时候就可以在router中勾选本地机房的节点集合后在在集合中负载均衡
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-04-03 19:13
 */
public interface Router {

    /**
     * 路由
     * @param providers 服务提供者
     * @return 路由
     */
    List<InstanceMeta> route(List<InstanceMeta> providers);

    /**
     * 默认实现
     */
    Router Default = p -> p;
}
