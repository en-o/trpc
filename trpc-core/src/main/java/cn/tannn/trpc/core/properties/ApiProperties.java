package cn.tannn.trpc.core.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 接口相关
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/2 10:47
 */
@Getter
@Setter
@ToString
public class ApiProperties {
    /**
     * rpc暴露的接口请求前缀【默认trpc, e.g trpc】
     */
    private String context;

    public String getContext() {
        if(null == context || context.isEmpty()){
            return "trpc";
        }
        return context;
    }
}
