package cn.tannn.trpc.demo.api;

import cn.tannn.trpc.demo.api.entity.User;

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
    long findId(Long id);
    Long findId(long id);
    int findId(User user);

    User findUser(User user);


    /**
     * 测试返回 String
     * @return String
     */
    String findName();

}
