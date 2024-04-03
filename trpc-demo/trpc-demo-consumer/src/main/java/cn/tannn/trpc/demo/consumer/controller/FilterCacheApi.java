package cn.tannn.trpc.demo.consumer.controller;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/3 下午10:57
 */
@RestController
@RequestMapping("filter")
public class FilterCacheApi {

    @TConsumer
    private UserService userService;


    @GetMapping("/cache")
    public User cache(){
        return userService.findById(100);
    }


    @GetMapping("/localmethod")
    public String localmethod(){
        return userService.toString();
    }
}
