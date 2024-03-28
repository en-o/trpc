package cn.tannn.trpc.core.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * RPC 客户端异常
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 9:22
 */
@Getter
@Setter
@ToString
public class TrpcException extends RuntimeException {

    /**
     * 错误
     */
    private ExceptionCode exceptionCode;


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

    public TrpcException(Throwable cause, ExceptionCode errcode) {
        super(cause);
        this.exceptionCode = errcode;
    }

    public TrpcException(ExceptionCode errcode) {
        super(errcode.getMessage());
        this.exceptionCode = errcode;
    }

    @Override
    public String getMessage() {
        if (null == exceptionCode) {
            return super.getMessage();
        } else {
            return exceptionCode.getType() + exceptionCode.getCode()
                    + "-" + exceptionCode.getMessage() + "-" + super.getMessage();
        }
    }
}
