package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.core.meta.ProviderMeta;
import cn.tannn.trpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static cn.tannn.trpc.core.exception.ExceptionCode.NO_SUCH_METHOD_EX;

/**
 * RPC服务调用的核心方法 - 反射执行参数方法
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/1 下午11:06
 */
@Slf4j
public class ProviderInvoker {

    /**
     * 本地缓存 - ProviderBootstrap透传过来的提供者[类全限定名,Provider的映射关系],用着反射调用元数据
     */
    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

    public ProviderInvoker(ProviderBootstrap providersBootstrap) {
        this.skeleton = providersBootstrap.getSkeleton();
    }



    /**
     * 方法调用  - 为RPC暴露出去的接口提供功能支持
     *
     * @param request 调用信息[包含了调用那个类,那个方法,方法的参数]
     * @return 调用结果
     */
    public RpcResponse<Object> invoke(RpcRequest request) {
        log.debug(" ===> ProviderInvoker.invoke(request:{})", request);
        RpcResponse<Object> rpcResponse = new RpcResponse<>();
        List<ProviderMeta> providerMetas = skeleton.get(request.getService());
        try {
            // 通过请求参数去 skeleton 里查询存储的提供者基础信息,用户下面的调用请求
            ProviderMeta meta = findProviderMeta(providerMetas, request.getMethodSign());
            if (meta == null) {
                // "非法RPC方法调用，当前方法不是RPC接口"
                throw new TrpcException(NO_SUCH_METHOD_EX);
            }
            Method method = meta.getMethod();
            // 参数类型转换  - 因为序列化过程中数据类型丢失了
            Object[] args = processArgs(request.getArgs(), method.getParameterTypes(), method.getGenericParameterTypes());
            Object result = method.invoke(meta.getServiceImpl(), args);
            rpcResponse.setStatus(true);
            rpcResponse.setData(result);
        } catch (InvocationTargetException e) {
            // 把异常传递回去
            rpcResponse.setEx(new TrpcException(e.getTargetException().getMessage()));
        }  catch (IllegalAccessException | IllegalArgumentException e) {
            // 把异常传递回去
            rpcResponse.setEx(new TrpcException(e.getMessage()));
        }
        log.debug(" ===> ProviderInvoker.invoke() = {}", rpcResponse);
        return rpcResponse;
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


    /**
     * 处理参数的实际类型
     *
     * @param args           参数
     * @param parameterTypes 参数类型
     * @param genericParameterTypes 泛型类型
     * @return Object
     */
    private Object[] processArgs(Object[] args, Class<?>[] parameterTypes, Type[] genericParameterTypes) {
        if (args == null || args.length == 0) {
            return args;
        }
        Object[] actuals = new Object[args.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            // actuals[i] = JSON.to(parameterTypes[i],  args[i]);
            actuals[i] = TypeUtils.castGeneric(args[i], parameterTypes[i], genericParameterTypes[i]);
        }
        return actuals;
    }
}
