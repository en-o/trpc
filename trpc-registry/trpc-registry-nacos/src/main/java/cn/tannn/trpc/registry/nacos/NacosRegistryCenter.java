package cn.tannn.trpc.registry.nacos;

import cn.tannn.trpc.common.api.RegistryCenter;
import cn.tannn.trpc.common.exception.TrpcException;
import cn.tannn.trpc.common.listener.ChangedListener;
import cn.tannn.trpc.common.meta.InstanceMeta;
import cn.tannn.trpc.common.meta.ServiceMeta;
import cn.tannn.trpc.common.properties.rc.RegistryCenterProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * nacos注册中心
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/9 15:50
 */
@Slf4j
public class NacosRegistryCenter implements RegistryCenter {

    private final RegistryCenterProperties rcp;

    public NacosRegistryCenter(RegistryCenterProperties rcp) {
        this.rcp = rcp;
        throw new TrpcException("暂不支持nacos");
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
        return null;
    }

    @Override
    public void subscribe(ServiceMeta service, ChangedListener changedListener) {

    }
}
