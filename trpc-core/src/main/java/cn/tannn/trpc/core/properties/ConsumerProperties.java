package cn.tannn.trpc.core.properties;

import cn.tannn.trpc.core.enums.FilterEnum;
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
     * 负载均衡算法选择  [默认随机]
     */
    private LoadBalancerEnum loadBalancer;

    /**
     * 过滤器设置
     */
    private FilterEnum[] filter;

    /**
     * 灰度调拨百分比 0-100
     */
    private Integer grayRatio = 0;



    /**
     * rpc请求http配置
     */
    @NestedConfigurationProperty
    private HttpProperties http = new HttpProperties();

    /**
     * 订阅的服务信息
     */
    @NestedConfigurationProperty
    private SubscribeProperties  subscribe = new SubscribeProperties();


    /**
     * 负载均衡配置设置默认值
     */
    public LoadBalancerEnum getLoadBalancer() {
        if(loadBalancer == null){
            return LoadBalancerEnum.RANDOM;
        }
        return loadBalancer;
    }


    /**
     *过滤器设置默认
     * @return FilterEnum
     */
    public FilterEnum[] getFilter() {
        if(filter == null || filter.length == 0){
            return new FilterEnum[]{FilterEnum.DEFAULT};
        }
        return filter;
    }


}
