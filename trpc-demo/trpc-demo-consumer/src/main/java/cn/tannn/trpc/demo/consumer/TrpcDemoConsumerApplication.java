
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
            userService.toString();

            System.out.println("============================================================\n");

            System.out.println(userService.findById(1));

            System.out.println(userService.findId());

            System.out.println( userService.findId(Long.valueOf(1000L)));

            System.out.println(userService.findId(2));

            System.out.println(userService.findId(new User(1,"tan")));

            System.out.println(userService.findUser(new User(12,"tan2")));

            System.out.println(userService.findName());
            System.out.println(Arrays.toString(userService.findIds()));


//            Order order = orderService.findById(2);
//            System.out.println("RPC result orderService.findBy(2) = " + order);

            // 模拟异常
//            Order order_404 = orderService.findById(404);
//            System.out.println("RPC result orderService.findBy(404) = " + order_404);



        };
    }

}
