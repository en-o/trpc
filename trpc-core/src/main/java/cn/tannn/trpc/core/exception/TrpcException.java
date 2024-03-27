package cn.tannn.trpc.core.exception;

/**
 * RPC 客户端异常
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 9:22
 */
public class TrpcException extends RuntimeException{

    public TrpcException() {
        super();
    }

    public TrpcException(String message) {
        super(message);
    }

    public TrpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrpcException(Throwable cause) {
        super(cause);
    }

}
