package cn.tannn.trpc.demo.consumer.runner;

import cn.tannn.trpc.common.api.RpcContext;
import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
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
@Order
public class StartUpCheck implements ApplicationRunner {

    @TConsumer
    private OrderService orderService;
    @TConsumer
    private UserService userService;

    @Override
    public void run(ApplicationArguments args) {
        casesOrder();
        cases();
    }


    /**
     * 测试
     */
    public void casesOrder() {
        // 常规Integer类型，返回User对象
        System.out.println("Case 1. >>===[常规Integer类型，返回Order对象]===");
        cn.tannn.trpc.demo.api.entity.Order orders1 = orderService.findById(1);
        System.out.println("RPC result orderService.findById(1) = " + orders1);

        // 参数为Long,Float类型，返回User对象
        System.out.println("Case 2. >>===[参数为Long,Float类型，返回Order对象]===");
        cn.tannn.trpc.demo.api.entity.Order order2 = orderService.findById(2L,15f);
        System.out.println("RPC result orderService.findById(2,15) = " + order2);

        // 无参数，返回Long类型
        System.out.println("Case 3. >>===[无参数，返回Long类型]===");
        Long order3 = orderService.findId();
        System.out.println("RPC result orderService.findId() = " + order3);

    }

    /**
     * 测试
     */
    public void cases() {
// 常规int类型，返回User对象
        System.out.println("Case 1. >>===[常规int类型，返回User对象]===");
        User user = userService.findById(1);
        System.out.println("RPC result userService.findById(1) = " + user);

        // 测试方法重载，同名方法，参数不同
        System.out.println("Case 2. >>===[测试方法重载，同名方法，参数不同===");
        User user1 = userService.findById(1, "hubao");
        System.out.println("RPC result userService.findById(1, \"hubao\") = " + user1);

        // 测试返回字符串
        System.out.println("Case 3. >>===[测试返回字符串]===");
        System.out.println("userService.getName() = " + userService.getName());

        // 测试重载方法返回字符串
        System.out.println("Case 4. >>===[测试重载方法返回字符串]===");
        System.out.println("userService.getName(123) = " + userService.getName(123));

        // 测试local toString方法
        System.out.println("Case 5. >>===[测试local toString方法]===");
        try {
            System.out.println("userService.toString() = " + userService.toString());
        }catch (Exception e){
            System.out.println(" ===> userService.toString(): " + e.getMessage());
        }

        // 测试long类型
        System.out.println("Case 6. >>===[常规int类型，返回User对象]===");
        System.out.println("userService.getId(10) = " + userService.getId(10));

        // 测试long+float类型
        System.out.println("Case 7. >>===[测试long+float类型]===");
        System.out.println("userService.getId(10f) = " + userService.getId(10f));

        // 测试参数是User类型
        System.out.println("Case 8. >>===[测试参数是User类型]===");
        System.out.println("userService.getId(new User(100,\"t\")) = " +
                userService.getId(new User(100,"t")));


        System.out.println("Case 9. >>===[测试返回long[]]===");
        System.out.println(" ===> userService.getLongIds(): ");
        for (long id : userService.getLongIds()) {
            System.out.println(id);
        }

        System.out.println("Case 10. >>===[测试参数和返回值都是long[]]===");
        System.out.println(" ===> userService.getLongIds(): ");
        for (long id : userService.getIds(new int[]{4,5,6})) {
            System.out.println(id);
        }

        // 测试参数和返回值都是List类型
        System.out.println("Case 11. >>===[测试参数和返回值都是List类型]===");
        List<User> list = userService.getList(List.of(
                new User(100, "t100"),
                new User(101, "t101")));
        list.forEach(System.out::println);

        // 测试参数和返回值都是Map类型
        System.out.println("Case 12. >>===[测试参数和返回值都是Map类型]===");
        Map<String, User> map = new HashMap<>();
        map.put("A200", new User(200, "t200"));
        map.put("A201", new User(201, "t201"));
        userService.getMap(map).forEach(
                (k,v) -> System.out.println(k + " -> " + v)
        );

        System.out.println("Case 13. >>===[测试参数和返回值都是Boolean/boolean类型]===");
        System.out.println("userService.getFlag(false) = " + userService.getFlag(false));

        System.out.println("Case 14. >>===[测试参数和返回值都是User[]类型]===");
        User[] users = new User[]{
                new User(100, "t100"),
                new User(101, "t101")};
        Arrays.stream(userService.findUsers(users)).forEach(System.out::println);

        System.out.println("Case 15. >>===[测试参数为long，返回值是User类型]===");
        User userLong = userService.findById(10000L);
        System.out.println(userLong);

        System.out.println("Case 16. >>===[测试参数为boolean，返回值都是User类型]===");
        User user100 = userService.ex(false);
        System.out.println(user100);

        System.out.println("Case 17. >>===[测试服务端抛出一个RuntimeException异常]===");
        try {
            User userEx = userService.ex(true);
            System.out.println(userEx);
        } catch (RuntimeException e) {
            System.out.println(" ===> exception: " + e.getMessage());
        }

        System.out.println("Case 18. >>===[测试服务端抛出一个超时重试后成功的场景]===");
        // 超时设置的【漏斗原则】
        // A 2000 -> B 1500 -> C 1200 -> D 1000
        long start = System.currentTimeMillis();
        userService.find(1100);
        userService.find(8100);
        System.out.println("userService.find take "
                + (System.currentTimeMillis()-start) + " ms");


        System.out.println("Case 19. >>===[测试通过Context跨消费者和提供者进行传参]===");
        String Key_Version = "rpc.version";
        String Key_Message = "rpc.message";

        RpcContext.setContextParameter(Key_Version, "v8");
        RpcContext.setContextParameter(Key_Message, "this is a test message");
        String version = userService.echoParameter(Key_Version);
        System.out.println(" ===> echo parameter from c->p->c: " + Key_Version + " -> " + version);

        // 上下文不服用，所以每次调用都要设置一遍
        RpcContext.setContextParameter(Key_Version, "v8");
        RpcContext.setContextParameter(Key_Message, "this is a test message");
        String message = userService.echoParameter(Key_Message);
        System.out.println(" ===> echo parameter from c->p->c: " + Key_Message + " -> " + message);

    }
}
