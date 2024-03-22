package cn.tannn.trpc.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用信息 [服务端写自己的发布信息，客户端写服务端的连接信息]
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

    /**
     * 版本隔离
     */
    String version;


}
