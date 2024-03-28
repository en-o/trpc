package cn.tannn.trpc.core.enums;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.filter.CacheFilter;
import cn.tannn.trpc.core.filter.LocalMethodFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 过滤器枚举
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/28 10:11
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
    LOCAL_METHOD;

    /**
     * 枚举换过滤器实例
     * @param filterEnum
     * @return
     */
    public static Filter findFilter(FilterEnum filterEnum) {
        return switch (filterEnum) {
            case CACHE -> new CacheFilter();
            case LOCAL_METHOD -> new LocalMethodFilter();
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
