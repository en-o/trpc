package cn.tannn.trpc.core.config;

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
public class ConsumerProperties {

    /**
     * 服务端访问地址,多个逗号隔开
     * <pr>
     *     - http://127.0.0.1:8081
     *     - http://127.0.0.1:8082
     *     - http://127.0.0.1:8083
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
}
