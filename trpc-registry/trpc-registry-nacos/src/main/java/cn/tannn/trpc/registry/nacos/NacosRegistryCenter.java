package cn.tannn.trpc.registry.nacos;

import cn.tannn.trpc.common.api.RegistryCenter;
import cn.tannn.trpc.common.exception.TrpcException;
import cn.tannn.trpc.common.listener.ChangedListener;
import cn.tannn.trpc.common.meta.InstanceMeta;
import cn.tannn.trpc.common.meta.ServiceMeta;
import cn.tannn.trpc.common.properties.rc.RegistryCenterProperties;
import com.alibaba.fastjson2.JSON;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.MapUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

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

    @SneakyThrows
    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        NamingService namingService = NamingFactory.createNamingService(service.toPath());
        Instance nacosInstance = new Instance();
        nacosInstance.setIp(instance.getHost());
        nacosInstance.setPort(instance.getPort());
        nacosInstance.setMetadata(JSON.to(Map.class, instance.getGray()));
        namingService.registerInstance(service.toMetas(), nacosInstance);
    }

    @SneakyThrows
    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {
        NamingService namingService = NamingFactory.createNamingService(service.toPath());
        namingService.deregisterInstance(service.toMetas(), instance.getHost(), instance.getPort());
    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        return null;
    }

    @Override
    public void subscribe(ServiceMeta service, ChangedListener changedListener) {

    }
}
