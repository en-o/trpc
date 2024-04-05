package cn.tannn.trpc.core.properties;

import lombok.Data;

/**
 * 故障隔离
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/5 下午1:25
 */
@Data
public class IsolateProperties {


    /**
     * 故障允许错误的次数， 默认10，[错误次数超过设置值就会别隔离]
     */
    private Integer faultLimit = 10;

    /**
     *  探活线程 - 是否启用
     */
    private boolean halfOpenEnable = true;

    /**
     * 探活线程 - 个数 默认1
     */
    private Integer corePoolSize = 1;

    /**
     * 探活线程 - 第一次执行的延迟时间 / 秒
     */
    private Long initialDelay = 10L;

    /**
     * 探活线程 - 除开第一次之后的延迟时间 / 秒
     */
    private Long delay = 30L;

    /**
     * 设置故障隔离阈值默认值
     * @return Integer
     */
    public Integer getFaultLimit() {
        if(faultLimit == null || faultLimit <=0){
            return 10;
        }
        return faultLimit;
    }
}
