package cn.tannn.trpc.core.util;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.core.api.RpcContext;
import cn.tannn.trpc.core.properties.ServiceProperties;
import cn.tannn.trpc.core.consumer.TInvocationHandler;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.meta.ServiceMeta;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.tannn.trpc.core.exception.ExceptionCode.PROXY_CREATE_EX;

/**
 * 代理工具类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-23 10:29
 */
@Slf4j
public class ProxyUtils {
    /**
     * 消费端存根 - 用户代理创建时不重复创建直接复用
     */
    private static Map<String, Object> stub = new HashMap<>();

    public static void rpcApiProxy(Object bean, RpcContext rpcContext){
        if (bean == null) {
            return;
        }
        // 获取标注了TConsumer注解的属性字段
        List<Field> fields = MethodUtils.findAnnotatedField(bean.getClass(), TConsumer.class);
        fields.forEach(field -> {
            log.info(" ===> {}", field.getName());
            try {
                // 为获取到的属性对象生成代理
                Class<?> service = field.getType();
                // 获取全限定名称
                String serviceName = service.getCanonicalName();
                // 查询缓存 - 由于 rpc service会被多个地方用到，所有这里处理以就行了，后面再用直接取
                Object consumer = stub.get(serviceName);
                if (consumer == null) {
                    // 为属性字段查询他的实现对象 - getXXImplBean
                    consumer = createFromRegistry(service, rpcContext);
                    stub.put(serviceName, consumer);
                }
                // 将实现对象加载到当前属性字段里去 （filed = new XXImpl()）
                field.setAccessible(true);
                field.set(bean, consumer);
            } catch (IllegalAccessException e) {
                throw new TrpcException(e, PROXY_CREATE_EX);
            }
        });
    }

    /**
     * 订阅注册中心 - 感知服务的上下线
     *
     * @param service        service
     * @param rpcContext     上下文
     * @return 代理类
     */
    private static Object createFromRegistry(Class<?> service,
                                            RpcContext rpcContext) {
        String serviceName = service.getCanonicalName();
        ServiceProperties serviceProperties = rpcContext.getRpcProperties().getConsumer().getService();
        ServiceMeta meta = new ServiceMeta(serviceName);
        meta.setAppid(serviceProperties.getAppid());
        meta.setNamespace(serviceProperties.getNamespace());
        meta.setEnv(serviceProperties.getEnv());
        meta.setVersion(serviceProperties.getVersion());
        // 由于此处只会在启动时处理一次，所以需要下面的订阅，订阅服务后，当数据发生了变动会重新执行
        List<InstanceMeta> providers = rpcContext.getRegistryCenter().fetchAll(meta);
        log.info("===> map to provider: ");
        // 订阅服务，感知服务变更
        rpcContext.getRegistryCenter().subscribe(meta, event -> {
            providers.clear();
            providers.addAll(event.getData());
        });
        return createConsumer(service, rpcContext, providers);
    }


    /**
     *  创建代理
     *
     * @param service service
     * @return 代理类
     */
    private static Object createConsumer(Class<?> service
            , RpcContext rpcContext, List<InstanceMeta> providers) {
        // 对 service进行操作时才会被触发
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new TInvocationHandler(service, rpcContext, providers));
    }

}
