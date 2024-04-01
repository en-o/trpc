package cn.tannn.trpc.core.providers;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.core.meta.ProviderMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
public class ProvidersInvoker {

    /**
     * 本地缓存 - ProviderBootstrap透传过来的提供者[类全限定名,Provider的映射关系],用着反射调用元数据
     */
    private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

    public ProvidersInvoker(MultiValueMap<String, ProviderMeta> skeleton) {
        this.skeleton = skeleton;
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
            // 这里要处理 重载 和 参数类型转换 , // todo 放到序列化参数类型处理那里做
            Object result = method.invoke(meta.getServiceImpl(), request.getArgs());
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
}
