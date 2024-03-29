package cn.tannn.trpc.demo.api;

import cn.tannn.trpc.demo.api.entity.Order;

/**
 * 订单
 * @author tnnn
 * @version V1.0
 * @date 2024-03-10 20:54
 */
public interface OrderService {

    /**
     * 查询订单
     * @param id id
     * @return User
     */
    Order findById(Integer id);

    /**
     * 测试入参 Long
     * @param id id
     * @return Order
     */
    Order findById2(Long id);


    /**
     * 测试出参Long
     * @return Long
     */
    Long findId();




}
