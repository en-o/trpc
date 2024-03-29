package cn.tannn.trpc.core.properties.registry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 连接
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 11:36
 */
@Getter
@Setter
@ToString
public class Connect {

    /**
     * ip
     */
    String ip;

    /**
     * 端口
     */
    String port;

    /**
     * 组装 ip+port
     * @return 127.0.0.1:8080
     */
    public String connectString() {
        return ip + ":" + port;
    }
}
