package cn.tannn.trpc.core.exception;


/**
 * 异常描述信息
 *  X  ==> 技术类异常
 *  Y  ==> 业务类异常
 *  Z  ==> unknown ，搞不清的异常，后面在归类
 * @author tnnn
 * @version V1.0
 * @date 2024/3/27 下午8:21
 */
public class ExceptionCode {
    /**
     * http 连接超时
     */
    public static final String SOCKET_TIME_EX = "X001"+"-"+"http_invoke_timeout";

    /**
     * 方法不存在
     */
    public static final String NO_SUCH_METHOD_EX = "X002"+"-"+"method_not_exists";

    /**
     * 未知异常
     */
    public static final String UNKNOWN_EX = "Z001"+"-"+"unknown";
}
