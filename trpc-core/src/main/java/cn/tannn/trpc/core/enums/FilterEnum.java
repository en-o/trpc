package cn.tannn.trpc.core.enums;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.filter.CacheFilter;
import cn.tannn.trpc.core.filter.LocalMethodFilter;
import cn.tannn.trpc.core.filter.ContextParameterFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 枚举换过滤器实例
     * @param filterEnum
     * @return
     */
    public static Filter findFilter(FilterEnum filterEnum) {
        return switch (filterEnum) {
            case CACHE -> new CacheFilter();
            case LOCAL_METHOD -> new LocalMethodFilter();
            case CONTEXT_PARAMETER -> new ContextParameterFilter();
            default -> Filter.Default;
        };
    }

    /**
     * 枚举换过滤器实例
     * @param filterEnums filterEnums
     * @return Filter[]
     */
    public static List<Filter> findFilter(FilterEnum[] filterEnums) {
        List<Filter> filters = new ArrayList<>();
        for (FilterEnum filterEnum : filterEnums) {
            Filter filter = findFilter(filterEnum);
            filters.add(filter);
        }
        return filters;
    }
}
