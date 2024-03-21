package cn.tannn.trpc.core.meta;

import cn.tannn.trpc.core.config.ServiceProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述服务元数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-20 21:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceMeta extends ServiceProperties {

    /**
     * 服务名 - 配置文件中设置无效
     */
    String name;


    /**
     * 组装 注册中心 path
     * @return zk[app_namespace_env_name_version]
     */
    public String toPath() {
        return String.format("%s_%s_%s_%s_%s", getAppid(), getNamespace(), getEnv(), name, getVersion());
    }
}
