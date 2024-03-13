package cn.tannn.trpc.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;


class MethodUtilsTest {

    @Test
    void cccc(){
        System.out.println(Long.class.isPrimitive());
    }

    @Test
    void methodSign2() {
        Method[] methods = User.class.getMethods();
        for (Method method : methods) {
            System.out.println(MethodUtils.methodSign(method));
        }
    }
}
