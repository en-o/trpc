package cn.tannn.trpc.core.annotation;

import java.lang.annotation.*;

/**
 * 提供者注解,负责标记提供者
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 20:57
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface TProvider {
}
