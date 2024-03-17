package cn.tannn.trpc.demo.consumer.runner;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 启动检查
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-16 16:31
 */
@Component
public class StartUpCheck implements ApplicationRunner {

    @TConsumer
    private UserService userService;

    @TConsumer
    private OrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(userService.toString());

        System.out.println("============================================================\n");

        System.out.println(userService.findById(1));

        System.out.println(userService.findId());
        System.out.println(userService.findId(1.1));
        System.out.println(userService.findId(Float.valueOf(1.2f)));
        System.out.println(userService.findId(1.33333));
        System.out.println(userService.findId(Double.valueOf(1.44444D)));
        System.out.println( userService.findId(Long.valueOf(1000L)));

        System.out.println(userService.findId(2));

        if(userService.findId(true)){
            System.out.println("findId@1_boolean");
        }
        for (boolean b : userService.findId(new boolean[]{true, false})) {
            System.out.println("findId@1_boolean[]"+b);
        }


        System.out.println(userService.findId(new User(1,"tan")));

        System.out.println(userService.findUser(new User(12,"tan2")));

        User[] userArray = userService.findUser(new User[]{new User(102, "tan02"), new User(12, "tan02")});
        for (User user : userArray) {
            System.out.println(user.toString());
        }

        System.out.println(userService.findName());

        System.out.println(Arrays.toString(userService.findIds()));
        System.out.println(Arrays.toString(userService.findLongIds()));
        System.out.println(Arrays.toString(userService.findIds(new int[]{3,4,6})));
        System.out.println(userService.getList(null));
        System.out.println(userService.getMap(null));
        userService.getList(List.of(
                new User(10, "tans"),
                new User(102, "tans2")
        )).forEach(user ->  System.out.println("user ==>"+user));

        userService.getListBoolean(List.of(
                true,
                false
        )).forEach(b ->  System.out.println("b ==>"+b));

        Map<String, User> map = userService.getMap(Map.of(
                "tanmap", new User(11, "tanmaps"),
                "tanmap2", new User(112, "tanmaps2")
        ));
        map.forEach((k,v) -> System.out.println("key==>"+k+"v==>"+v));



        System.out.println(orderService.findById(2));

        // 模拟异常
//            System.out.println(orderService.findById(404));

    }
}
