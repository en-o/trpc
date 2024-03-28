package cn.tannn.trpc.core.config;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.enums.FilterEnum;
import cn.tannn.trpc.core.enums.LoadBalancerEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Arrays;

/**
 * 客户端配置
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-17 13:39
 */
@Getter
@Setter
@ToString
public class ConsumerProperties {

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
     * 过滤器设置
     */
    private FilterEnum[] filter;


    /**
     * 服务信息 - 不做new处理让他不填直接错
     */
    @NestedConfigurationProperty
    private ServiceProperties service;


    /**
     * 设置默认值
     */
    public LoadBalancerEnum getLoadBalancer() {
        if(loadBalancer == null){
            return LoadBalancerEnum.RANDOM;
        }
        return loadBalancer;
    }

    /**
     * 设置默认
     * @return FilterEnum
     */
    public FilterEnum[] getFilter() {
        if(filter == null || filter.length == 0){
            return new FilterEnum[]{FilterEnum.DEFAULT};
        }
        return filter;
    }
}
