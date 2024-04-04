package cn.tannn.trpc.core.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册中心订阅信息
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/4 下午5:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeProperties {

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
