package cn.tannn.trpc.core.config;

import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.enums.RegistryCenterEnum;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.registry.zk.ZkRegistryCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册中心Bean加载
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/22 8:55
 */
@AutoConfiguration
@Slf4j
public class RegistryCenterConfig {
    RegistryCenter registryCenter;

    /**
     * 加载注册中心
     * <pr>
     *  启动自动执行 RegistryCenter#start (在providerBootstrap.start()中执行)
     *  销毁自动执行 RegistryCenter#stop (在providerBootstrap.stop()中执行)
     * </pr>
     */
    @Bean
    RegistryCenter registryCenter(RpcProperties rpcProperties){

        if(rpcProperties.getRc().getName().equals(RegistryCenterEnum.ZK)){
            registryCenter = new ZkRegistryCenter(rpcProperties.getRc());
        }else {
            String[] providers = rpcProperties.getRc().getProviders();
            if(null == providers || providers.length == 0 ){
                registryCenter =  new RegistryCenter.StaticRegistryCenter(null);
            }else {
                List<InstanceMeta> instanceMetas = new ArrayList<>();
                for (String s : providers) {
                    String[] ipPort = s.split("_");
                    InstanceMeta apply = InstanceMeta.http(ipPort[0], Integer.valueOf(ipPort[1]));
                    instanceMetas.add(apply);
                }
                registryCenter =  new RegistryCenter.StaticRegistryCenter(instanceMetas);
            }
        }
        return registryCenter;
    }




}