package cn.tannn.trpc.core.config;

import cn.tannn.trpc.core.config.registry.RegistryCenterProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * rpc基础配置
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/22 15:00
 */
@ConfigurationProperties(prefix = "trpc")
@Component
@Getter
@Setter
@ToString
public class RpcProperties {

    /**
     * 服务信息 - 不做new处理让他不填直接错
     */
    @NestedConfigurationProperty
    private ServiceProperties app;

    /**
     * 客户端信息
     */
    @NestedConfigurationProperty
    ConsumerProperties consumer = new ConsumerProperties();

    /**
     * 注册中心
     */
    @NestedConfigurationProperty
    private RegistryCenterProperties rc = new RegistryCenterProperties();
}
