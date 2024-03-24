package cn.tannn.trpc.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc请求
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 20:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest {
    /**
     * 接口(全限定名) : cn.tannn.trpc.demo.api.UserService
     */
    private String service;

    /**
     * 方法签名
     */
    private String methodSign;

    /**
     * 参数 : 100
     */
    private Object[] args;

}
