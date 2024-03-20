package cn.tannn.trpc.core.exception;

/**
 * RPC 注册中心异常
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 9:23
 */
public class RegistryCenterException extends RuntimeException{

    public RegistryCenterException() {
        super();
    }

    public RegistryCenterException(String message) {
        super(message);
    }

    public RegistryCenterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistryCenterException(Throwable cause) {
        super(cause);
    }

}
