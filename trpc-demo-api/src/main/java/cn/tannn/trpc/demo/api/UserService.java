package cn.tannn.trpc.demo.api;

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
}
