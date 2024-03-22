package cn.tannn.trpc.demp.providerconsumer;

import cn.tannn.trpc.core.annotation.TConsumer;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.entity.Order;
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
@RequestMapping("order")
public class RpcOrderCallApi {


    @TConsumer
    private OrderService orderService;


    /**
     * 测试返回对象为Bean
     * @param id id
     * @return Order
     */
    @GetMapping("/{id}")
    public Order findOrder(@PathVariable(value = "id",required = false) Integer id){
        if(id == null){
            id = 10;
        }
       return orderService.findById(id);
    }

    /**
     * 测试返回对象为Bean
     * @param id id
     * @return Order
     */
    @GetMapping("/l/{id}")
    public Order findOrder2(@PathVariable(value = "id",required = false) Long id){
        if(id == null){
            id = 10L;
        }
        return orderService.findById2(id);
    }


    /**
     * 测试返回对象为数字
     * @return Integer
     */
    @GetMapping("/id")
    public Long findId(){
        return orderService.findId();
    }


    /**
     * 测试返回异常
     * @return Order
     */
    @GetMapping("/404")
    public Order findOrder(){
        return orderService.findById(404);
    }
}
