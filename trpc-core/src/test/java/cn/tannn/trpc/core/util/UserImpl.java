package cn.tannn.trpc.core.util;

/**
 * 测试类
 *
 * @author tnnn
 * @version V1.0
 * @date 2024/4/1 下午10:48
 */
public class UserImpl implements User {
    @Override
    public void hi() {

    }

    @Override
    public String lo(String name) {
        return "";
    }

    @Override
    public String lo(String name, Integer age) {
        return "";
    }

    @Override
    public String toString() {
        return "UserImpl{}";
    }
}
