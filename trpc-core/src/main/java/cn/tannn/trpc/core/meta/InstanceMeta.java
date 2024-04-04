package cn.tannn.trpc.core.meta;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 描述服务实例的元数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-20 21:04
 */
@Data
@NoArgsConstructor
public class InstanceMeta {

    /**
     * 协议 [http]
     */
    private String schema;

    /**
     * host
     */
    private String host;

    /**
     * port
     */
    private Integer port;

    /**
     * 上下文
     */
    private String context;



    /**
     *  基础构造 [schema://host:port/context]
     * @param schema 协议 [http]
     * @param host host
     * @param port port
     * @param context 上下文
     */
    public InstanceMeta(String schema, String host, Integer port, String context) {
        this.schema = schema;
        this.host = host;
        this.port = port;
        this.context = context;
    }

    /**
     * 组装 注册中心 path
     * @return zk[host_port]
     */
    public String toPath() {
        return String.format("%s_%d_%s", host, port, context);
    }

    /**
     * 组装基础 http基础构造 [http,host,port,null]
     * @param host host
     * @param port port
     * @return InstanceMeta
     */
    public static InstanceMeta http(String host, Integer port) {
        return new InstanceMeta("http", host, port, "");
    }

    /**
     * 组装基础 http基础构造 [http,host,port,null]
     * @param host host
     * @param port port
     * @return InstanceMeta
     */
    public static InstanceMeta http(String host, Integer port, String context) {
        return new InstanceMeta("http", host, port, context);
    }

    /**
     * 组装 url [context自带斜杠噢]
     * @return [schema://host:port/context]
     */
    public String toUrl() {
        return String.format("%s://%s:%d/%s", schema, host, port, context);
    }

}
