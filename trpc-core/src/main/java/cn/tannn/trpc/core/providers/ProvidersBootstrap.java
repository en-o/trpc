package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.meta.ProviderMeta;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.core.util.TypeUtils;
import com.alibaba.fastjson2.JSON;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

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
        for (Method method : anInterface.getMethods()) {
            //  这里可以过滤 Object的一些方法
            if (MethodUtils.checkLocalMethod(method)) {
                continue;
            }
            createProvider(anInterface, x, method);
        }

    }

    /**
     * 存储 能力提供者信息
     */
    private void createProvider(Class<?> anInterface, Object aclass, Method method) {
        ProviderMeta providerMeta = new ProviderMeta();
        providerMeta.setMethod(method);
        providerMeta.setServiceImpl(aclass);
        providerMeta.setMethodSign(MethodUtils.methodSign(method));
        System.out.println(" create a provider: " + providerMeta);
        skeleton.add(anInterface.getCanonicalName(), providerMeta);
    }


    /**
     * 调用接口
     *
     * @param request 接口元数据
     * @return 调用结果
     */
    public RpcResponse invokeRequest(RpcRequest request) {
        // todo 屏蔽 toString / equals 等 Object 的一些基本方法
        RpcResponse rpcResponse = new RpcResponse();
        List<ProviderMeta> providerMetas = skeleton.get(request.getService());
        try {
            ProviderMeta meta = findProviderMeta(providerMetas, request.getMethodSign());
            Method method = meta.getMethod();
            Object[] args = processArgs(request.getArgs(), method.getParameterTypes());
            Object result = method.invoke(meta.getServiceImpl(), args);
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
     * 处理参数的实际类型
     * @param args 参数
     * @param parameterTypes 参数类型
     * @return
     */
    private Object[] processArgs(Object[] args, Class<?>[] parameterTypes) {
        if(args == null || args.length == 0){
            return args;
        }
        Object[] actuals = new Object[args.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            actuals[i] = JSON.to(parameterTypes[i],  args[i]);
            // 下面这个是模拟上面那个写的
//            actuals[i] = TypeUtils.cast(args[i],parameterTypes[i]);
        }
        return actuals;
    }

    /**
     * 根据签名获取当前方法的元数据
     *
     * @param providerMetas 提供者元数据集合
     * @param methodSign    方法签名
     * @return ProviderMeta
     */
    private ProviderMeta findProviderMeta(List<ProviderMeta> providerMetas, String methodSign) {
        Optional<ProviderMeta> first = providerMetas.stream().filter(providerMeta -> providerMeta.getMethodSign().equals(methodSign)).findFirst();
        return first.orElse(null);
    }

}
