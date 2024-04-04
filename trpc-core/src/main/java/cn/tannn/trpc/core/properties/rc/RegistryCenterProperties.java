package cn.tannn.trpc.core.properties.rc;

import cn.tannn.trpc.core.enums.RegistryCenterEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * 注册中心配置
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/4 下午4:49
 */
@Getter
@Setter
@ToString
public class RegistryCenterProperties {
    /**
     * 注册中心名
     */
    private RegistryCenterEnum name;


    /**
     * 注册中心为 def 时 这里用
     * <p>客户端配置服务端连接信息 - 配置文件方式
     * <p> 服务端访问地址,多个逗号隔开[静态注册 ip_port_context]
     *   <p>   - 127.0.0.1_8081_ [ps 空的时候也要写_]
     *   <p>   - 127.0.0.1_8082_trpc
     */
    private String[] providers;


    /**
     * 连接信息
     * <pr>
     *    正常注册中心这里是填写注册中心的地址，
     * </pr>
     */
    @NestedConfigurationProperty
    private Connect[] connect = new Connect[]{};

    /**
     * rc namespace
     */
    String namespace;


    public RegistryCenterEnum getName() {
        if(name == null){
            return RegistryCenterEnum.DEF;
        }
        return name;
    }

    public Connect[] getConnect() {
        if(connect.length == 0){
            return new Connect[]{new Connect("127.0.0.1","20242")};
        }
        return connect;
    }

    public String getNamespace() {
        if(null == namespace || namespace.isEmpty()){
            return "trpc";
        }
        return namespace;
    }
}
