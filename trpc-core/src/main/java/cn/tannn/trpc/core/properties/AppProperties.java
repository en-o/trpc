package cn.tannn.trpc.core.properties;

import lombok.Data;

/**
 * 服务注册信息
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/4 下午5:13
 */
@Data
public class AppProperties {

    /**
     * 订阅的服务名称
     */
    String appid="provider";
    /**
     * 订阅的空间
     */
    String namespace = "public";
    /**
     * 订阅的环境
     */
    String env = "dev";
    /**
     * 订阅的版本
     */
    String version = "0.0.1";
}
