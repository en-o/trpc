package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;

/**
 * http请求接口
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-20 20:40
 */
public interface HttpInvoker {

    /**
     * post
     * @param rpcRequest RpcRequest
     * @param url 请求地址
     * @return 返回结果
     */
    RpcResponse<Object> post(RpcRequest rpcRequest, String url);

}
