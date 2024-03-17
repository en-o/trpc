package cn.tannn.trpc.core.providers;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

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
}
