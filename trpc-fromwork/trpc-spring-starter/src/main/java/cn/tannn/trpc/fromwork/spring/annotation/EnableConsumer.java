package cn.tannn.trpc.fromwork.spring.annotation;

import cn.tannn.trpc.fromwork.spring.consumer.ConsumerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 消费能力启用注解
 * @author tn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({ConsumerConfig.class})
public @interface EnableConsumer {
}
