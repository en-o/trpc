package cn.tannn.trpc.core.registry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 事件驱动
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-17 21:54
 */
@Data
@AllArgsConstructor
public class Event {
    List<String> data;
}
