package cn.tannn.trpc.core.util;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.core.consumer.TInvocationHandler;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.core.meta.InstanceMeta;
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


    /**
     * 动态代理
     *
     * @param bean      需要代理的可能对象,是对其内容属性字段检测是否需要代理
     * @param providers 服务提供者元信息
     */
    public static void rpcApiProxy(Object bean, List<InstanceMeta> providers) {
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
                    consumer = createConsumer(service, providers);
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
     * 创建代理
     *
     * @param service   需要代理的服务
     * @param providers 服务提供者的连接信息
     * @return 代理类
     */
    private static Object createConsumer(Class<?> service
            , List<InstanceMeta> providers) {
        // 对 service进行操作时才会被触发
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new TInvocationHandler(service, providers));
    }

}
