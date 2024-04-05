package cn.tannn.trpc.core.properties.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务配置灰度信息
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/5 下午12:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrayMetas {
    /**
     * 地区
     */
    String dc = "cq";
    /**
     * 是否灰度
     */
    boolean gray = false;

    /**
     * 机房
     */
    String unit ="A000";
}
