package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.LoadBalancer;
import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.api.Router;
import cn.tannn.trpc.core.api.RpcContext;
import cn.tannn.trpc.core.exception.ConsumerException;
import cn.tannn.trpc.core.util.MethodUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消费者处理器
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 19:47
 */
@Slf4j
public class ConsumerBootstrap implements ApplicationContextAware {

    /**
     * 设置包扫描路径
     */
    private final String[] scanPackages;


    /**
     * spring 上下文
     */
    private ApplicationContext context;

    /**
     * 消费端存根 - 用户代理创建时不重复创建直接复用
     */
    private Map<String, Object> stub = new HashMap<>();


    public ConsumerBootstrap(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }


    /**
     * applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * init  init : 拿到 所有标记了TConsumer注解的类（所有的提供者接口元数据），并设置代理
     * <pr>为了包装所有实例都已经加载完成，在 使用 runner 主动调用，确保实例都加载完成</pr>
     */
    public void start() {
        log.info("consumerBootstrap starting ...");

        // 扫描指定路径 , true 扫描spring 注解 @Component, @Repository, @Service, and @Controller
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        // rpc的接口调用一般只会出现在这几个spring注解之下, 如何自定义注解的会加大开发复杂度
        provider.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Service.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Bean.class));
        for (String scanPackage : scanPackages) {
            Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(scanPackage);
            scanConsumerAndProxy(candidateComponents);
        }

        log.info("consumerBootstrap started ...");
    }

    /**
     * 扫描拥有注解的类并设置动态代理
     *
     * @param candidateComponents BeanDefinition
     */
    private void scanConsumerAndProxy(Set<BeanDefinition> candidateComponents) {

        LoadBalancer loadBalancer = context.getBean(LoadBalancer.class);
        Router router = context.getBean(Router.class);
        RegistryCenter registryCenter = context.getBean(RegistryCenter.class);

        RpcContext<String> rpcContext = new RpcContext<>();
        rpcContext.setLoadBalancer(loadBalancer);
        rpcContext.setRouter(router);

        for (BeanDefinition beanDefinition : candidateComponents) {
            try {
                Object bean = getBean(context, beanDefinition);
                if (bean != null) {
                    // 获取标注了TConsumer注解的属性字段
                    List<Field> fields = MethodUtils.findAnnotatedField(bean.getClass());
                    fields.forEach(field -> {
                        log.info(" ===> " + field.getName());
                        try {
                            // 为获取到的属性对象生成代理
                            Class<?> service = field.getType();
                            // 获取全限定名称
                            String serviceName = service.getCanonicalName();
                            // 查询缓存 - 由于 rpc service会被多个地方用到，所有这里处理以就行了，后面再用直接取
                            Object consumer = stub.get(serviceName);
                            if (consumer == null) {
                                // 为属性字段查询他的实现对象 - getXXImplBean
                                consumer = createFromRegistry(service, rpcContext, registryCenter);
                                stub.put(serviceName, consumer);
                            }
                            // 将实现对象加载到当前属性字段里去 （filed = new XXImpl()）
                            field.setAccessible(true);
                            field.set(bean, consumer);
                        } catch (IllegalAccessException e) {
                            throw new ConsumerException(e);
                        }
                    });
                }
            } catch (Exception e) {
                throw new ConsumerException(e);
            }
        }
    }

    /**
     * 通过注册中心创建代理
     *
     * @param service        service
     * @param rpcContext     上下文
     * @param registryCenter 注册中心
     * @return 代理类
     */
    private Object createFromRegistry(Class<?> service, RpcContext rpcContext, RegistryCenter registryCenter) {
        String serviceName = service.getCanonicalName();
        // 由于此处只会在启动时处理一次，所以需要下面的订阅，订阅服务后，当数据发生了变动会重新执行
        List<String> providers = mapUrl(registryCenter.fetchAll(serviceName));
        log.info("===> map to provider: ");
        if (log.isDebugEnabled()) {
            providers.forEach(log::debug);
        }
        // 订阅服务，感知服务变更
        registryCenter.subscribe(serviceName, event -> {
            providers.clear();
            providers.addAll(mapUrl(event.getData()));
        });

        return createConsumer(service, rpcContext, providers);
    }


    /**
     * 查询 spring bean
     *
     * @param applicationContext ApplicationContext
     * @param beanDefinition     beanDefinition
     * @return 扫描出来的spring bean
     */
    private static Object getBean(ApplicationContext applicationContext, BeanDefinition beanDefinition) {
        Object bean = null;
        String beanName = beanDefinition.getBeanClassName();
        if (applicationContext.containsBean(beanName)) {
            // 如果该名称在上下文中已注册,则使用该名称获取实例
            bean = applicationContext.getBean(beanName);
        } else {
            // 否则,使用 getBeanNamesForType 获取该类型的所有 Bean 名称
            String[] beanNames = applicationContext
                    .getBeanNamesForType(getBeanClassFromDefinition(beanDefinition));
            if (beanNames.length > 0) {
                // 如果存在该类型的 Bean,则使用第一个名称获取实例
                bean = applicationContext.getBean(beanNames[0]);
            }
        }
        return bean;
    }

    /**
     * getBean时没有Class 自己for一个
     *
     * @param beanDefinition BeanDefinition
     * @return Class
     */
    private static Class<?> getBeanClassFromDefinition(BeanDefinition beanDefinition) {
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new ConsumerException("Cannot load bean class", e);
        }
    }

    /**
     * 为服务创建代理
     *
     * @param service service
     * @return 代理类
     */
    private Object createConsumer(Class<?> service
            , RpcContext rpcContext, List<String> providers) {
        // 对 service进行操作时才会被触发
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new TInvocationHandler(service, rpcContext, providers));
    }




    /**
     * 处理注册中心中的
     *
     * @param nodes 注册中心里的所有节点
     * @return 处理成标准http地址
     */
    private List<String> mapUrl(List<String> nodes) {
        return nodes.stream().map(x -> "http://" + x.replace("_", ":")).collect(Collectors.toList());
    }

}
