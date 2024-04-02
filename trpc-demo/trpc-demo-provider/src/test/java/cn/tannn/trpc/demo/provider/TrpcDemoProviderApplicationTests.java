package cn.tannn.trpc.demo.provider;


import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TrpcDemoProviderApplicationTests {


    @Test
    void contextLoads() {
        System.out.println(" ===> trpcDemoProviderApplicationTests  .... ");
    }


    /**
     * 测试 trpc-spring-starter 提供的默认rpc接口
     */
    @Test
    void testInvokeApi() {
//        // 创建HttpServletRequest的模拟对象
//        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
//        // 设置模拟请求的上下文和参数
//        Mockito.when(request.getContextPath()).thenReturn("/test");
//        Mockito.when(request.getMethod()).thenReturn("GET");
//        Mockito.when(request.getParameter("token")).thenReturn(signBean);
    }




}
