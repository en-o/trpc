package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    @Autowired
    Environment environment;

    @Override
    public User findById(Integer id) {
        return new User(id, "t-" + environment.getProperty("server.port") + "_" + System.currentTimeMillis());
    }

    @Override
    public User findById(int id, String name) {
        return new User(id, "t-" + name + "_" + System.currentTimeMillis());
    }

    @Override
    public float findId(float id) {
        return id;
    }

    @Override
    public float findId(long id) {
        return id;
    }

    @Override
    public float findId(User id) {
        return id.getId();
    }

    @Override
    public long getId(User user) {
        return user.getId();
    }

    @Override
    public long getId(float id) {
        return 1L;
    }

    @Override
    public double findId(double id) {
        return id;
    }

    @Override
    public double findId(int id) {
        return id;
    }

    @Override
    public String getName() {
        return "tan 123";
    }

    @Override
    public String getName(int id) {
        return "tan -" + id;
    }

    @Override
    public int[] getIds() {
        return new int[]{1, 2, 3};
    }

    @Override
    public long[] getLongIds() {
        return new long[]{100, 200, 300};
    }

    @Override
    public int[] getIds(int[] ids) {
        return ids;
    }

    @Override
    public User[] findUsers(User[] users) {
        return users;
    }

    @Override
    public User findUser(User user) {
        return user;
    }

    @Override
    public List<User> getList(List<User> userList) {
        return userList;
    }

    @Override
    public List<Boolean> getListBoolean(List<Boolean> userListBoolean) {
        return userListBoolean;
    }

    @Override
    public Map<String, User> getMap(Map<String, User> userMap) {
        return userMap;
    }

    @Override
    public boolean getFlag(boolean id) {
        return id;
    }

    @Override
    public boolean[] getFlag(boolean[] id) {
        return id;
    }

    @Override
    public User findById(long id) {
        return new User(Long.valueOf(id).intValue(), "tan");
    }

    @Override
    public User ex(boolean flag) {
        if (flag) {
            throw new TrpcException("just throw an exception");
        }
        return new User(100, "tt100");
    }

    @Override
    public User find(int timeout) {
        String port = environment.getProperty("server.port");
        if ("8081".equals(port)) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return new User(102, "tanTimeout-" + port);
    }


}
