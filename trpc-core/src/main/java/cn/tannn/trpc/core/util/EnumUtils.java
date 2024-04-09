package cn.tannn.trpc.core.util;

import cn.tannn.trpc.common.api.Filter;
import cn.tannn.trpc.common.enums.FilterEnum;
import cn.tannn.trpc.core.filter.CacheFilter;
import cn.tannn.trpc.core.filter.ContextParameterFilter;
import cn.tannn.trpc.core.filter.LocalMethodFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 枚举工具类
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/9 14:03
 */
public class EnumUtils {

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
