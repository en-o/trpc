package cn.tannn.trpc.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 负载均衡算法
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/19 16:37
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
