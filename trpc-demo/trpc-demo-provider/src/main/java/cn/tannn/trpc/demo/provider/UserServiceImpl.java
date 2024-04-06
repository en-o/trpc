package cn.tannn.trpc.demo.provider;

import cn.tannn.trpc.core.annotation.TProvider;
import cn.tannn.trpc.core.api.RpcContext;
import cn.tannn.trpc.core.exception.TrpcException;
import cn.tannn.trpc.demo.api.UserService;
import cn.tannn.trpc.demo.api.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     *  environment
     */
    @Autowired
    Environment environment;

    @Override
    public User findById(int id) {
        return new User(id, "t-"
                + environment.getProperty("server.port")
                + "_" + System.currentTimeMillis());
    }

    @Override
    public User findById(int id, String name) {
        return new User(id, "t-" + name + "_" + System.currentTimeMillis());
    }

    @Override
    public long getId(long id) {
        return id;
    }

    @Override
    public long getId(User user) {
        return user.getId().longValue();
    }

    @Override
    public long getId(float id) {
        return 1L;
    }

    @Override
    public String getName() {
        return "t123";
    }

    @Override
    public String getName(int id) {
        return "t-" + id;
    }

    @Override
    public int[] getIds() {
        return new int[] {100,200,300};
    }

    @Override
    public long[] getLongIds() {
        return new long[]{1,2,3};
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
    public List<User> getList(List<User> userList) {
        User[] users = userList.toArray(new User[0]);
        System.out.println(" ==> userList.toArray()[] = ");
        Arrays.stream(users).forEach(System.out::println);
        userList.add(new User(2024,"t2024"));
        return userList;
    }

    @Override
    public Map<String, User> getMap(Map<String, User> userMap) {
        userMap.values().forEach(x -> System.out.println(x.getClass()));
        User[] users = userMap.values().toArray(new User[userMap.size()]);
        System.out.println(" ==> userMap.values().toArray()[] = ");
        Arrays.stream(users).forEach(System.out::println);
        userMap.put("A2024", new User(2024,"t2024"));
        return userMap;
    }

    @Override
    public Boolean getFlag(boolean flag) {
        return !flag;
    }

    @Override
    public User findById(long id) {
        return new User(Long.valueOf(id).intValue(), "t");
    }

    @Override
    public User ex(boolean flag) {
        if(flag) {
            throw new TrpcException("just throw an exception");
        }
        return new User(100, "t100");
    }

    String timeoutPorts = "8081,8094";

    @Override
    public User find(int timeout) {
        String port = environment.getProperty("server.port");
        if(Arrays.stream(timeoutPorts.split(",")).anyMatch(port::equals)) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return new User(1001, "t1001-" + port);
    }

    public void setTimeoutPorts(String timeoutPorts) {
        this.timeoutPorts = timeoutPorts;
    }

    @Override
    public String echoParameter(String key) {
        System.out.println(" ====>> RpcContext.ContextParameters: ");
        RpcContext.ContextParameters.get().forEach((k, v)-> System.out.println(k+" -> " +v));
        return RpcContext.getContextParameter(key);
    }
}
