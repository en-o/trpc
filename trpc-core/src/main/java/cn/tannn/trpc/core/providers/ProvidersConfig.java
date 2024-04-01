package cn.tannn.trpc.core.providers;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 服务提供者配置类 - springboot 集成
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/1 下午11:15
 */
@AutoConfiguration
public class ProvidersConfig {

    /**
     * 启动服务提供者信息扫描并存储
     */
    @Bean
    ProviderBootstrap providersBootstrap(){
        return new ProviderBootstrap();
    }

    /**
     * 注册 : RPC服务调用的核心方法
     */
    @Bean
    ProvidersInvoker providersInvoker(ProviderBootstrap providersBootstrap){
        return new ProvidersInvoker(providersBootstrap);
    }

}
