package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.api.RpcRequest;
import cn.tannn.trpc.core.properties.RpcProperties;
import cn.tannn.trpc.core.test.TestZKServer;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

@SpringBootTest
@WebAppConfiguration
class TrpcDemoProviderApplicationTests {
    private String orderServiceNameStr = "cn.tannn.trpc.demo.api.OrderService";

    MockMvc mockMvc;

    static TestZKServer zkServer = new TestZKServer();

    @Autowired
    private RpcProperties rpcProperties;

    @BeforeAll
    static void init() {
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        System.out.println(" =============     ZK2182    ========== ");
        System.out.println(" ====================================== ");
        System.out.println(" ====================================== ");
        zkServer.start();
    }
    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void contextLoads() {
        System.out.println(" ===> trpcDemoProviderApplicationTests  .... ");
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


    @AfterAll
    static void destory() {
        zkServer.stop();
    }
}
