package cn.tannn.trpc.core.exception;

/**
 * RPC 服务端异常
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 9:23
 */
public class ProviderException extends RuntimeException{

    public ProviderException() {
        super();
    }

    public ProviderException(String message) {
        super(message);
    }

    public ProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProviderException(Throwable cause) {
        super(cause);
    }

}
