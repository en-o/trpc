package cn.tannn.trpc.demo.api;

import cn.tannn.trpc.demo.api.entity.Order;

/**
 * 订单
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 20:54
 */
public interface OrderService {

    Order findById(Integer id);


    Order findById(Long id, Float amount);

    Long findId();




}
