package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.demo.api.OrderService;
import cn.tannn.trpc.demo.api.entity.Order;

/**
 * order实现
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/1 下午11:24
 */
public class OrderServiceImpl  implements OrderService {
    @Override
    public Order findById(Integer id) {
        return new Order(Long.valueOf(id), 15f);
    }

    @Override
    public Order findById2(Long id) {
        return new Order(id, 15f);
    }

    @Override
    public Long findId() {
        return 0L;
    }
}
