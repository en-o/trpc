package cn.tannn.trpc.core.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;

/**
 * mock
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/3/23 下午9:14
 */
public class MockUtils {

    public static Object mock(Class<?> type){
        if(type == null){
            return null;
        }else if(type.equals(Integer.class) || type.equals(Integer.TYPE)){
            return 1;
        }else if(type.equals(Long.class) || type.equals(Long.TYPE)) {
            return 10000L;
        }else if(Number.class.isAssignableFrom(type)){
            return 1;
        }else if(type.equals(String.class)){
            return "this_is_a_mock_string";
        }else {
            return mockPojo(type);
        }
    }

    @SneakyThrows
    public static Object mockPojo(Class<?> type){
        Object result = type.getDeclaredConstructor().newInstance();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> fValue = field.getType();
            field.set(result,fValue);
        }
        return result;
    }
}
