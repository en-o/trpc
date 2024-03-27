package cn.tannn.trpc.core.filter;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.core.util.MockUtils;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * mock 过滤 - 模拟请求
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/3/23 下午9:04
 */
public class MockFilter implements Filter {

    @SneakyThrows
    @Override
    public Object prefilter(RpcRequest request) {
        Class<?> service = Class.forName(request.getService());
        Method method = findMethod(service, request.getMethodSign());
        Class<?> clazz = method.getReturnType();
        return MockUtils.mock(clazz);
    }

    private Method findMethod(Class<?> service, String methodSign) {
       return Arrays.stream(service.getMethods())
               .filter( method -> MethodUtils.checkLocalMethod(method))
               .filter(method -> methodSign.equals(MethodUtils.methodSign(method)))
               .findFirst()
               .orElse(null);
    }

    @Override
    public Object postFilter(RpcRequest request,  Object result) {
        return null;
    }
}
