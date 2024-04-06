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
     *  findById
     * @param id id
     * @return Order
     */
    Order findById(Integer id);

    /**
     *  findById
     * @param id id
     * @param amount amount
     * @return Order
     */
    Order findById(Long id, Float amount);


    /**
     * findId
     * @return Long
     */
    Long findId();




}
