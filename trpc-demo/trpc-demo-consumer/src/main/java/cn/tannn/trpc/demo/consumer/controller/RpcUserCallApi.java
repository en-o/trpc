package cn.tannn.trpc.demo.consumer.controller;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * rpc接口测试
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 21:55
 */
@RestController
@RequestMapping("user")
public class RpcUserCallApi {

    @TConsumer
    private UserService userService;

    /**
     * 测试返回对象为Bean
     * @param id id
     * @return User
     */
    @GetMapping("/{id}")
    public User findUser(@PathVariable(value = "id",required = false) Integer id){
        if(id == null){
            id = 10;
        }
        return userService.findById(id);
    }

    /**
     * 测试返回对象为数字
     * @return Integer
     */
    @GetMapping("/id")
    public Integer findId(){
        return userService.findId();
    }

    /**
     * 测试返回对象为字符串
     * @return Integer
     */
    @GetMapping("/name")
    public String findName(){
        return userService.findName();
    }
}
