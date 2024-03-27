package cn.tannn.trpc.core.filter;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.api.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 过滤器责任链
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/27 15:47
 */
@Component
@Slf4j
public class FilterChain {

    /**
     *  使用 order注解，这个list是有序的，且自定义排序的
     */
    private final List<Filter> filters;

    public FilterChain(List<Filter> filters) {
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
            // 将处理好的数据数据赋值给下一位再继续处理
            Object filterResult = filter.postFilter(rpcRequest, result);
            if(filterResult != null){
                result = filterResult;
            }
        }
        return result;
    }
}
