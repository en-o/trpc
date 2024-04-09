package cn.tannn.trpc.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常描述信息
 * X  ==> 技术类异常
 * Y  ==> 业务类异常
 * Z  ==> unknown ，搞不清的异常，后面在归类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/3/27 下午8:21
 */
@Getter
@AllArgsConstructor
public enum ExceptionCode {
    /**
     * http 连接超时
     */
    SOCKET_TIME_EX("X", 1, "http_invoke_timeout"),
    /**
     * 方法不存在
     */
    NO_SUCH_METHOD_EX("X", 2, "method_not_exists"),
    /**
     * 不允许调用本地方法
     */
    ILLEGALITY_METHOD_EX("X", 3, "method_illegality"),
    /**
     * consumer请设置扫描包路径
     */
    SCAN_PACKAGE_EX("X", 4, "consumer_scan_package_not_setting"),
    /**
     * http地址不存在
     */
    HTTP_URI_EX("X", 5, "http_url_empty"),
    /**
     * 代理创建异常
     */
    PROXY_CREATE_EX("X", 6, "proxy_create_error"),

    /**
     * HTTP 调用失败
     */
    HTTP_POST_EX("X", 7, "http_call_error"),

    /**
     * ZK连接信息未设置
     */
    ZK_NOT_SETTING("X", 8, "zk_not_setting"),
    /**
     * zk注册失败
     */
    ZK_REGISTER_FAIL("X", 9, "zk_register_fail"),

    /**
     * zk卸载失败
     */
    ZK_UNREGISTER_FAIL("X", 10, "zk_unregister_fail"),

    /**
     * zk获取服务实例失败
     */
    ZK_FETCH_INSTANCE_FAIL("X", 11, "zk_fetch_instance_fail"),

    /**
     * 类不存在
     */
    NO_SUCH_CLASS_EX("X", 12, "class_not_exists"),

    /**
     * 流控
     */
    EXCEED_LIMIT_EX("X", 13, "tps_exceed_limit"),

    /**
     * 未知异常
     */
    UNKNOWN_EX("Z", 001, "unknown");

    /**
     * 异常类型
     * X  ==> 技术类异常
     * Y  ==> 业务类异常
     * Z  ==> unknown ，搞不清的异常，后面在归类
     */
    String type;
    /**
     * 状态码
     */
    Integer code;

    /**
     * 异常消息
     */
    String message;


}
