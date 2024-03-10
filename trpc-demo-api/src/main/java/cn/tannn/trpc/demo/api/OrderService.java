package cn.tannn.trpc.demo.api;

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



}
