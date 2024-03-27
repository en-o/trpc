package cn.tannn.trpc.demo.consumer.runner;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.core.exception.TrpcException;
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
    private UserService userService;

    @TConsumer
    private OrderService orderService;

    @Override
    public void run(ApplicationArguments args) {

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


        try {
            // 测试local toString方法
            System.out.println("Case 5. >>===[测试local toString方法]===");
            System.out.println("userService.toString() = " + userService.toString());
        } catch (TrpcException e) {
            System.out.println(" ===> 调用本地方法 exception: " + e.getMessage());
        }


        // 测试long类型
        System.out.println("Case 6. >>===[常规int类型，返回User对象]===");
        System.out.println("userService.getId(10) = " + userService.getId(10));

        // 测试long+float类型
        System.out.println("Case 7. >>===[测试long+float类型]===");
        System.out.println("userService.getId(10f) = " + userService.getId(10f));

        // 测试User+float类型
        System.out.println("Case 8. >>===[测试long+float类型]===");
        System.out.println("userService.findId(new User(100,\"tan_f\")) = " + userService.findId(new User(100,"tan_f")));


        // 测试long+float类型
        System.out.println("Case 9. >>===[测试long+float类型]===");
        System.out.println("userService.findId(100L) = " + userService.findId(100L));

        // 测试double类型+double类型类型
        System.out.println("Case 10. >>===[测试double+double类型]===");
        System.out.println("userService.findId(100D) = " + userService.findId(100D));

        // 测试double+int类型类型
        System.out.println("Case 11. >>===[测试double+double类型]===");
        System.out.println("userService.findId(10) = " + userService.findId(10));

        // 测试参数是User类型
        System.out.println("Case 12. >>===[测试参数是User类型]===");
        System.out.println("userService.getId(new User(100,\"KK\")) = " +
                userService.getId(new User(100,"KK")));

        // 测试参数返回都是是User类型
        System.out.println("Case 13. >>===[测试参数是User类型]===");
        User tanUser = userService.findUser(new User(100, "tan"));
        System.out.println("userService.findUser(new User(100,\"tan\")) = " + tanUser );


        System.out.println("Case 14. >>===[测试返回long[]]===");
        System.out.println(" ===> userService.getLongIds(): ");
        for (long id : userService.getLongIds()) {
            System.out.println(id);
        }

        System.out.println("Case 15. >>===[测试参数和返回值都是long[]]===");
        System.out.println(" ===> userService.getIds(new int[]{4,5,6})): ");
        for (long id : userService.getIds(new int[]{4,5,6})) {
            System.out.println(id);
        }

        System.out.println("Case 16. >>===[测试参数和返回值都是long[]]===");
        System.out.println(" ===> userService.getIds(): ");
        for (long id : userService.getIds()) {
            System.out.println(id);
        }


        // 测试参数和返回值都是List类型
        System.out.println("Case 17. >>===[测试参数和返回值都是List-user类型]===");
        List<User> listBean = userService.getList(List.of(
                new User(100, "KK100"),
                new User(101, "KK101")));
        listBean.forEach(System.out::println);

        // 测试参数和返回值都是List类型
        System.out.println("Case 18. >>===[测试参数和返回值都是List-boolean类型]===");
        List<Boolean> listBoolean = userService.getListBoolean(List.of(
                true,
                false
        ));
        listBoolean.forEach(System.out::println);

        // 测试参数和返回值都是Map类型
        System.out.println("Case 19. >>===[测试参数和返回值都是Map类型]===");
        Map<String, User> map = new HashMap<>();
        map.put("A200", new User(200, "KK200"));
        map.put("A201", new User(201, "KK201"));
        userService.getMap(map).forEach(
                (k,v) -> System.out.println(k + " -> " + v)
        );

        System.out.println("Case 20. >>===[测试参数和返回值都是Boolean/boolean类型]===");
        System.out.println("userService.getFlag(false) = " + userService.getFlag(false));

        System.out.println("Case 21. >>===[测试参数和返回值都是Boolean[]/boolean[]类型]===");
        System.out.println("userService.getFlag(false) = " + userService.getFlag(new boolean[]{true,false}));

        System.out.println("Case 22. >>===[测试参数和返回值都是User[]类型]===");
        User[] users = new User[]{
                new User(100, "KK100"),
                new User(101, "KK101")};
        Arrays.stream(userService.findUsers(users)).forEach(System.out::println);

        System.out.println("Case 23. >>===[测试参数为long，返回值是User类型]===");
        User userLong = userService.findById(10000L);
        System.out.println(userLong);

        System.out.println("Case 24. >>===[测试参数为boolean，返回值都是User类型]===");
        User user100 = userService.ex(false);
        System.out.println(user100);

        System.out.println("Case 25. >>===[测试服务端抛出一个TrpcException异常]===");
        try {
            User userEx = userService.ex(true);
            System.out.println(userEx);
        } catch (TrpcException e) {
            System.out.println(" ===> exception: " + e.getMessage());
        }

        System.out.println("Case 26. >>===[null类型，返回User对象]===");
        User nUser = userService.findById(null);
        System.out.println("RPC result userService.findById(null) = " + nUser);

    }
}
