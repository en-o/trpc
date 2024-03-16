package cn.tannn.trpc.core.api;

import java.util.List;

/**
 * 路由
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
