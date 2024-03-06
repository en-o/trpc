package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.demo.api.User;
import cn.tannn.trpc.demo.api.UserService;
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
}
