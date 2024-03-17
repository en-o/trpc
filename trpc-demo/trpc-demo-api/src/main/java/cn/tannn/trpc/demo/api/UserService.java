package cn.tannn.trpc.demo.api;

import cn.tannn.trpc.demo.api.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户接口
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 20:36
 */
public interface UserService {

    /**
     * 查询用户
     * @param id id
     * @return User
     */
    User findById(Integer id);



    /**
     * 测试返回 Integer
     * @return Integer
     */
    Integer findId();
    float findId(float id);
    Float findId(Float id);
    double findId(double id);
    Double findId(Double id);
    Long findId(Long id);
    long findId(long id);
    boolean findId(boolean id);
    boolean[] findId(boolean[] id);
    int findId(User user);

    int[] findIds();
    int[] findIds(int[] ids);
    long[] findLongIds();

    User findUser(User user);
    User[] findUser(User[] user);
    List<User> getList(List<User> userList);
    List<Boolean> getListBoolean(List<Boolean> userListBoolean);
    Map<String,User> getMap(Map<String,User> userMap);

    /**
     * 测试返回 String
     * @return String
     */
    String findName();

}
