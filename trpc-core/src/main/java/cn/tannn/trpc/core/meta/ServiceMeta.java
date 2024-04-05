package cn.tannn.trpc.core.meta;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 描述服务元数据  （rc注册的元信息描述
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/4 下午4:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ServiceMeta {

    /**
     * appid
     */
    String appid;

    /**
     * 服务名
     */
    String name;

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
    String version = "0.0.1";


    /**
     * 其他参数
     */
    private Map<String, String> parameters;

    /**
     * 组装 注册中心 path
     * @return zk[app_namespace_env_name_version]
     */
    public String toPath() {
        return String.format("%s_%s_%s_%s_%s", appid, name, env, namespace, version);
    }


    /**
     * parameter json string
     */
    public String toMetas() {
        return JSON.toJSONString(this.getParameters());
    }
}
