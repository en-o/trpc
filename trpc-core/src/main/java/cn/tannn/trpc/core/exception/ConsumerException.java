package cn.tannn.trpc.core.exception;

/**
 * RPC 客户端异常
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 9:22
 */
public class ConsumerException extends RuntimeException{

    public ConsumerException() {
        super();
    }

    public ConsumerException(String message) {
        super(message);
    }

    public ConsumerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsumerException(Throwable cause) {
        super(cause);
    }

}
