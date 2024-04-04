package cn.tannn.trpc.core.properties;

import cn.tannn.trpc.core.properties.rc.RegistryCenterProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * prc配置文件
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/2 10:43
 */
@ConfigurationProperties(prefix = "trpc")
@Component
@Getter
@Setter
@ToString
public class RpcProperties {

    /**
     * 设置 trpc扫描包路径，获取 RPC注解 标记的服务
     * <pr>
     *     - cn.tannn.trpc.demo.consumer.controller
     *     - cn.tannn.trpc.demo.consumer.runner
     * </pr>
     */
    private String[] scanPackages;

    /**
     * 注册中心
     */
    @NestedConfigurationProperty
    private RegistryCenterProperties rc = new RegistryCenterProperties();


    /**
     * rpc暴露接口 的相关配置
     */
    @NestedConfigurationProperty
    private ApiProperties api;


    /**
     * 客户端信息
     */
    @NestedConfigurationProperty
    private ConsumerProperties consumer = new ConsumerProperties();


    public ApiProperties getApi() {
        if(null == api ){
            return new ApiProperties();
        }
        return api;
    }

}
