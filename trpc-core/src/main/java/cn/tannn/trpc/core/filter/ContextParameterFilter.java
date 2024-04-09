package cn.tannn.trpc.core.filter;

import cn.tannn.trpc.common.api.Filter;
import cn.tannn.trpc.common.api.RpcContext;
import cn.tannn.trpc.common.api.RpcRequest;

import java.util.Map;

/**
 * 嵌入上下文参数到请求体中
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/5 下午2:09
 */
public class ContextParameterFilter implements Filter {

    @Override
    public Object prefilter(RpcRequest request) {
        // 上下文不为空才传递
        Map<String, String> params = RpcContext.ContextParameters.get();
        if (!params.isEmpty()) {
            // 追加
            request.getParams().putAll(params);
        }
        return null;
    }

    @Override
    public Object postFilter(RpcRequest request, Object result) {
        // 这里自动清空, 上下文不允许复用。如果非要服用，请求在把这里注释，在调用完成后主动clear
        RpcContext.ContextParameters.get().clear();
        return null;
    }

    @Override
    public int getOrder() {
        return 3;
    }
}
