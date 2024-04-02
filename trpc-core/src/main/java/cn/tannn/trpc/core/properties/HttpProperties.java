package cn.tannn.trpc.core.properties;

import lombok.*;

/**
 * http参数
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/3/27 下午9:21
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HttpProperties {

    /**
     * http[r,w,c]超时时间/毫秒 ，默认1s
     */
    Integer timeout = 1000;


    public Integer getTimeout() {
        return timeout<1000?1000:timeout;
    }
}
