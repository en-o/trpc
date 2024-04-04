package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.properties.RpcProperties;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 测试starter提供的rpc接口
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/2 11:28
 */
@SpringBootTest
@WebAppConfiguration
public class InvokeApiTests {

    private String orderServiceNameStr = "cn.tannn.trpc.demo.api.OrderService";

    MockMvc mockMvc;

    @Autowired
    private RpcProperties rpcProperties;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }





   /**
     * 测试 trpc-spring-starter 提供的默认rpc接口
     */
    @Test
    void testInvokeOrderApi() throws Exception {
        // findById@1_java.lang.Integer
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setService(orderServiceNameStr);
        rpcRequest.setMethodSign("findById@1_java.lang.Integer");
        rpcRequest.setArgs(new Object[]{201});
        String rpcRequestJson = JSON.toJSONString(rpcRequest);

        // 构建
        RequestBuilder request = MockMvcRequestBuilders.post("/"+rpcProperties.getApi().getContext())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(rpcRequestJson);
        // 执行
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        System.out.println();
        System.out.println("status_" +response.getStatus()+ orderServiceNameStr+"@findById@1_java.lang.Integer@return"+ response.getContentAsString());
    }
}
