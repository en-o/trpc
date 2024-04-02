package cn.tannn.trpc.demo.consumer.runner;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
        System.out.println("Case 1. >>===[参数为Long,Float类型，返回Order对象]===");
        cn.tannn.trpc.demo.api.entity.Order order2 = orderService.findById(2L,15f);
        System.out.println("RPC result orderService.findById(2,15) = " + order2);

        // 无参数，返回Long类型
        System.out.println("Case 1. >>===[无参数，返回Long类型]===");
        Long order3 = orderService.findId();
        System.out.println("RPC result orderService.findId() = " + order3);

    }

    /**
     * 测试
     */
    public void cases() {

    }
}
