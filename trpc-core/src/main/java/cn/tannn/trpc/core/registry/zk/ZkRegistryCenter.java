package cn.tannn.trpc.core.registry.zk;

import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.meta.ServiceMeta;
import cn.tannn.trpc.core.properties.rc.Connect;
import cn.tannn.trpc.core.properties.rc.RegistryCenterProperties;
import cn.tannn.trpc.core.registry.ChangedListener;
import cn.tannn.trpc.core.registry.Event;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.stream.Collectors;

import static cn.tannn.trpc.core.exception.ExceptionCode.*;

/**
 * zk注册中心
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/4 下午4:42
 */
@Slf4j
public class ZkRegistryCenter implements RegistryCenter {

    private CuratorFramework client = null;

    private final RegistryCenterProperties rcp;
    TreeCache cacheGlobal = null;

    public ZkRegistryCenter(RegistryCenterProperties rcp) {
        this.rcp = rcp;
    }


    @Override
    public void start() {
        if (client != null) {
            log.info("zkServer alive ... ");
            return;
        }
        log.info("zkServer start ...");
        // 重试
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);

        Connect[] connect = rcp.getConnect();
        String connectString;
        if (connect == null || connect.length == 0) {
            // "请填写注册中心连接信息"
            throw new TrpcException(ZK_NOT_SETTING);
        } else {
            // todo 注册中心也可以设置多个, 目前只拿第一个
            connectString = connect[0].connectString();
        }
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .namespace(rcp.getNamespace())
                .retryPolicy(retry)
                .build();

        log.info(" ===> zk client starting.");
        client.start();
    }

    @Override
    public void stop() {
        if (cacheGlobal != null) {
            cacheGlobal.close();
        }
        client.close();
        log.info(" ===> zk client stopped.");
    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {
        // zk 路径以 / 分割
        String servicePath = "/" + service.toPath();
        try {
            // 创建服务的持久化节点
            // 检查service是否存在
            if (client.checkExists().forPath(servicePath) == null) {
                // 创建 [PERSISTENT:持久化]
                client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath, "service.toMetas()".getBytes());
            }
            // 创建实例的临时节点
            String instancePath = servicePath + "/" + instance.toPath();
            log.info(" ===> register to zk: {}", instancePath);
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath, "instance.toMetas()".getBytes());
        } catch (Exception e) {
            throw new TrpcException(e, ZK_REGISTER_FAIL);
        }
    }

    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {
        // zk 路径以 / 分割
        String servicePath = "/" + service.toPath();
        try {
            // 判断服务是否存在
            if (client.checkExists().forPath(servicePath) == null) {
                return;
            }
            // 删除实例节点
            String instancePath = servicePath + "/" + instance.toPath();
            log.info(" ===> unregister from zk: {}", instancePath);
            client.delete().quietly().forPath(instancePath);
        } catch (Exception e) {
            throw new TrpcException(e, ZK_UNREGISTER_FAIL);
        }
    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
        // zk 路径以 / 分割
        String servicePath = "/" + service.toPath();
        try {
            // 获取所有子节点
            List<String> nodes = client.getChildren().forPath(servicePath);
            log.info(" ===> fetchAll from zk: {}", servicePath);
            return mapInstances(nodes);
        } catch (Exception e) {
            throw new TrpcException(e, ZK_FETCH_INSTANCE_FAIL);
        }
    }

    @SneakyThrows
    @Override
    public void subscribe(ServiceMeta service, ChangedListener listener) {
        // 通过 client参数 感知服务的上下线
        final TreeCache cache = TreeCache
                .newBuilder(client, "/" + service.toPath())
                .setCacheData(true)
                .setMaxDepth(2)
                .build();
        cache.getListenable().addListener((curator, event) -> {
            // 节点变动，这里会感知到
            log.info("zk subscribe event: {}", event);
            List<InstanceMeta> nodes = fetchAll(service);
            listener.fire(new Event(nodes));
        });
        cache.start();
        cacheGlobal = cache;
    }



    /**
     * 节点
     * @param nodes 节点
     * @return 节点实例 {zk str ->  InstanceMeta}
     */
    private List<InstanceMeta> mapInstances(List<String> nodes) {
        return nodes.stream().map(x -> {
            String[] split = x.split("_");
            InstanceMeta instance = InstanceMeta.http(split[0], Integer.valueOf(split[1]), split[2]);
            log.debug(" fetchAll instance: {}" , instance.toUrl());
            return instance;
        }).collect(Collectors.toList());
    }
}
