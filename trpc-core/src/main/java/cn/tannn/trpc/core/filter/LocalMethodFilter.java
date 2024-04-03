package cn.tannn.trpc.core.filter;

import cn.tannn.trpc.core.api.Filter;
import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.exception.ExceptionCode;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.core.util.MethodUtils;
import org.springframework.core.annotation.Order;

/**
 * 本地方法过滤
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/3 下午10:43
 */
@Order(1)
public class LocalMethodFilter  implements Filter {

    @Override
    public Object prefilter(RpcRequest request) {
        // 屏蔽 toString / equals 等 Object 的一些基本方法
        if (MethodUtils.checkLocalMethod(request.getMethodSign().substring(0,request.getMethodSign().indexOf("@")))) {
            throw new TrpcException(ExceptionCode.ILLEGALITY_METHOD_EX);
        }
        return null;
    }

    @Override
    public Object postFilter(RpcRequest request, Object result) {
        return null;
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
