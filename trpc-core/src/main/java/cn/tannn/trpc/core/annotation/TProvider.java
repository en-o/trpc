package cn.tannn.trpc.core.annotation;

import java.lang.annotation.*;

/**
 * 服务能力打标： 服务端，一般作用于接口实现类上
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/1 9:05
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TProvider {
}
