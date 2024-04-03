package cn.tannn.trpc.core.properties;

import cn.tannn.trpc.core.enums.LoadBalancerEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 消费端配置
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/2 16:29
 */
@Getter
@Setter
@ToString
public class ConsumerProperties {


    /**
     * 客户端配置服务端连接信息 - 配置文件方式
     * <p> 服务端访问地址,多个逗号隔开[静态注册 ip_port_context]
     *   <p>   - 127.0.0.1_8081_/ [ps 空的时候也要写_/]
     *   <p>   - 127.0.0.1_8082_/trpc
     */
    private String[] providers;


    /**
     * 负载均衡算法选择  [默认随机]
     */
    private LoadBalancerEnum loadBalancer;


    /**
     * rpc请求http配置
     */
    @NestedConfigurationProperty
    private HttpProperties http = new HttpProperties();


    /**
     * 负载均衡配置设置默认值
     */
    public LoadBalancerEnum getLoadBalancer() {
        if(loadBalancer == null){
            return LoadBalancerEnum.RANDOM;
        }
        return loadBalancer;
    }



}
