package cn.tannn.trpc.core.properties;

import cn.tannn.trpc.core.enums.RegistryCenterEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
     *   <p>   - 127.0.0.1_8081_/ [ps 空的时候也要写_/]
     *   <p>   - 127.0.0.1_8082_/trpc
     */
    private String[] providers;



    public RegistryCenterEnum getName() {
        if(name == null){
            return RegistryCenterEnum.DEF;
        }
        return name;
    }

}
