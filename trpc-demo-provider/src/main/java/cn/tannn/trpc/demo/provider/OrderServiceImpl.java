package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.demo.api.Order;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.User;
import org.springframework.stereotype.Component;

/**
 * 订单
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 20:56
 */
@Component
@TProvider
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findById(Integer id) {
        if(id == 404){
            throw new RuntimeException("404了呀");
        }
        return new Order(id.longValue(), 15.6f);
    }
}
