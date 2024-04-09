package cn.tannn.trpc.common.api;

/**
 * 过滤器 - 对路由前置后置处理
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-04-03 19:13
 */
public interface Filter{

    /**
     * 请求前置处理
     * @param request RpcRequest
     * @return result（不想处理就直接返回空）
     */
    Object prefilter(RpcRequest request);



    /**
     * 请求后置处理
     * @param request RpcRequest
     * @param result 返回值处理结果
     * @return RpcResponse （不想处理就直接返回空）
     */
    Object postFilter(RpcRequest request, Object result);


    /**
     * 排序 [倒叙]
     * @return 顺序值
     */
    int getOrder();


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

        @Override
        public int getOrder() {
            return 0;
        }
    };
}
