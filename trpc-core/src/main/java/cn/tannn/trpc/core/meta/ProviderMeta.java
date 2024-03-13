package cn.tannn.trpc.core.meta;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * 提供者元数据
 *  - 存储的提供者信息可以减少频繁反射获取getDeclaredMethod(String name, Class<?>... parameterTypes)的性能问题
 * @author tnnn
 * @version V1.0
 * @date 2024-03-13 20:18
 */
@Data
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
