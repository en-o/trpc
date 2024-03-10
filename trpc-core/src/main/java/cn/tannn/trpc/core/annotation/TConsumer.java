package cn.tannn.trpc.core.annotation;

import java.lang.annotation.*;

/**
 * 消费者
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 19:38
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface TConsumer {
}
