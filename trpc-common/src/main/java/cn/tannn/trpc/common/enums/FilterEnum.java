package cn.tannn.trpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 过滤器枚举
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/3 下午10:42
 */
@AllArgsConstructor
@Getter
public enum FilterEnum {
    /**
     * 默认
     */
    DEFAULT,


    /**
     * 请求缓存
     */
    CACHE,

    /**
     * 本地方法拦截
     */
    LOCAL_METHOD,

    /**
     * 嵌入上下文参数到请求体中
     */
    CONTEXT_PARAMETER;


}
