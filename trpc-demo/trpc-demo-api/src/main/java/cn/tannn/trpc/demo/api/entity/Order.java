package cn.tannn.trpc.demo.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 20:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    Long id;
    Float amount;
}
