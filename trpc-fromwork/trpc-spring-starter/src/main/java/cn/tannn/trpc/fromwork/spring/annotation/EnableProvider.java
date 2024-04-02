package cn.tannn.trpc.fromwork.spring.annotation;

import cn.tannn.trpc.fromwork.spring.provider.ProvidersConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 提供者能力启用注解
 * @author tn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({ProvidersConfig.class})
public @interface EnableProvider {
}
