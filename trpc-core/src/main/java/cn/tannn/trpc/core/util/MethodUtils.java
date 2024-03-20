package cn.tannn.trpc.core.util;

import cn.tannn.trpc.core.annotation.TConsumer;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tnnn
 * @version V1.0
 * @date 2024-03-12 18:43
 */
@Slf4j
public class MethodUtils {

    /**
     * 获取 方法签名
     *
     * @param method Method
     * @return 方法签名 方法名(参数类型1, 参数类型2)
     */
    public static String methodSign(Method method) {
        // 方法名
        StringBuilder signature = new StringBuilder(method.getName());
        // 参数个数
        signature.append("@").append(method.getParameterCount());

        Arrays.stream(method.getParameterTypes()).forEach(parameter -> {
            signature.append("_").append(parameter.getCanonicalName());
        });
        return signature.toString();
    }


    /**
     * 本地方法不代理
     * @param methodName Object 的一些方法名
     * @return boolean
     */
    public static boolean checkLocalMethod(final String methodName) {
        //本地方法不代理
        return "toString".equals(methodName) ||
                "hashCode".equals(methodName) ||
                "notifyAll".equals(methodName) ||
                "equals".equals(methodName) ||
                "wait".equals(methodName) ||
                "getClass".equals(methodName) ||
                "notify".equals(methodName);
    }


    /**
     * 本地方法不代理 - 被重写过后会拦截不到
     * @param method Object 的一些方法名
     * @return boolean
     */
    public static boolean checkLocalMethod(final Method method) {
        return method.getDeclaringClass().equals(Object.class);
    }



    /**
     * 获取自定注解的属性字段
     *
     * @param aClass Class
     * @return Field
     */
    public static  List<Field> findAnnotatedField(Class<?> aClass) {
        ArrayList<Field> result = new ArrayList<>();
        while (aClass != null) {
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(TConsumer.class)) {
                    result.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }


}
