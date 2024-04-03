package cn.tannn.trpc.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 负载均衡算法枚举
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/3 下午10:12
 */
@AllArgsConstructor
@Getter
public enum LoadBalancerEnum {

    /**
     * 随机 [默认]
     */
    RANDOM,

    /**
     * 轮询
     */
    ROUND_RIBBON


}
