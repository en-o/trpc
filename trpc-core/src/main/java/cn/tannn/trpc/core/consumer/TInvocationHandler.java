package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.common.api.RpcContext;
import cn.tannn.trpc.common.api.RpcRequest;
import cn.tannn.trpc.common.api.RpcResponse;
import cn.tannn.trpc.common.exception.TrpcException;
import cn.tannn.trpc.common.meta.InstanceMeta;
import cn.tannn.trpc.common.properties.IsolateProperties;
import cn.tannn.trpc.core.consumer.http.OkHttpInvoker;
import cn.tannn.trpc.core.governance.SlidingTimeWindow;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 消费端：动态代理 实现
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/2 14:37
 */
@Slf4j
public class TInvocationHandler implements InvocationHandler {

    /**
     * 需要添加代理的对象
     */
    final Class<?> service;
    /**
     *  服务连接信息实例
     */
    final List<InstanceMeta> providers;
    /**
     * 上下文
     */
    final RpcContext rpcContext;
    final IsolateProperties isolate;
    /**
     *  http 网络协议
     */
    final HttpInvoker httpInvoker;
    /**
     * 隔离服务实例
     */
    Set<InstanceMeta> isolateProviders = new HashSet<>();
    /**
     * 故障隔离 - 故障信息记录
     */
    final Map<String, SlidingTimeWindow> windows = new HashMap<>();
    /**
     * 故障隔离恢复哨兵
     */
    ScheduledExecutorService executor;
    /**
     * 探活实例队列
     */
    final List<InstanceMeta> halfProviders = new ArrayList<>();


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
        this.isolate = rpcContext.getRpcProperties().getConsumer().getIsolate();
        this.httpInvoker = new OkHttpInvoker(rpcContext.getRpcProperties().getConsumer().getHttp());
        if(isolate.isHalfOpenEnable()){
            // 探活线程
            this.executor = Executors.newScheduledThreadPool(isolate.getCorePoolSize());
            // 60s - 探活线程 执行配置
            this.executor.scheduleWithFixedDelay(this::halfOpen, isolate.getInitialDelay(), isolate.getDelay() , TimeUnit.SECONDS);

        }
    }


    /**
     * 探活线程执行方法
     */
    private void halfOpen(){
        log.debug(" ===> half open isolateProviders {}", isolateProviders);
        halfProviders.clear();
        halfProviders.addAll(isolateProviders);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        // 组装调用参数 ： 类全限定名称，方法，参数
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setService(service.getCanonicalName());
        rpcRequest.setMethodSign( MethodUtils.methodSign(method));
        rpcRequest.setArgs(args);

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

                // 路由探活- 检查是否存在需要探活的示例，有的话就放一笔流量进行探活
                InstanceMeta instance;
                synchronized (halfProviders){
                    if(halfProviders.isEmpty()){
                        // 先通过路由筛选在进行负载均衡
                        List<InstanceMeta> instances = rpcContext.getRouter().route(this.providers);
                        // 通过负载均衡器选择路由
                        instance = rpcContext.getLoadBalancer().choose(instances);
                        log.debug("loadBalancer.choose(urls) ==> {}", instance);
                    }else {
                        // 使用当前流量探活
                        // 获取一个进行探活处理 - 获取并删除是为了保证当前流量不被污染 - 必须结合错误重试机制哦
                        instance = halfProviders.remove(0);
                        log.debug("check alive instance ==> {}", instance);
                    }
                }


                Object result;
                String callUri = instance.toUrl();
                try {
                    // 发送请求
                    RpcResponse<Object> rpcResponse = httpInvoker.post(rpcRequest, callUri);
                    // 对结果集进行处理
                    result = castReturnResult(method, rpcResponse);
                }catch (Exception e){
                    // 故障规则统计和隔离
                    // 加上 synchronized 控制并发
                    // 每一次异常，记录一次。统计30s的异常数
                    synchronized (windows) {
                        SlidingTimeWindow window = windows.computeIfAbsent(callUri, k -> new SlidingTimeWindow());
                        window.record(System.currentTimeMillis());
                        log.debug("instance {} in window with {}", callUri, window.getSum());
                        // 发生10次，就要做故障隔离
                        if(window.getSum()>=isolate.getFaultLimit()){
                            // 从路由里摘掉[隔离]
                            isolate(instance);
                        }
                    }
                    throw e;
                }

                // 故障恢复
                synchronized (providers){
                    // 当前实例不是正常实例，就表明他是探活实例，且探活成功就将节点恢复正常
                    if(!providers.contains(instance)){
                        isolateProviders.remove(instance);
                        providers.add(instance);
                        log.debug("instance {} is recovered, isolateProviders = {}, provides = {}",
                                instance, isolateProviders, providers);
                    }
                }

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

    /**
     * 把故障节点隔离掉
     * @param instance 故障节点
     */
    private void isolate(InstanceMeta instance) {
        log.debug(" ===> isolate instanc {}", instance);
        providers.remove(instance);
        log.debug(" ===> providers  = {} ", providers);
        isolateProviders.add(instance);
        log.debug(" ===> isolateProviders  = {} ", isolateProviders);
    }
}
