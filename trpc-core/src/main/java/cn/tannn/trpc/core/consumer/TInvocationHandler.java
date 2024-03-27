package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.api.RpcContext;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.consumer.http.OkHttpInvoker;
import cn.tannn.trpc.core.exception.ConsumerException;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 消费端：动态代理
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 20:03
 */
@Slf4j
public class TInvocationHandler implements InvocationHandler {
    Class<?> service;
    RpcContext rpcContext;
    List<InstanceMeta> providers;

    HttpInvoker httpInvoker = new OkHttpInvoker();



    public TInvocationHandler(Class<?> service, RpcContext rpcContext, List<InstanceMeta> providers) {
        this.service = service;
        this.rpcContext = rpcContext;
        this.providers = providers;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 屏蔽 toString / equals 等 Object 的一些基本方法
        if (MethodUtils.checkLocalMethod(method)) {
            return null;
        }

        // 组装调用参数 ： 类全限定名称，方法，参数
        RpcRequest rpcRequest = new RpcRequest(service.getCanonicalName(), MethodUtils.methodSign(method), args);
        // 对请求就像处理: 缓存减少IO[CacheFilter],
        Object prefilter = rpcContext.getFilters().executePref(rpcRequest);
        if(prefilter != null) {
            return prefilter;
        }
        // 路由
        List<InstanceMeta> instances = rpcContext.getRouter().route(this.providers);
        // 选择路由
        InstanceMeta instance = rpcContext.getLoadBalancer().choose(instances);
        log.debug("loadBalancer.choose(urls) ==> {}", instance);
        // 发送请求
        RpcResponse<Object> rpcResponse = httpInvoker.post(rpcRequest,  instance.toUrl());
        Object result = castReturnResult(method, rpcResponse);
        // 对结果进行处理 - 可能不是最终值，需要设计
        Object filterResult = rpcContext.getFilters().executePost(rpcRequest, result);
        // 不是空就返回处理之后的结果
        if(filterResult != null){
            return filterResult;
        }
        return result;
    }

    /**
     * 处理RpcResponse格式
     * @param method Method
     * @param rpcResponse RpcResponse
     * @return Object
     */
    private static @Nullable Object castReturnResult(Method method, RpcResponse<Object> rpcResponse) {
        if (rpcResponse.isStatus()) {
            Object data = rpcResponse.getData();
            return TypeUtils.castMethodResult(method, data);
        } else {
            // 处理回传的调用期间发生的异常
            Exception ex = rpcResponse.getEx();
            throw new ConsumerException(ex);
        }
    }
}
