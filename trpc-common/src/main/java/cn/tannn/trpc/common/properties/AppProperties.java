package cn.tannn.trpc.common.properties;

import cn.tannn.trpc.common.properties.meta.GrayMetas;
import lombok.Data;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

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
     * 流控参数，每秒请求的次数超过次阈值就会过载保护[0不启用]，
     */
    Integer trafficControl = 0;

    // registry 信息  -- start
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
    // registry 信息  -- end

    /**
     * 灰度
     */
    @NestedConfigurationProperty
    GrayMetas gray = new GrayMetas();
}
