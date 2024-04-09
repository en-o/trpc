package cn.tannn.trpc.common.api;

import cn.tannn.trpc.common.listener.ChangedListener;
import cn.tannn.trpc.common.meta.InstanceMeta;
import cn.tannn.trpc.common.meta.ServiceMeta;

import java.util.List;


/**
 * 注册中心
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/4 下午4:37
 */
public interface RegistryCenter {

    /**
     * 启动注册
     */
    void start();

    /**
     * 销毁注销
     */
    void stop();


    // provider 侧
    /**
     * 注册
     * @param service 服务
     * @param instance 实例
     */
    void register(ServiceMeta service, InstanceMeta instance);

    /**
     * 注销
     * @param service 服务
     * @param instance 实例
     */
    void unregister(ServiceMeta service, InstanceMeta instance);


    // consumer侧

    /**
     * 获取服务的  instance
     * @param service ServiceMeta
     * @return instance
     */
    List<InstanceMeta> fetchAll(ServiceMeta service);

    /**
     * 订阅服务 instance 的变化，通知消费者刷新列表
     * @param service 服务
     * @param changedListener 监听
     */
    void subscribe(ServiceMeta service, ChangedListener changedListener);



    /**
     * 静态注册中心
     */
    class StaticRegistryCenter implements RegistryCenter {


        List<InstanceMeta> providers;

        public StaticRegistryCenter(List<InstanceMeta> providers) {
            this.providers = providers;
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void register(ServiceMeta service, InstanceMeta instance) {

        }

        @Override
        public void unregister(ServiceMeta service, InstanceMeta instance) {

        }

        @Override
        public List<InstanceMeta> fetchAll(ServiceMeta service) {
                return providers;
        }

        @Override
        public void subscribe(ServiceMeta service, ChangedListener changedListener) {

        }
    }
}
