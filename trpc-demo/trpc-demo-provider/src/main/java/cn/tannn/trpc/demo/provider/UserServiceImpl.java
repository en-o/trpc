package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.stereotype.Component;

/**
 * 用户接口实现
 *
 * @author tnnn
 * @version V1.0
 * @date 2024-03-06 20:43
 */
@Component
@TProvider
public class UserServiceImpl implements UserService {
    @Override
    public User findById(Integer id) {
        return new User(id, "t-" + System.currentTimeMillis());
    }

    @Override
    public Integer findId() {
        return 1;
    }

    @Override
    public long findId(Long id) {
        return id;
    }

    @Override
    public Long findId(long id) {
        return id;
    }

    @Override
    public int findId(User user) {
        return user.getId();
    }

    @Override
    public User findUser(User user) {
        return user;
    }

    @Override
    public String findName() {
        return "tn";
    }
}