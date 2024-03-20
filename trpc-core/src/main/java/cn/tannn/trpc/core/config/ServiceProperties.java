package cn.tannn.trpc.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用信息 [下面都是服务端的信息，服务端配置的自己的信息,客户端配置文件配置的是服务的信息]
 * @author tnnn
 * @version V1.0
 * @date 2024-03-20 22:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProperties {
    /**
     * appid
     */
    String appid;
    /**
     * 空间隔离
     */
    String namespace;
    /**
     * 环境隔离
     */
    String env;
}
