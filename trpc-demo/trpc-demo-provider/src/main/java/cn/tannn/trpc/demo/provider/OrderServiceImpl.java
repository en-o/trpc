package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.entity.Order;
import org.springframework.stereotype.Service;

/**
 * order实现
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/1 下午11:24
 */
@Service
@TProvider
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findById(Integer id) {
        return new Order(Long.valueOf(id), 15f);
    }

    @Override
    public Order findById(Long id, Float amount) {
        return new Order(id, amount);
    }


    @Override
    public Long findId() {
        return 0L;
    }
}
