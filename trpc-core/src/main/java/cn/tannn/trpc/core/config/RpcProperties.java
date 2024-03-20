package cn.tannn.trpc.core.config;

import cn.tannn.trpc.core.enums.LoadBalancerEnum;
import cn.tannn.trpc.core.enums.RegistryCenterEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@Data
public class RpcProperties {

    /**
     * 服务端访问地址,多个逗号隔开[静态注册 ip_port]
     * <pr>
     *     - 127.0.0.1_8083
     *     - 127.0.0.1_8083
     *     - 127.0.0.1_8083
     * </pr>
     */
    private String[] providers;

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


    private RegistryCenterEnum registryCenter;


    /**
     * 设置默认值
     */
    public LoadBalancerEnum getLoadBalancer() {
        if(loadBalancer == null){
            return LoadBalancerEnum.RANDOM;
        }
        return loadBalancer;
    }

    public RegistryCenterEnum getRegistryCenter() {
        if(registryCenter == null){
            return RegistryCenterEnum.DEF;
        }
        return registryCenter;
    }
}
