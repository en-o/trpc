package cn.tannn.trpc.core.annotation;

import java.lang.annotation.*;

/**
 * 消费能力打标： 客户端，一般作用于接口类作为属性引用时标记在属性上
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/1 9:05
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface TConsumer {
}
