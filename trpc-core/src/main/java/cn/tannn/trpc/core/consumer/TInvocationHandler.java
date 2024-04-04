package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.RpcContext;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.consumer.http.OkHttpInvoker;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
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
     *  服务连接信息实例
     */
    final List<InstanceMeta> providers;

    /**
     *  http 网络协议
     */
    final HttpInvoker httpInvoker;

    /**
     * 上下文
     */
    final RpcContext rpcContext;

    /**
     * 需要添加代理的对象
     */
    final Class<?> service;

    /**
     * @param service     需要添加代理的对象
     * @param providers   服务提供者连接信息
     * @param rpcContext  上下文
     */
    public TInvocationHandler(Class<?> service
            , List<InstanceMeta> providers
            , RpcContext rpcContext) {
        this.service = service;
        this.providers = providers;
        this.rpcContext = rpcContext;
        this.httpInvoker = new OkHttpInvoker(rpcContext.getRpcProperties().getConsumer().getHttp());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        // 组装调用参数 ： 类全限定名称，方法，参数
        RpcRequest rpcRequest = new RpcRequest(service.getCanonicalName(), MethodUtils.methodSign(method), args);

        // 超时重试
        Integer retries = rpcContext.getRpcProperties().getConsumer().getHttp().getRetries();
        while (retries -- > 0 ){
            log.debug("====> retries = {}", retries);
            // 重试整个请求链
            try {
                // 对请求进行前置处理 todo 这里并发好像有点问题
                Object prefilter = rpcContext.getFilters().executePref(rpcRequest);
                if (prefilter != null) {
                    return prefilter;
                }
                // 通过负载均衡器选择路由
                InstanceMeta instance = rpcContext.getLoadBalancer().choose(this.providers);
                log.debug("loadBalancer.choose(urls) ==> {}", instance);

                // 发送请求
                RpcResponse<Object> rpcResponse = httpInvoker.post(rpcRequest, instance.toUrl());
                // 对结果集进行处理
                Object result = castReturnResult(method, rpcResponse);
                // 对结果集进行过滤器后置处理,比如利用cache缓存结果集,下次请求就在前置拦截里发现了直接返回减少IO
                Object filterResult = rpcContext.getFilters().executePost(rpcRequest, result);
                // 后置处理对结果集处理之后就直接返回,为空就是没处理那就直接返回原生的
                if (filterResult != null) {
                    return filterResult;
                }
                return result;
            }catch (RuntimeException e){
                // 不是超时的异常就中断循环
                if (!(e.getCause() instanceof SocketTimeoutException)) {
                    throw e;
                }else {
                    log.error("{}出现了超时，进行超时重试",rpcRequest);
                }
            }

        }
       return null;

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
            // 参数类型转换  - 因为序列化过程中数据类型丢失了
            return TypeUtils.castMethodResult(method, data);
        } else {
            TrpcException ex = rpcResponse.getEx();
            if (null != ex) {
                throw ex;
            }
            return null;
        }
    }
}
