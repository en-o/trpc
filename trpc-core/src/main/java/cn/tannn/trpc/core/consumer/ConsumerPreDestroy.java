package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.providers.ProviderBootstrap;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;

/**
 * 消费端 注销rc操作，如果服务端同时存在，销毁操作只由服务端发起
 * @author tnnn
 * @version V1.0
 * @date 2024/3/23 上午11:17
 */
@AutoConfiguration
@ConditionalOnMissingBean(ProviderBootstrap.class)
@Slf4j
public class ConsumerPreDestroy {

    /**
     * spring 上下文
     */
    @Autowired
    private ApplicationContext context;

    /**
     * spring boot 生命完结时自动销毁
     */
    @PreDestroy
    public void stop(){
        log.info("ConsumerBootstrap stop...");
        // 注册中心工作结束下班
        RegistryCenter registryCenter = context.getBean(RegistryCenter.class);
        registryCenter.stop();
        log.info("ConsumerBootstrap stopped.");
    }
}
