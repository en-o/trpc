package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.annotation.TConsumer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * 提供者 处理类
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 19:47
 */
public class ConsumerBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 设置包扫描路径
     */
    private String[] scanPackages;

    /**
     * 消费端存根
     */
    private Map<String, Object> stub = new HashMap<>();

    public ConsumerBootstrap(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    /**
     * 扫描到指定的注解，对其进行初始化
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        // 扫描指定路径 , true 扫描spring 注解 @Component, @Repository, @Service, and @Controller
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        // rpc的接口调用一般只会出现在这几个spring注解之下, 如何自定义注解的会加大开发复杂度
        provider.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Service.class));
        provider.addIncludeFilter(new AnnotationTypeFilter(Bean.class));
        for (String scanPackage : scanPackages) {
            Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(scanPackage);
            scanConsumerAndProxy(candidateComponents,applicationContext);
        }
    }

    /**
     * 扫描拥有注解的类并设置动态代理
     * @param candidateComponents BeanDefinition
     * @param applicationContext ApplicationContext
     */
    private void scanConsumerAndProxy(Set<BeanDefinition> candidateComponents, ApplicationContext applicationContext){
        for (BeanDefinition beanDefinition : candidateComponents){
            try {
                Object bean = getBean(applicationContext, beanDefinition);
                System.out.println("bean ===> " + bean);
                if(bean != null){
                    // 获取标注了TConsumer注解的属性字段
                    List<Field> fields =  findAnnotatedField(bean.getClass());
                    fields.forEach(field -> {
                        try {
                            // 为获取到的属性对象生成代理
                            Class<?> service = field.getType();
                            // 获取全限定名称
                            String serviceName = service.getCanonicalName();
                            // 查询缓存 - 由于 rpc service会被多个地方用到，所有这里处理以就行了，后面再用直接取
                            Object existBean = stub.get(serviceName);
                            if(existBean == null){
                                // 为属性字段查询他的实现对象 - getXXImplBean
                                existBean = createConsumer(service);
                                stub.put(serviceName, existBean);
                            }
                            // 将实现对象加载到当前属性字段里去 （filed = new XXImpl()）
                            field.setAccessible(true);
                            field.set(bean, existBean);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }




    /**
     * 查询 spring bean
     * @param applicationContext ApplicationContext
     * @param beanDefinition beanDefinition
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
                bean =  applicationContext.getBean(beanNames[0]);
            }
        }
       return bean;
    }

    /**
     * 没有Class 自己for一个
     * @param beanDefinition BeanDefinition
     * @return Class
     */
    private static Class<?> getBeanClassFromDefinition(BeanDefinition beanDefinition) {
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load bean class", e);
        }
    }


    /**
     * 为服务创建代理
     * @param service service
     * @return
     */
    private Object createConsumer(Class<?> service) {
        // 对 service进行操作时才会被触发
        return Proxy.newProxyInstance(service.getClassLoader(),
                new Class[]{service}, new TInvocationHandler(service) );
    }


    /**
     * 获取自定注解的属性字段
     * @param aClass Class
     * @return Field
     */
    private List<Field> findAnnotatedField(Class<?> aClass) {
        ArrayList<Field> result = new ArrayList<>();
        while (aClass != null){
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if(field.isAnnotationPresent(TConsumer.class)){
                    result.add(field);
                }
            }
            aClass = aClass.getSuperclass();
        }
        return result;
    }


}
