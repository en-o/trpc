package cn.tannn.trpc.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rpc返回
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 20:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T>  {
    /**
     * 状态：true
     */
    boolean status;

    /**
     * 接口执行得到数据
     */
    T data;

    /**
     * 处理错误
     */
    Exception ex;

}
