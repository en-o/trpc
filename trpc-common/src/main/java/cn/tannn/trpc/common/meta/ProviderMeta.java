package cn.tannn.trpc.common.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * 描述Provider的映射关系
 *  - 存储的提供者信息可以减少频繁反射获取getDeclaredMethod(String name, Class<?>... parameterTypes)的性能问题
 * @author tnnn
 * @version V1.0
 * @date 2024-03-13 20:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderMeta {
    /**
     * 具体方法
     */
    Method method;
    /**
     * 方法签名
     */
    String methodSign;

    /**
     * 方法属于的实现类
     */
    Object serviceImpl;
}
