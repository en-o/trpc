package cn.tannn.trpc.core.util;

import com.alibaba.fastjson2.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

/**
 * @author tnnn
 * @version V1.0
 * @date 2024-03-12 20:33
 */
public class TypeUtils {



    /**
     * 将 方法参数 赋予真实类型
     * @param origin Object
     * @param type Object真实类型
     * @return Object
     */
    public static Object cast(Object origin, Class<?> type) {
        if(origin == null){
            return null;
        }
        Class<?> aClass = origin.getClass();
        if (type.isAssignableFrom(aClass)) {
            return origin;
        }

        // 处理数组
        if(type.isArray()){
            if(origin instanceof List<?> list){
                origin = list.toArray();
            }
            int length = Array.getLength(origin);
            Class<?> componentType = type.getComponentType();
            Object resultArray = Array.newInstance(componentType, length);
            for (int i = 0; i < length; i++) {
                if (componentType.isPrimitive() || componentType.getPackageName().startsWith("java")) {
                    Array.set(resultArray, i, Array.get(origin, i));
                } else {
                    Object castObject = cast(Array.get(origin, i), componentType);
                    Array.set(resultArray, i, castObject);
                }
            }
            return resultArray;
        }
        // 处理map
        if (origin instanceof HashMap<?,?> map) {
            JSONObject jsonObject = new JSONObject(map);
            return jsonObject.toJavaObject(type);
        }

        // 处理 String
        if (type.equals(String.class)) {
            return origin.toString();
        }

        // 处理 基本类型
        // todo 处理类型的时候 totString 可能会影响性能
        // ps： 假如传入的参数是Boolean，但是需要的类型是boolean，这段代码会先转string，再valueof
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
            return Character.valueOf(origin.toString().charAt(0));
        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            return Boolean.valueOf(origin.toString());
        }
        return null;
    }

}
