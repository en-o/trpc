package cn.tannn.trpc.core.config.registry;

import cn.tannn.trpc.core.enums.RegistryCenterEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


/**
 * 注册中心配置
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 11:19
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
     * 连接信息
     * <pr>
     *    正常注册中心这里是填写注册中心的地址，
     * </pr>
     */
    @NestedConfigurationProperty
    private Connect[] connect = new Connect[]{};

    /**
     * 注册中心为 def 时 这里用
     * <pr>
     *     服务端访问地址,多个逗号隔开[静态注册 ip_port]
     *     - 127.0.0.1_8083
     *     - 127.0.0.1_8083
     *     - 127.0.0.1_8083
     * </pr>
     */
    private String[] providers;


    /**
     * namespace
     */
    String namespace;


    public RegistryCenterEnum getName() {
        if(name == null){
            return RegistryCenterEnum.DEF;
        }
        return name;
    }

}
