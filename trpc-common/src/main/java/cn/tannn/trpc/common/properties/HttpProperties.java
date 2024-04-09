package cn.tannn.trpc.common.properties;

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
     * 超时重试次数， 默认1
     */
    Integer retries = 1;


    /**
     * http[r,w,c]超时时间/毫秒 ，默认1s
     */
    Integer timeout = 1000;

    public Integer getRetries() {
        return retries<1?1:retries;
    }


    public Integer getTimeout() {
        return timeout<1000?1000:timeout;
    }


}
