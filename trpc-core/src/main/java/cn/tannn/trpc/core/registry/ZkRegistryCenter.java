package cn.tannn.trpc.core.registry;

import cn.tannn.trpc.core.api.RegistryCenter;
import cn.tannn.trpc.core.config.registry.Connect;
import cn.tannn.trpc.core.config.registry.RegistryCenterProperties;
import cn.tannn.trpc.core.exception.ConsumerException;
import cn.tannn.trpc.core.exception.ProviderException;
import cn.tannn.trpc.core.exception.RegistryCenterException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * zk注册中心
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 21:00
 */
@Slf4j
public class ZkRegistryCenter implements RegistryCenter {

    private CuratorFramework client = null;

    private final RegistryCenterProperties rcp;

    public ZkRegistryCenter(RegistryCenterProperties rcp) {
        this.rcp = rcp;
    }


    /**
     * 初始化创建 zk连接
     */
    @Override
    public void start() {
        log.info("zkServer start: ");
        // 重试
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);

        Connect[] connect = rcp.getConnect();
        String connectString = "";
        if(connect == null || connect.length == 0){
            throw new RegistryCenterException("请填写注册中心连接信息");
        }else {
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

    /**
     * 关闭 zk
     */
    @Override
    public void stop() {
        log.info(" ===> zk client stopped.");
        client.close();
    }

    /**
     * 将服务和实例注册到zk
     *
     * @param service  服务
     * @param instance 实例
     */
    @Override
    public void register(String service, String instance) {
        // zk 路径以 / 分割
        String servicePath = "/" + service;
        try {
            // 创建服务的持久化节点
            // 检查service是否存在
            if (client.checkExists().forPath(servicePath) == null) {
                // 创建 [PERSISTENT:持久化]
                client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath, "service".getBytes());
            }
            // 创建实例的临时节点
            String instancePath = servicePath + "/" + instance;
            log.info(" ===> register to zk: " + instancePath);
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath, "provider".getBytes());
        } catch (Exception e) {
            throw new ProviderException(e);
        }
    }

    /**
     * 注销注册的服务和实例
     *
     * @param service  服务
     * @param instance 实例
     */
    @Override
    public void unregister(String service, String instance) {

        // zk 路径以 / 分割
        String servicePath = "/" + service;
        try {
            // 判断服务是否存在
            if (client.checkExists().forPath(servicePath) == null) {
               return;
            }
            // 删除实例节点
            String instancePath = servicePath + "/" + instance;
            log.info(" ===> unregister from zk: " + instancePath);
            client.delete().quietly().forPath(instancePath);
        } catch (Exception e) {
            throw new ProviderException(e);
        }
    }

    @Override
    public List<String> fetchAll(String service) {
        // zk 路径以 / 分割
        String servicePath = "/" + service;
        try {
            // 获取所有子节点
            List<String> nodes = client.getChildren().forPath(servicePath);
            log.info(" ===> fetchAll from zk: " + servicePath);
            return nodes;
        } catch (Exception e) {
            throw new ConsumerException(e);
        }
    }

    @SneakyThrows
    @Override
    public void subscribe(String service, ChangedListener listener) {
        // 通过 client参数 感知服务的上下线
        final TreeCache cache = TreeCache
                .newBuilder(client, "/"+service)
                .setCacheData(true)
                .setMaxDepth(2)
                .build();
        cache.getListenable().addListener((curator, event) ->{
            // 节点变动，这里会感知到
            log.info("zk subscribe event: " + event);
            List<String> nodes = fetchAll(service);
            listener.fire(new Event(nodes));
        });
        cache.start();
    }


}
