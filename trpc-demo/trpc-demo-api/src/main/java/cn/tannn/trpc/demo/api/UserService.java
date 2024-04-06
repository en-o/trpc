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

    /**
     * findById
     *
     * @param id id
     * @return User
     */
    User findById(int id);

    /**
     * findById
     *
     * @param id   id
     * @param name name
     * @return User
     */
    User findById(int id, String name);

    /**
     * getId
     *
     * @param id id
     * @return long
     */
    long getId(long id);

    /**
     * getId
     *
     * @param user user
     * @return long
     */
    long getId(User user);

    /**
     * getId
     *
     * @param id id
     * @return long
     */
    long getId(float id);

    /**
     * getName
     *
     * @return String
     */
    String getName();

    /**
     * getName
     *
     * @param id id
     * @return String
     */
    String getName(int id);

    /**
     * getIds
     *
     * @return int
     */
    int[] getIds();

    /**
     * getLongIds
     *
     * @return int
     */
    long[] getLongIds();

    /**
     * getIds
     *
     * @param ids ids
     * @return int
     */
    int[] getIds(int[] ids);

    /**
     * findUsers
     *
     * @param users users
     * @return User
     */
    User[] findUsers(User[] users);

    /**
     * getList
     *
     * @param userList userList
     * @return User
     */
    List<User> getList(List<User> userList);

    /**
     * getMap
     *
     * @param userMap userMap
     * @return Map
     */
    Map<String, User> getMap(Map<String, User> userMap);

    /**
     * getFlag
     *
     * @param flag flag
     * @return Boolean
     */
    Boolean getFlag(boolean flag);

    /**
     * findById
     *
     * @param id id
     * @return User
     */
    User findById(long id);

    /**
     * ex
     *
     * @param flag flag
     * @return User
     */
    User ex(boolean flag);

    /**
     * find
     *
     * @param timeout timeout
     * @return User
     */
    User find(int timeout);

    /**
     * setTimeoutPorts
     *
     * @param timeoutPorts timeoutPorts
     */
    void setTimeoutPorts(String timeoutPorts);

    /**
     * echoParameter
     *
     * @param key key
     * @return String
     */
    String echoParameter(String key);
}
