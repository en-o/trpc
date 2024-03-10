package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.annotation.TConsumer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供者 处理类
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 19:47
 */
public class ConsumerBootstrap implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 消费端存根
     */
    private Map<String, Object> stub = new HashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 扫描到指定的注解，对其进行初始化
     */
    public void start(){
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            // 获取标注了TConsumer注解的属性字段
            List<Field> fields =  findAnnotatedField(bean.getClass());
            fields.forEach(field -> {
                System.out.println("===>" + field.getName());
                try {
                    // 为获取到的属性对象生成代理
                    Class<?> service = field.getType();
                    // 获取全限定名称
                    String serviceName = service.getCanonicalName();
                    // 查询缓存
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
     * @param aClass Class<
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
