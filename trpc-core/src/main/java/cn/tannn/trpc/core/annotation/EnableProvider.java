package cn.tannn.trpc.core.annotation;

import cn.tannn.trpc.core.providers.ProvidersConfig;
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
