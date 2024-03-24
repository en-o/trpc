package cn.tannn.trpc.core.consumer.http;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.consumer.HttpInvoker;
import cn.tannn.trpc.core.exception.ConsumerException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.util.concurrent.TimeUnit;

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

    OkHttpClient httpClient = new OkHttpClient().newBuilder()
            // 连接池
            .connectionPool(new ConnectionPool(16, 60, TimeUnit.SECONDS))
            // 各项超时时间
            .readTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .build();

    @Override
    public RpcResponse<Object> post(RpcRequest rpcRequest, String url) {
        if(url == null){
            return new RpcResponse<>(false,null,new RuntimeException("router is empty"));
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
            throw new ConsumerException(e);
        }
    }


}
