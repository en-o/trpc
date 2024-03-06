package cn.tannn.trpc.core.providers;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * 配置类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@Configurable
public class ProvidersConfig {

    @Bean
    private ProvidersBootstrap providersBootstrap(){
       return new ProvidersBootstrap();
    }
}
