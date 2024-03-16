package cn.tannn.trpc.core.registry;

import cn.tannn.trpc.core.api.RegistryCenter;

import java.util.List;

/**
 * zk注册中心
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 21:00
 */
public class ZkRegistryCenter implements RegistryCenter {
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
        return null;
    }

    @Override
    public void subscribe() {

    }
}
