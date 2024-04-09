package cn.tannn.trpc.common.chain;

import cn.tannn.trpc.common.api.Filter;
import cn.tannn.trpc.common.api.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

/**
 * 过滤器责任链 -- 利用 spring 体系注入 filters,如果用构造自己传入的话要注意顺序
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/3 下午10:43
 */
@Slf4j
public class FilterChain {

    /**
     *  使用 order注解，这个list是有序的，且自定义排序的
     */
    private final List<Filter> filters;

    public FilterChain(List<Filter> filters) {
        filters.sort(Comparator.comparing(Filter::getOrder).reversed());
        this.filters = filters;
    }


    /**
     * 执行前置处理
     * - 缓存
     * - 幂等
     * - 数据检查
     * @param rpcRequest RpcRequest
     */
    public Object executePref(RpcRequest rpcRequest){
        // 一直为空标表示没有触发到前置的规则
        Object prefilter = null;
        for (Filter filter : filters) {
            // 那最后一次处理得到的数据内容
            prefilter = filter.prefilter(rpcRequest);
            log.debug(" {} ===> prefilter {}",filter.getClass().getName() ,rpcRequest);
        }
        return prefilter;
    }

    /**
     * 执行后置处理
     * @param rpcRequest RpcRequest
     * @param result 返回值处理结果
     */
    public Object executePost(RpcRequest rpcRequest, Object result){
        for (Filter filter : filters) {
            // 将处理好的数据数据赋值给下一位再继续处理,等于空还是继续使用以前的
            Object filterResult = filter.postFilter(rpcRequest, result);
            log.debug(" {} ===> post filter", filter.getClass().getName() );
            if(filterResult != null){
                result = filterResult;
            }
        }

        return result;
    }
}
