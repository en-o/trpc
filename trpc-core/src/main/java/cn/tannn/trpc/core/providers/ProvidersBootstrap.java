package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供者
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 21:33
 */
@Data
public class ProvidersBootstrap implements ApplicationContextAware {
    /**
     * 存储所有的提供者
     */
    private Map<String, Object> skeleton = new HashMap<>();

    /**
     *  拿到 所有标记了TProvider注解的类（所有的提供者）
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        // 所有标记了 @TProvider 注解的类
        Map<String, Object> providers = context.getBeansWithAnnotation(TProvider.class);
        providers.forEach((x, y) -> System.out.println("name: " + x));
        providers.values().forEach(x -> genInterface(x));
    }


    /**
     * 获取接口
     *
     * @param x x
     */
    public void genInterface(Object x) {
        // 默认只拿一个接口
        Class<?> anInterface = x.getClass().getInterfaces()[0];
        // 存储（全限定名，bean的实现类）
        skeleton.put(anInterface.getCanonicalName(), x);
    }


    /**
     * 调用接口
     * @param request 接口元数据
     * @return 调用结果
     */
    private RpcResponse invokeRequest(RpcRequest request) {
        Object bean = skeleton.get(request.getService());
        try {
            Method method = findMethod(bean.getClass(), request.getMethod());
            Object result = method.invoke(bean, request.getArgs());
            return new RpcResponse(true, result);
        }  catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    private Method findMethod(Class<?> aClass, String methodName){
        for (Method method : aClass.getMethods()) {
            if(method.getName().equals(methodName)){
                return method;
            }
        }
        return null;
    }

}
