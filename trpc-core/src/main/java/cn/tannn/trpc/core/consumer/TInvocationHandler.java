package cn.tannn.trpc.core.consumer;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.core.util.MethodUtils;
import cn.tannn.trpc.core.util.TypeUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 动态代理，对属性进行初始化，注入
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 20:03
 */
public class TInvocationHandler implements InvocationHandler {
    Class<?> service;

    /**
     * HTTP JSON_TYPE
     */
    final static MediaType JSON_TYPE = MediaType.get("application/json; charset=UTF-8");


    public TInvocationHandler(Class<?> service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 屏蔽 toString / equals 等 Object 的一些基本方法
        if (MethodUtils.checkLocalMethod(method)) {
            return null;
        }

        //组装调用参数 ： 类全限定名称，方法，参数
        RpcRequest rpcRequest = new RpcRequest(service.getCanonicalName(), MethodUtils.methodSign(method), args);

        // 发送请
        RpcResponse rpcResponse = post(rpcRequest);
        if (rpcResponse.isStatus()) {
            Object data = rpcResponse.getData();
            Class<?> returnType = method.getReturnType();
            // 处理 Map<String,Bean> 中的Bean类型丢失
            if (data instanceof JSONObject jsonResult && Map.class.isAssignableFrom(returnType)) {
                Type genericReturnType = method.getGenericReturnType();
                Map resultMap = new HashMap<>();
                if (genericReturnType instanceof ParameterizedType parameterizedType) {
                    Type actualTypeKey = parameterizedType.getActualTypeArguments()[0];
                    Type actualTypeValue = parameterizedType.getActualTypeArguments()[1];
                    jsonResult.forEach((k,v) -> {
                        resultMap.put(TypeUtils.cast(k, (Class<?>) actualTypeKey),
                                TypeUtils.cast(v, (Class<?>) actualTypeValue));
                    });
                } else {
                     jsonResult.toJavaObject(method.getReturnType());
                }
                return resultMap;
            }else if (data instanceof JSONArray jsonArray && List.class.isAssignableFrom(returnType)) {
                //  处理 List<Bean> 中的Bean类型丢失
                Object[] array = jsonArray.toArray();
                List<Object> resultList = new ArrayList<>(array.length);
                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType parameterizedType) {
                    Type actualType = parameterizedType.getActualTypeArguments()[0];
                    for (Object o : array) {
                        resultList.add(TypeUtils.cast(o, (Class<?>) actualType));
                    }
                } else {
                    resultList.addAll(Arrays.asList(array));
                }
                return resultList;
            }else {
                // 处理结果类型
//            return JSON.to(method.getReturnType(),  data);
                // TypeUtils.cast 是模拟上面那个写的
                return TypeUtils.cast(data, method.getReturnType());
            }
        } else {
            // 处理回传的调用期间发生的异常
            Exception ex = rpcResponse.getEx();
            throw new RuntimeException(ex);
        }
    }

    OkHttpClient httpClient = new OkHttpClient().newBuilder()
            // 连接池
            .connectionPool(new ConnectionPool(16, 60, TimeUnit.SECONDS))
            // 各项超时时间
            .readTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .build();

    /**
     * 发送 http 请求
     *
     * @param rpcRequest RpcRequest
     * @return
     */
    private RpcResponse post(RpcRequest rpcRequest) {
        String reqJson = JSON.toJSONString(rpcRequest);
        System.out.println("reqJson =====> " + reqJson);
        Request request = new Request.Builder()
                .url("http://localhost:8080/")
                .post(RequestBody.create(reqJson, JSON_TYPE))
                .build();
        try {
            String responseJson = httpClient.newCall(request).execute().body().string();
            System.out.println("respJson =====> " + responseJson);
            return JSON.parseObject(responseJson, RpcResponse.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
