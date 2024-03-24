package cn.tannn.trpc.core.api;

/**
 * 过滤器 - 对路由前置后置处理
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 19:13
 */
public interface Filter {

    /**
     * 请求前置处理
     * @param request RpcRequest
     * @return result
     */
    Object prefilter(RpcRequest request);



    /**
     * 请求后置处理
     * @param response RpcResponse
     * @param request RpcRequest
     * @param result 返回值处理结果
     * @return RpcResponse
     */
    Object postFilter(RpcRequest request, Object result);


    /**
     * 默认实现
     */
    Filter Default = new Filter(){

        @Override
        public Object prefilter(RpcRequest request) {
            return null;
        }

        @Override
        public Object postFilter(RpcRequest request, Object object) {
            return null;
        }
    };
}
