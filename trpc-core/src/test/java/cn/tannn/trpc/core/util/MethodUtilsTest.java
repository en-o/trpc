package cn.tannn.trpc.core.util;

import cn.tannn.trpc.core.util.entity.UserImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MethodUtilsTest {

    @Test
    void checkLocalMethod() throws NoSuchMethodException {
        // 不能处理重写方法
        assertFalse(MethodUtils.checkLocalMethod(UserImpl.class.getMethod("toString")));
        assertTrue(MethodUtils.checkLocalMethod(UserImpl.class.getMethod("hashCode")));
        assertFalse(MethodUtils.checkLocalMethod(UserImpl.class.getMethod("hi")));
    }

    @Test
    void checkLocalMethodSign() throws NoSuchMethodException {
        assertTrue(MethodUtils.checkLocalMethodSign("toString@0"));
        assertTrue(MethodUtils.checkLocalMethodSign("hashCode@0"));
        assertFalse(MethodUtils.checkLocalMethodSign("hi@0"));
    }

    @Test
    void testCheckLocalMethod() {
        assertTrue(MethodUtils.checkLocalMethod("toString"));
        assertTrue(MethodUtils.checkLocalMethod("hashCode"));
        assertFalse(MethodUtils.checkLocalMethod("hi"));
    }

    @Test
    void methodSign() throws NoSuchMethodException {
        assertEquals("toString@0",MethodUtils.methodSign(UserImpl.class.getMethod("toString")));
        assertEquals("hi@0",MethodUtils.methodSign(UserImpl.class.getMethod("hi")));
        assertEquals("lo@2_java.lang.String_java.lang.Integer",MethodUtils.methodSign(UserImpl.class.getMethod("lo",String.class,Integer.class)));
        assertEquals("lo@1_java.lang.String",MethodUtils.methodSign(UserImpl.class.getMethod("lo",String.class)));
    }


}
