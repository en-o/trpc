package cn.tannn.trpc.common.api;

import cn.tannn.trpc.common.exception.TrpcException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc接口统一返回对象
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/1 9:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T> {
    /**
     * rpc请求执行状态
     */
    boolean status;

    /**
     * rpc请求执行得到的数据
     */
    T data;

    /**
     * rpc请求执行失败记录的错误
     */
    TrpcException ex;

}
