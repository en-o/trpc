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
    public float findId(float id) {
        return id;
    }

    @Override
    public Float findId(Float id) {
        return id;
    }

    @Override
    public double findId(double id) {
        return id;
    }

    @Override
    public Double findId(Double id) {
        return id;
    }

    @Override
    public Long findId(Long id) {
        return id;
    }

    @Override
    public long findId(long id) {
        return id;
    }

    @Override
    public int findId(User user) {
        return user.getId();
    }

    @Override
    public int[] findIds() {
        return new int[]{1, 2, 3};
    }

    @Override
    public int[] findIds(int[] ids) {
        return ids;
    }

    @Override
    public long[] findLongIds() {
        return new long[]{2,3,4};
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
