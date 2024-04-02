package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.consumer.http.OkHttpInvoker;
import cn.tannn.trpc.core.exception.ExceptionCode;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.properties.HttpProperties;
import cn.tannn.trpc.core.util.MethodUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.List;

/**
 * 消费端：动态代理 实现
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/2 14:37
 */
@Slf4j
public class TInvocationHandler implements InvocationHandler {

    /**
     * 服务提供者连接信息
     */
    final List<InstanceMeta> providers;

    /**
     *  http 网络协议
     */
    final HttpInvoker httpInvoker;

    /**
     * 需要添加代理的对象
     */
    final Class<?> service;

    SecureRandom random = new SecureRandom();

    /**
     * @param service     需要添加代理的对象
     * @param providers   服务提供者连接信息
     */
    public TInvocationHandler(Class<?> service, List<InstanceMeta> providers) {
        this.providers = providers;
        this.service = service;
        this.httpInvoker = new OkHttpInvoker(new HttpProperties(1000));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 组装调用参数 ： 类全限定名称，方法，参数
        RpcRequest rpcRequest = new RpcRequest(service.getCanonicalName(), MethodUtils.methodSign(method), args);
        // 屏蔽 toString / equals 等 Object 的一些基本方法
        if (MethodUtils.checkLocalMethodSign(MethodUtils.methodSign(method))) {
            throw new TrpcException(ExceptionCode.ILLEGALITY_METHOD_EX);
        }
        // 随机获取请求地址
        InstanceMeta instance = providers.get(random.nextInt(providers.size()));
        // 发送请求
        RpcResponse<Object> rpcResponse = httpInvoker.post(rpcRequest, instance.toUrl());
        return castReturnResult(method, rpcResponse);
    }



    /**
     * 处理RpcResponse格式
     *
     * @param method      Method
     * @param rpcResponse RpcResponse
     * @return Object
     */
    private static @Nullable Object castReturnResult(Method method, RpcResponse<Object> rpcResponse) {
        if (rpcResponse.isStatus()) {
            Object data = rpcResponse.getData();
            // TODO 需要对类型进行处理 - 1
            return JSON.to(method.getReturnType(),data);
        } else {
            Exception ex = rpcResponse.getEx();
            if (ex instanceof TrpcException trpcException) {
                throw trpcException;
            }
            // 处理回传的调用期间发生的异常
            throw new TrpcException(rpcResponse.getEx(), ExceptionCode.UNKNOWN_EX);
        }
    }
}
