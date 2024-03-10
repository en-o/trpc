package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 动态代理，对属性进行初始化，注入
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 20:03
 */
public class TInvocationHandler implements InvocationHandler {
    Class<?> service;

    /**
     * HTTP JSON_TYPE
     */
    final  static MediaType JSON_TYPE = MediaType.get("application/json; charset=UTF-8");



    public TInvocationHandler(Class<?> service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // todo 屏蔽 toString / equals 等 Object 的一些基本方法
        String requestMethodName = method.getName();
        if(requestMethodName.equals("toString")
                || requestMethodName.equals("hashCode")){
            return null;
        }
        //类全限定名称，方法，参数
        RpcRequest rpcRequest = new RpcRequest(service.getCanonicalName(), requestMethodName, args);
        // 发送请
        RpcResponse rpcResponse = post(rpcRequest);
        if(rpcResponse.isStatus()){
            // todo 处理基本类型
            Object result = rpcResponse.getData();
            if(result instanceof JSONObject jsonResult){
                // 返回原本的T类型
                return jsonResult.toJavaObject(method.getReturnType());
            }else {
                // 返回原本的T类型
                return result;
            }

        }else {
            // 处理回传的调用期间发生的异常
            Exception ex = rpcResponse.getEx();
            throw  ex;
        }
    }


    OkHttpClient httpClient = new OkHttpClient().newBuilder()
            // 连接池
            .connectionPool(new ConnectionPool(16,60,TimeUnit.SECONDS))
            // 各项超时时间
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build();

    /**
     * 发送 http 请求
     * @param rpcRequest RpcRequest
     * @return
     */
    private RpcResponse post(RpcRequest rpcRequest) {
        String reqJson =  JSON.toJSONString(rpcRequest);
        System.out.println("reqJson =====> " + reqJson);
        Request request = new Request.Builder()
                .url("http://localhost:8080/")
                .post(RequestBody.create(reqJson, JSON_TYPE))
                .build();
        try {
            String responseJson = httpClient.newCall(request).execute().body().string();
            System.out.println("respJson =====> " + responseJson);
            return JSON.parseObject(responseJson, RpcResponse.class);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}
