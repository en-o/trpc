
package cn.tannn.trpc.demo.consumer;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.core.consumer.ConsumerConfig;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author tnnn
 */
@SpringBootApplication
@Import(ConsumerConfig.class)
public class TrpcDemoConsumerApplication {

    @TConsumer
    private UserService userService;

    @TConsumer
    private OrderService orderService;

    public static void main(String[] args) {
        SpringApplication.run(TrpcDemoConsumerApplication.class, args);
    }


    /**
     * 启动测试用的
     */
    @Bean
    public ApplicationRunner consumer_runner1() {
        return x -> {
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

            System.out.println(userService.findId(new User(1,"tan")));

            System.out.println(userService.findUser(new User(12,"tan2")));

            System.out.println(userService.findName());

            System.out.println(Arrays.toString(userService.findIds()));
            System.out.println(Arrays.toString(userService.findLongIds()));
            System.out.println(Arrays.toString(userService.findIds(new int[]{3,4,6})));
            System.out.println(userService.getList(List.of(new User(10, "tans"))));
            System.out.println(userService.getMap(Map.of("tanmap",new User(11,"tanmaps"))));


            System.out.println(orderService.findById(2));

            // 模拟异常
//            System.out.println(orderService.findById(404));



        };
    }

}
