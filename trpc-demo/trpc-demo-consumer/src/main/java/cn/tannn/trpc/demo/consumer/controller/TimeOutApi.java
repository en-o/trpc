package cn.tannn.trpc.demo.consumer.controller;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.core.api.RpcResponse;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * 超时
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/4 下午4:19
 */
@RestController
@RequestMapping("timeOut")
public class TimeOutApi {

    @TConsumer
    private UserService userService;

    /**
     * 测试超时 - 默认 8081,8094 端口会用超时处理
     * @return Integer
     */
    @GetMapping("/{timeOut}")
    public User timeOut(@PathVariable("timeOut") Integer timeOut){
        long start = System.currentTimeMillis();
        User user = userService.find(timeOut);
        System.out.println("userService.find task : " + (System.currentTimeMillis() - start) + " ms" );
        return user;
    }

}
