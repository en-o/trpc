package cn.tannn.trpc.core.api;

import java.util.List;

/**
 * 路由 - 针对负载均衡的前置处理， 比如某些引用需要走本地机房的节点服务，这个是时候就可以在router中勾选本地机房的节点集合后在在集合中负载均衡
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 19:13
 */
public interface Router<T> {

    /**
     * 路由
     * @param providers 服务提供者
     * @return 路由
     */
    List<T> route(List<T> providers);

    /**
     * 默认实现
     */
    Router Default = p -> p;
}
