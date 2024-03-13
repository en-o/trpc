package cn.tannn.trpc.core.util;

import java.lang.reflect.Method;

/**
 * @author tnnn
 * @version V1.0
 * @date 2024-03-12 18:43
 */
public class MethodUtils {

    /**
     * 获取 方法签名
     *
     * @param method Method
     * @return 方法签名 方法名(参数类型1, 参数类型2)
     */
    public static String getMethodSignature(Method method) {
        StringBuilder signature = new StringBuilder();
        signature.append(method.getName()).append("(");
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) {
                signature.append(", ");
            }
            signature.append(parameterTypes[i].getName());
        }
        signature.append(")");
        return signature.toString();
    }


    /**
     * 获取签名中的方法名
     *
     * @param signMethod 方法签名
     * @return 方法名
     */
    public static String analysisMethodSignatureName(String signMethod) {
        return signMethod.substring(0, signMethod.indexOf("(")).trim();
    }

    /**
     * 获取签名中的类型
     *
     * @param signMethod 方法签名
     * @return 类型 java.lang.String, java.lang.Integer
     */
    public static Class<?>[] analysisMethodSignatureParameterTypes(String signMethod) throws ClassNotFoundException {
        String substring = signMethod.substring(signMethod.indexOf("(") + 1, signMethod.length() - 1);
        if (substring.isEmpty()) {
            return null;
        }
        String[] split = substring.split(",");

        Class<?>[] classes = new Class[split.length];
        // 循环添加数据到数组中
        for (int i = 0; i < split.length; i++) {
            // 将字符串全限定名转换为 Class 对象
            if (split[i] != null && !split[i].isEmpty()) {
                classes[i] = TypeUtils.castPrimitive(split[i].trim());
            }
        }
        return classes;
    }

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

}
