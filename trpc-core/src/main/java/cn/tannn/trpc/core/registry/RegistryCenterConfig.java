package cn.tannn.trpc.core.registry;

import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.enums.RegistryCenterEnum;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.properties.RpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册中心Bean加载
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/4 下午4:39
 */
@AutoConfiguration
@Slf4j
public class RegistryCenterConfig {




    /**
     * 加载注册中心
     * <pr>
     *  启动自动执行 RegistryCenter#start (在providerBootstrap.start()中执行)
     *  销毁自动执行 RegistryCenter#stop (在providerBootstrap.stop()中执行)
     * </pr>
     */
    @Bean
    RegistryCenter registryCenter(RpcProperties rpcProperties){
        RegistryCenter registryCenter;
        String[] providers = rpcProperties.getRc().getProviders();
        if(null == providers || providers.length == 0 ){
            registryCenter =  new RegistryCenter.StaticRegistryCenter(null);
        }else {
            List<InstanceMeta> instanceMetas = new ArrayList<>();
            for (String ipPortContext : providers) {
                String[] split = ipPortContext.split("_");
                instanceMetas.add(InstanceMeta.http(split[0], Integer.valueOf(split[1]), split[2]));
            }
            registryCenter =  new RegistryCenter.StaticRegistryCenter(instanceMetas);
        }
        return registryCenter;
    }




}
