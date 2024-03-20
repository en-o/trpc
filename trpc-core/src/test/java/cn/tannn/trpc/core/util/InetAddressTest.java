package cn.tannn.trpc.core.util;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/3/20 14:07
 */
public class InetAddressTest {

    @Test
    void getLocalHost() throws UnknownHostException {
        for (int i = 0; i < 2; i++) {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        }
    }
}
