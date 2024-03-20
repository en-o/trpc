package cn.tannn.trpc.core.config;

import cn.tannn.trpc.core.config.registry.RegistryCenterProperties;
import cn.tannn.trpc.core.enums.LoadBalancerEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * 客户端配置
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-17 13:39
 */
@ConfigurationProperties(prefix = "trpc")
@Component
@Getter
@Setter
@ToString
public class RpcProperties {

    /**
     * 客户端扫描包路径
     * <pr>
     *     - cn.tannn.trpc.demo.consumer.controller
     *     - cn.tannn.trpc.demo.consumer.runner
     * </pr>
     */
    private String[] scanPackages;


    /**
     * 负载均衡算法选择  [默认随机]
     */
    private LoadBalancerEnum loadBalancer;

    /**
     * 注册中心
     */
    @NestedConfigurationProperty
    private RegistryCenterProperties rc = new RegistryCenterProperties();

    /**
     * 设置默认值
     */
    public LoadBalancerEnum getLoadBalancer() {
        if(loadBalancer == null){
            return LoadBalancerEnum.RANDOM;
        }
        return loadBalancer;
    }


}
