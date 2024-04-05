package cn.tannn.trpc.core.cluster;

import cn.tannn.trpc.core.api.Router;
import cn.tannn.trpc.core.meta.InstanceMeta;
import cn.tannn.trpc.core.properties.ConsumerProperties;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 灰度路由
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/5 下午12:25
 */
@Slf4j
public class GrayRouter implements Router {

    private ConsumerProperties consumerProperties;

    SecureRandom random = new SecureRandom();

    public GrayRouter(ConsumerProperties consumerProperties) {
        this.consumerProperties = consumerProperties;
    }

    @Override
    public List<InstanceMeta> route(List<InstanceMeta> providers) {

        // 只有一个节点就不做处理，直接返回
        if (providers == null || providers.size() <= 1) {
            log.debug(" =====> 节点只有一个");
            return providers;
        }

        // 正常节点
        List<InstanceMeta> normalNodes = new ArrayList<>();
        // 灰度节点
        List<InstanceMeta> grayNodes = new ArrayList<>();

        // 节点分类
        providers.forEach(p -> {
            if (p.getGray().isGray()) {
                grayNodes.add(p);
            } else {
                normalNodes.add(p);
            }
        });

        log.debug(" grayRouter grayNodes/normalNodes,grayRatio ===> {}/{},{}",
                grayNodes.size(), normalNodes.size(), consumerProperties.getGrayRatio());


        // 节点容错处理， 都是空，表示是其他类型，则直接返回
        if (normalNodes.isEmpty() || grayNodes.isEmpty()) {
            return providers;
        }
        // 灰度调拨百分比上下限处理
        if (consumerProperties.getGrayRatio() <= 0) {
            // 流量调调拨小于等与0则 不处理灰度节点
            return normalNodes;
        } else if (consumerProperties.getGrayRatio() >= 100) {
            // 流量调调全走灰度，那就直接 返回灰度节点
            return grayNodes;
        }

        // 灰度算法 -  构造虚拟节点,根据灰度比例构造一个均匀分布的大集合 （不常用

        // 灰度算法 [常规做法] - 随机数决定灰度比例，随机数很均衡
        if(random.nextInt(100) < consumerProperties.getGrayRatio()){
            log.debug(" grayRouter grayNodes ===> {}", grayNodes);
            return grayNodes;
        }else {
            log.debug(" grayRouter normalNodes ===> {}", normalNodes);
            return normalNodes;
        }

    }
}
