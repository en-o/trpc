package cn.tannn.trpc.demo.api;

import cn.tannn.trpc.demo.api.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 序列化 case
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 20:36
 */
public interface UserService {

    User findById(Integer id);
    User findById(int id, String name);

    float findId(float id);
    float findId(long id);
    float findId(User id);

    long getId(User user);

    long getId(float id);

    double findId(double id);

    double findId(int id);

    String getName();

    String getName(int id);

    int[] getIds();


    long[] getLongIds();
    int[] getIds(int[] ids);

    User[] findUsers(User[] users);

    User findUser(User user);

    List<User> getList(List<User> userList);

    List<Boolean> getListBoolean(List<Boolean> userListBoolean);

    Map<String, User> getMap(Map<String, User> userMap);

    boolean getFlag(boolean id);
    boolean[] getFlag(boolean[] id);

    User findById(long id);
    User ex(boolean flag);


    /**
     * 8081端口模拟超时
     * @param timeout 超时/毫秒
     */
    User find(int timeout);
}
