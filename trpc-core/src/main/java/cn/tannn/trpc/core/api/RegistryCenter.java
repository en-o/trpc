package cn.tannn.trpc.core.api;

import java.util.List;

/**
 * 注册中心
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 20:40
 */
public interface RegistryCenter {
    void start();
    void stop();


    // provider 侧
    /**
     * 注册
     * @param service 服务
     * @param instance 实例
     */
    void register(String service, String instance);

    /**
     * 注销
     * @param service 服务
     * @param instance 实例
     */
    void unregister(String service, String instance);


    // consumer侧

    /**
     * 获取服务的  instance
     * @return instance
     */
    List<String> fetchAll(String service);

    /**
     * 订阅服务 instance 的变化，通知消费者刷新列表
     */
    void subscribe();


    /**
     * 静态注册中心
     */
    class StaticRegistryCenter implements RegistryCenter {

        List<String> providers;

        public StaticRegistryCenter(List<String> providers) {
            this.providers = providers;
        }

        @Override
        public void start() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void register(String service, String instance) {

        }

        @Override
        public void unregister(String service, String instance) {

        }

        @Override
        public List<String> fetchAll(String service) {
            return providers;
        }

        @Override
        public void subscribe() {

        }
    }


}
