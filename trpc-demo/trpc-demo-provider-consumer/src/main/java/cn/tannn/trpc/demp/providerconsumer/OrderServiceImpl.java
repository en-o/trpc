package cn.tannn.trpc.demp.providerconsumer;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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


    @Autowired
    Environment environment;


    @Override
    public Order findById(Integer id) {
        if(id == 404){
            throw new RuntimeException("404了呀");
        }
        String property = environment.getProperty("server.port");
        return new Order(id.longValue(), Float.valueOf(property));
    }

    @Override
    public Order findById2(Long id) {
        String property = environment.getProperty("server.port");
        return new Order(id,  Float.valueOf(property));
    }

    @Override
    public Long findId() {
        return 3L;
    }
}
