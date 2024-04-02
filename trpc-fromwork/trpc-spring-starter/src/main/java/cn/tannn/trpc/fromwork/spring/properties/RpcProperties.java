package cn.tannn.trpc.fromwork.spring.properties;

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
     * rpc暴露接口 的相关配置
     */
    @NestedConfigurationProperty
    private ApiProperties api;


    public ApiProperties getApi() {
        if(null == api ){
            return new ApiProperties();
        }
        return api;
    }

}
