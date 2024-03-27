package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.exception.RpcException;
import cn.tannn.trpc.core.meta.ProviderMeta;
import cn.tannn.trpc.core.util.TypeUtils;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * 服务调用
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-20 20:17
 */
public class ProvidersInvoker {

    /**
     * 存储所有的提供者 , 其中包含了[全限定名，提供者元数据]
     */
    private MultiValueMap<String, ProviderMeta> skeleton;


    public ProvidersInvoker(ProviderBootstrap provider) {
        this.skeleton = provider.getSkeleton();
    }

    /**
     * 方法调用 [反射] 写在这儿使用为要用 skeleton, 如何升级到了其他存储就可以把他单独放出去
     *
     * @param request 接口元数据
     * @return 调用结果
     */
    public RpcResponse<Object> invoke(RpcRequest request) {
        RpcResponse<Object> rpcResponse = new RpcResponse<>();
        List<ProviderMeta> providerMetas = skeleton.get(request.getService());
        try {
            ProviderMeta meta = findProviderMeta(providerMetas, request.getMethodSign());
            if (meta == null) {
                throw new RpcException("非法RPC方法调用，当前方法不是RPC接口");
            }
            Method method = meta.getMethod();
            Object[] args = processArgs(request.getArgs(), method.getParameterTypes());
            Object result = method.invoke(meta.getServiceImpl(), args);
            rpcResponse.setStatus(true);
            rpcResponse.setData(result);
        } catch (InvocationTargetException e) {
            // 把异常传递回去
            rpcResponse.setEx(new RuntimeException(e.getTargetException().getMessage()));
        }  catch (IllegalAccessException e) {
            // 把异常传递回去
            rpcResponse.setEx(new RuntimeException(e.getMessage()));
        }
        return rpcResponse;
    }


    /**
     * 处理参数的实际类型
     *
     * @param args           参数
     * @param parameterTypes 参数类型
     * @return Object
     */
    private Object[] processArgs(Object[] args, Class<?>[] parameterTypes) {
        if (args == null || args.length == 0) {
            return args;
        }
        Object[] actuals = new Object[args.length];
        for (int i = 0; i < parameterTypes.length; i++) {
//            actuals[i] = JSON.to(parameterTypes[i],  args[i]);
            // TypeUtils.cast 是模拟上面那个写的
            actuals[i] = TypeUtils.cast(args[i], parameterTypes[i]);
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
        Optional<ProviderMeta> first = providerMetas.stream()
                .filter(m -> m.getMethodSign().equals(methodSign))
                .findFirst();
        return first.orElse(null);
    }
}
