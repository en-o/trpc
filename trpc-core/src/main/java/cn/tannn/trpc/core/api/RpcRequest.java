package cn.tannn.trpc.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc 接口的统一请求对象
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/1 9:05
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
     * 方法签名 name@参数个数_参数类型1_参数类型2
     */
    private String methodSign;

    /**
     * 参数
     */
    private Object[] args;
}
