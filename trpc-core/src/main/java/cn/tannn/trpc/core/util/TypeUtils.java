package cn.tannn.trpc.core.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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


    /**
     * 处理 rpc 返回值的类型
     * @param method 方法
     * @param data 数据
     * @return data Type
     */
    @Nullable
    public static Object castMethodResult(Method method, Object data) {
        Class<?> returnType = method.getReturnType();
        if (data instanceof JSONObject jsonResult) {
            // 处理 Map<String,Bean> 中的Bean类型丢失
            if (Map.class.isAssignableFrom(returnType)) {
                Type genericReturnType = method.getGenericReturnType();
                Map resultMap = new HashMap<>();
                if (genericReturnType instanceof ParameterizedType parameterizedType) {
                    Type actualTypeKey = parameterizedType.getActualTypeArguments()[0];
                    Type actualTypeValue = parameterizedType.getActualTypeArguments()[1];
                    jsonResult.forEach((k,v) -> {
                        resultMap.put(TypeUtils.cast(k, (Class<?>) actualTypeKey),
                                TypeUtils.cast(v, (Class<?>) actualTypeValue));
                    });
                }
                return resultMap;
            }
            return jsonResult.toJavaObject(returnType);
        } else if (data instanceof JSONArray jsonArray){
            //  处理 List<Bean> 中的Bean类型丢失
            Object[] array = jsonArray.toArray();
            if (returnType.isArray()) {
                Class<?> componentType = returnType.getComponentType();
                Object resultArray = Array.newInstance(componentType, array.length);
                for (int i = 0; i < array.length; i++) {
                    if (componentType.isPrimitive() || componentType.getPackageName().startsWith("java")) {
                        Array.set(resultArray, i, array[i]);
                    } else {
                        Object castObject = TypeUtils.cast(array[i], componentType);
                        Array.set(resultArray, i, castObject);
                    }
                }
                return resultArray;
            } else if (List.class.isAssignableFrom(returnType)) {
                List<Object> resultList = new ArrayList<>(array.length);
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType parameterizedType) {
                    Type actualType = parameterizedType.getActualTypeArguments()[0];
                    for (Object o : array) {
                        resultList.add(TypeUtils.cast(o, (Class<?>) actualType));
                    }
                } else {
                    resultList.addAll(Arrays.asList(array));
                }
                return resultList;
            } else {
                return null;
            }
        } else {
            // 处理结果类型
//            return JSON.to(method.getReturnType(),  data);
            // TypeUtils.cast 是模拟上面那个写的
            return TypeUtils.cast(data, method.getReturnType());
        }
    }

}
