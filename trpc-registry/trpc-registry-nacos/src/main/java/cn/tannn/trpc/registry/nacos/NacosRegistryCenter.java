package cn.tannn.trpc.registry.nacos;

import cn.tannn.trpc.common.api.RegistryCenter;
import cn.tannn.trpc.common.exception.ExceptionCode;
import cn.tannn.trpc.common.exception.TrpcException;
import cn.tannn.trpc.common.listener.ChangedListener;
import cn.tannn.trpc.common.listener.Event;
import cn.tannn.trpc.common.meta.InstanceMeta;
import cn.tannn.trpc.common.meta.ServiceMeta;
import cn.tannn.trpc.common.properties.meta.GrayMetas;
import cn.tannn.trpc.common.properties.rc.Connect;
import cn.tannn.trpc.common.properties.rc.RegistryCenterProperties;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.utils.MapUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * nacos注册中心
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/9 15:50
 */
@Slf4j
public class NacosRegistryCenter implements RegistryCenter {

    private final RegistryCenterProperties rcp;
    private NamingService client;

    public NacosRegistryCenter(RegistryCenterProperties rcp) {
        this.rcp = rcp;
    }

    @SneakyThrows
    @Override
    public void start() {
        if (client != null) {
            log.info("nacosServer alive ... ");
            return;
        }
        Connect[] connect = rcp.getConnect();
        // @see com.alibaba.nacos.api.PropertyKeyConst
        Properties properties = new Properties();
        if (connect == null || connect.length == 0) {
            // "请填写注册中心连接信息"
            throw new TrpcException(ExceptionCode.ZK_NOT_SETTING);
        } else {
            // todo 注册中心也可以设置多个, 目前只拿第一个
            String serverList = Arrays.stream(connect).map(Connect::connectString).collect(Collectors.joining(","));
            properties.setProperty("serverAddr", serverList);
        }
        log.info("nacosServer start ...");
        properties.setProperty("serverName", rcp.getNamespace());
        client = NamingFactory.createNamingService(properties);
        log.info(" ===> nacos client starting.");
    }

    @SneakyThrows
    @Override
    public void stop() {
        client.shutDown();
    }

    @SneakyThrows
    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        Instance nacosInstance = new Instance();
        nacosInstance.setServiceName(service.toPath());
        nacosInstance.setIp(instance.getHost());
        nacosInstance.setPort(instance.getPort());
        nacosInstance.setClusterName(instance.getContext());
        nacosInstance.setMetadata(JSON.to(Map.class, JSON.toJSON(instance.getGray())));
        nacosInstance.setHealthy(true);
        client.registerInstance(service.toMetas(), nacosInstance);
        log.info(" ===> register to nacos: {}", instance);
    }

    @SneakyThrows
    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {
        client.deregisterInstance(service.toPath(),
                instance.getHost(),
                instance.getPort(),
                instance.getContext());
        log.info(" ===> unregister from nacos: {}", instance);
    }

    @SneakyThrows
    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        List<Instance> nodes = client.selectInstances(service.toPath(), true);
        return nodes.stream().map(instance -> {
            InstanceMeta meta = InstanceMeta.http(
                    instance.getIp(), instance.getPort(), instance.getClusterName());
            GrayMetas grayMetas = JSON.to(GrayMetas.class, instance.getMetadata());
            meta.setGray(grayMetas);
            log.debug(" fetchAll instance: {}", meta.toUrl());
            return meta;
        }).collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public void subscribe(ServiceMeta service, ChangedListener changedListener) {

        client.subscribe(service.toPath(), event -> {
            // 节点变动，这里会感知到
            log.info("nacos subscribe event: {}", event);
            List<InstanceMeta> nodes = fetchAll(service);
            changedListener.fire(new Event(nodes));
        });

    }
}
