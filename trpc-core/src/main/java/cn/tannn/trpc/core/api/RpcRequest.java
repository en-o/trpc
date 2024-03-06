package cn.tannn.trpc.core.api;

import lombok.Data;

/**
 * rpc请求
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 20:49
 */
@Data
public class RpcRequest {
    /**
     * 接口(全限定名) : cn.tannn.trpc.demo.api.UserService
     */
    private String service;

    /**
     * 方法 : findById()
     */
    private String method;

    /**
     * 参数 : 100
     */
    private Object[] args;
}
