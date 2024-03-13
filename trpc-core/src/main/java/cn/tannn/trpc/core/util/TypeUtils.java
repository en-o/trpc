package cn.tannn.trpc.core.util;

import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;

/**
 * @author tnnn
 * @version V1.0
 * @date 2024-03-12 20:33
 */
public class TypeUtils {

    /**
     * 将 方法参数 赋予真实类型
     * @param origin 方法参数
     * @param signMethod 方法签名，签名中有参数的真实类型
     * @return Object
     */
    public static Object[] cast(Object[] origin, String signMethod) throws ClassNotFoundException {
        if (origin == null) {
            return null;
        }
        Object[] objects = new Object[origin.length];
        Class<?>[] types = MethodUtil.analysisMethodSignatureParameterTypes(signMethod);
        for (int i = 0; i < origin.length; i++) {
            objects[i] = cast(origin[i], types[i]);
        }

        return objects;

    }

    /**
     * 将 方法参数 赋予真实类型
     * @param origin Object
     * @param type Object真实类型
     * @return Object
     */
    public static Object cast(Object origin, Class<?> type) {
        Class<?> aClass = origin.getClass();
        if (type.isAssignableFrom(aClass)) {
            return origin;
        }
        if (origin instanceof HashMap map) {
            JSONObject jsonObject = new JSONObject(map);
            return jsonObject.toJavaObject(type);
        }
        if (type.equals(String.class)) {
            return origin.toString();
        }

//        if(type.isPrimitive()){
        if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            return Integer.valueOf(origin.toString());
        } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
            return Long.valueOf(origin.toString());
        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            return Double.valueOf(origin.toString());
        } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
            return Float.valueOf(origin.toString());
        } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
            return Byte.valueOf(origin.toString());
        } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
            return Short.valueOf(origin.toString());
        } else if (type.equals(Character.class) || type.equals(Character.TYPE)) {
            return origin;
        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            return Boolean.valueOf(origin.toString());
        }
//        }
        return null;
    }


    /**
     * 将基础类型变成包装类型
     * @param typeName 基础类型名 int,long ..
     * @return 包装类型
     * @throws ClassNotFoundException
     */
    public static Class<?> castPrimitive(String typeName) throws ClassNotFoundException {
        if ("int".equals(typeName)) {
            return Integer.class;
        }
        if ("long".equals(typeName)) {
            return Long.class;
        }
        if ("double".equals(typeName)) {
            return Double.class;
        }
        if ("float".equals(typeName)) {
            return Float.class;
        }
        if ("byte".equals(typeName)) {
            return Byte.class;
        }
        if ("short".equals(typeName)) {
            return Short.class;
        }
        if ("char".equals(typeName)) {
            return Character.class;
        }
        if ("boolean".equals(typeName)) {
            return Boolean.class;
        }
        return Class.forName(typeName);
    }
}
