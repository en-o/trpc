package cn.tannn.trpc.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;


class MethodUtilTest {

    @Test
    void getMethodSignature() {
        Method[] methods = User.class.getMethods();
        for (Method method : methods) {
            //lo(java.lang.String)
            //lo(java.lang.String, java.lang.Integer)
            //hi()
            System.out.println(MethodUtil.getMethodSignature(method));
        }

    }


    @Test
    void analysisMethodSignatureName() {
        Method[] methods = User.class.getMethods();
        for (Method method : methods) {
            // lo(java.lang.String)
            // lo(java.lang.String, java.lang.Integer)
            // hi()
            String methodSignature = MethodUtil.getMethodSignature(method);
            // lo
            // lo
            // hi
            System.out.println(MethodUtil.analysisMethodSignatureName(methodSignature));
        }
    }

    @Test
    void analysisMethodSignatureParameterTypes() throws ClassNotFoundException {
        Method[] methods = User.class.getMethods();
        for (Method method : methods) {
            // lo(java.lang.String)
            // lo(java.lang.String, java.lang.Integer)
            // hi()
            String methodSignature = MethodUtil.getMethodSignature(method);
            // [class java.lang.String]
            // [class java.lang.String, class java.lang.Integer]
            // null
            System.out.println(Arrays.toString(MethodUtil.analysisMethodSignatureParameterTypes(methodSignature)));
        }
    }
    @Test
    void cccc(){
        System.out.println(Long.class.isPrimitive());
    }
}
