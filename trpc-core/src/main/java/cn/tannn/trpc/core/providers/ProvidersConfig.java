package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.config.ConsumerProperties;
import cn.tannn.trpc.core.registry.ZkRegistryCenter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 配置类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@AutoConfiguration
public class ProvidersConfig {

    @Bean
    ProvidersBootstrap providersBootstrap(){
       return new ProvidersBootstrap();
    }

    /**
     * 注册中心
     * <pr>
     *  启动执行 start
     *  销毁执行 stop
     * </pr>
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    RegistryCenter provider_rc(){
        return new ZkRegistryCenter();
    }
}
