package cn.tannn.trpc.core.exception;

/**
 * RPC 客户端异常
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 9:22
 */
public class RpcException extends RuntimeException{

    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

}
