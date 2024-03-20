package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.config.RpcProperties;
import cn.tannn.trpc.core.enums.RegistryCenterEnum;
import cn.tannn.trpc.core.registry.ZkRegistryCenter;
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
    ProviderBootstrap providersBootstrap(){
       return new ProviderBootstrap();
    }

    /**
     * 在 applicationRunner后主动调用，确保实例全部加载完成，防止初始化的过程中被注册使用导致ClassNotFoundException
     * @param providerBootstrap ProviderBootstrap
     * @return ApplicationRunner
     */
    @Bean
    @Order(Integer.MIN_VALUE)
    public ApplicationRunner providerBootstrapRunner(ProviderBootstrap providerBootstrap) {
        return x -> providerBootstrap.start();
    }

    /**
     * 加载注册中心
     * <pr>
     *  启动自动执行 RegistryCenter#start (在providerBootstrap.start()中执行)
     *  销毁自动执行 RegistryCenter#stop (在providerBootstrap.stop()中执行)
     * </pr>
     */
    @Bean
    RegistryCenter consumerRc(RpcProperties rpcProperties){
        if(rpcProperties.getRc().getName().equals(RegistryCenterEnum.ZK)){
            return new ZkRegistryCenter(rpcProperties.getRc());
        }else {
            return new RegistryCenter.StaticRegistryCenter(null);
        }
    }

}
