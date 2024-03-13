package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.util.MethodUtil;
import cn.tannn.trpc.core.util.TypeUtils;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供者 处理类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@Data
public class ProvidersBootstrap implements ApplicationContextAware {
    /**
     * 存储所有的提供者 , 其中包含了[全限定名，对象实例]
     */
    private Map<String, Object> skeleton = new HashMap<>();

    /**
     * 拿到 所有标记了TProvider注解的类（所有的提供者），并存储
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        // 所有标记了 @TProvider 注解的类
        Map<String, Object> providers = context.getBeansWithAnnotation(TProvider.class);
        providers.values().forEach(x -> genInterface(x));
    }


    /**
     * 存储接口信息
     *
     * @param x 接口
     */
    public void genInterface(Object x) {
        // 默认只拿一个接口
        Class<?> anInterface = x.getClass().getInterfaces()[0];
        // 存储（全限定名，对象示例）
        skeleton.put(anInterface.getCanonicalName(), x);
    }


    /**
     * 调用接口
     *
     * @param request 接口元数据
     * @return 调用结果
     */
    public RpcResponse invokeRequest(RpcRequest request) {
        // todo 屏蔽 toString / equals 等 Object 的一些基本方法
        String requestMethodName = request.getMethodSign();
        if (MethodUtil.checkLocalMethod(MethodUtil.analysisMethodSignatureName(requestMethodName))) {
            return null;
        }
        RpcResponse rpcResponse = new RpcResponse();

        Object bean = skeleton.get(request.getService());
        try {
            Method method = findMethod(bean.getClass(), request.getMethodSign());
            Object result = method.invoke(bean, TypeUtils.cast(request.getArgs(), request.getMethodSign()));
            rpcResponse.setStatus(true);
            rpcResponse.setData(result);
        } catch (InvocationTargetException e) {
            // 把异常传递回去
            e.printStackTrace();
            // 多余的栈信息不要
            rpcResponse.setEx(new RuntimeException(e.getTargetException().getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            // 把异常传递回去
//            rpcResponse.setEx(e);
            // 多余的栈信息不要
            rpcResponse.setEx(new RuntimeException(e.getMessage()));
        }
        return rpcResponse;
    }


    /**
     * 查询方法
     *
     * @param aClass     对象
     * @param signatureMethod 方法签名
     * @return 查找到的方法
     */
    private Method findMethod(Class<?> aClass, String signatureMethod) {

        try {
            // 参数类型
            Class<?>[] parameterTypes = MethodUtil.analysisMethodSignatureParameterTypes(signatureMethod);
            String methodName = MethodUtil.analysisMethodSignatureName(signatureMethod);
            Method targetMethod;
            if(parameterTypes == null){
                targetMethod = aClass.getDeclaredMethod(methodName);
            }else {
                targetMethod = aClass.getDeclaredMethod(methodName, parameterTypes);
            }
            // 如果方法是私有的，需要设置可访问性
            targetMethod.setAccessible(true);
            return targetMethod;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
