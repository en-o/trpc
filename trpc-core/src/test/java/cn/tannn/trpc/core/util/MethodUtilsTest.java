package cn.tannn.trpc.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MethodUtilsTest {

    @Test
    void checkLocalMethod() throws NoSuchMethodException {
        // 不能处理重写方法
        assertFalse(MethodUtils.checkLocalMethod(UserImpl.class.getMethod("toString")));
        assertTrue(MethodUtils.checkLocalMethod(UserImpl.class.getMethod("hashCode")));
        assertFalse(MethodUtils.checkLocalMethod(UserImpl.class.getMethod("hi")));
    }

    @Test
    void testCheckLocalMethod() {
        assertTrue(MethodUtils.checkLocalMethod("toString"));
        assertTrue(MethodUtils.checkLocalMethod("hashCode"));
        assertFalse(MethodUtils.checkLocalMethod("hi"));
    }
}
