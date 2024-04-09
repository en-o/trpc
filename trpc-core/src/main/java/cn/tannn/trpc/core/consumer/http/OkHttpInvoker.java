package cn.tannn.trpc.core.consumer.http;

import cn.tannn.trpc.common.api.RpcRequest;
import cn.tannn.trpc.common.api.RpcResponse;
import cn.tannn.trpc.common.exception.TrpcException;
import cn.tannn.trpc.common.properties.HttpProperties;
import cn.tannn.trpc.core.consumer.HttpInvoker;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

import static cn.tannn.trpc.common.exception.ExceptionCode.HTTP_POST_EX;
import static cn.tannn.trpc.common.exception.ExceptionCode.HTTP_URI_EX;

/**
 * okhttp 请求
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-20 20:41
 */
@Slf4j
public class OkHttpInvoker implements HttpInvoker {
    /**
     * HTTP JSON_TYPE
     */
    final static MediaType JSON_TYPE = MediaType.get("application/json; charset=UTF-8");

    OkHttpClient httpClient;

    /**
     * @param httpProperties r,w,c 超时时间
     */
    public OkHttpInvoker(HttpProperties httpProperties) {
        log.debug("http config ===> {}", httpProperties);
        this.httpClient = new OkHttpClient().newBuilder()
                // 连接池
                .connectionPool(new ConnectionPool(16, 60, TimeUnit.SECONDS))
                // 各项超时时间
                .readTimeout(httpProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(httpProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(httpProperties.getTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }


    @Override
    public RpcResponse<Object> post(RpcRequest rpcRequest, String url) {
        if (url == null) {
            return new RpcResponse<>(false, null, new TrpcException(HTTP_URI_EX));
        }
        String reqJson = JSON.toJSONString(rpcRequest);
        log.debug(" ===> reqJson = {}", reqJson);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(reqJson, JSON_TYPE))
                .build();
        try {
            String responseJson = httpClient.newCall(request).execute().body().string();
            log.debug(" ===> respJson = {}", responseJson);
            return JSON.parseObject(responseJson, RpcResponse.class);
        } catch (Exception e) {
            throw new TrpcException(e, HTTP_POST_EX);
        }
    }


}
