package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.properties.RpcProperties;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

/**
 * 配置类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@AutoConfiguration
public class ProvidersConfig {


    /**
     * init - method
     */
    @Bean
    ProviderBootstrap providersBootstrap(RpcProperties rpcProperties){
       return new ProviderBootstrap(rpcProperties);
    }

    /**
     * create - providersInvoker
     */
    @Bean
    ProvidersInvoker providersInvoker(ProviderBootstrap providersBootstrap){
        return new ProvidersInvoker(providersBootstrap);
    }

    /**
     * 在 applicationRunner后主动调用，确保实例全部加载完成，防止初始化的过程中被注册使用导致ClassNotFoundException
     * @param providerBootstrap ProviderBootstrap
     * @return ApplicationRunner
     */
    @Bean
    @Order(Integer.MIN_VALUE+1)
    public ApplicationRunner providerBootstrapRunner(ProviderBootstrap providerBootstrap) {
        return x -> providerBootstrap.start();
    }



}
