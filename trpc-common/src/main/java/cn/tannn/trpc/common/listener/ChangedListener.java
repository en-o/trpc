package cn.tannn.trpc.common.listener;

/**
 * 监听服务变化
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-17 21:48
 */
public interface ChangedListener {
    /**
     * 处理事件
     * @param event Event
     */
    void fire(Event event);
}
