package cn.tannn.trpc.core.registry;

import cn.tannn.trpc.core.api.RegistryCenter;
import lombok.SneakyThrows;
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
public class ZkRegistryCenter implements RegistryCenter {

    private CuratorFramework client = null;


    /**
     * 初始化创建 zk连接
     */
    @Override
    public void start() {
        // 重试
        RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString("localhost:20242")
                .namespace("trpc")
                .retryPolicy(retry)
                .build();

        System.out.println(" ===> zk client started");
        client.start();

    }

    /**
     * 关闭 zk
     */
    @Override
    public void stop() {
        System.out.println(" ===> zk client stopped");
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
            System.out.println(" ===> zk register to zk: " + instancePath);
            client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath, "provider".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            System.out.println(" ===> zk unregister to zk: " + instancePath);
            client.delete().quietly().forPath(instancePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> fetchAll(String service) {
        // zk 路径以 / 分割
        String servicePath = "/" + service;
        try {
            // 获取所有子节点
            List<String> nodes = client.getChildren().forPath(servicePath);
            System.out.println(" ===> fetchAll from zk: " + servicePath);
            return nodes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public void subscribe(String service, ChangedListener listener) {
        final TreeCache cache = TreeCache
                .newBuilder(client, "/"+service)
                .setCacheData(true)
                .setMaxDepth(2)
                .build();
        cache.getListenable().addListener((curator, event) ->{
            // 节点变动，这里会感知到
            System.out.println("zk subscribe envent: " + event);
            List<String> nodes = fetchAll(service);
            listener.fire(new Event(nodes));
        });
        cache.start();
    }


}
