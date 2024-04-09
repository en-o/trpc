package cn.tannn.trpc.registry.nacos;

import cn.tannn.trpc.common.api.RegistryCenter;
import cn.tannn.trpc.common.listener.ChangedListener;
import cn.tannn.trpc.common.meta.InstanceMeta;
import cn.tannn.trpc.common.meta.ServiceMeta;

import java.util.List;

/**
 * nacos注册中心
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/9 15:50
 */
public class NacosRegistryCenter implements RegistryCenter {
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
        return null;
    }

    @Override
    public void subscribe(ServiceMeta service, ChangedListener changedListener) {

    }
}
