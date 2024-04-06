package cn.tannn.trpc.core.util;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * ip
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/4/6 下午8:41
 */
@Slf4j
public class IpUtils {
    /**
     * 获取本地真正的IP地址，即获得有线或者无线WiFi地址。
     * 过滤虚拟机、蓝牙等地址
     *
     * @return java.lang.String
     * @author tn
     * @date 2020/4/21 23:44
     */
    public static String getRealIp() {
       try {
           Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                   .getNetworkInterfaces();
           while (allNetInterfaces.hasMoreElements()) {
               NetworkInterface netInterface = allNetInterfaces
                       .nextElement();

               // 去除回环接口，子接口，未运行和接口
               if (netInterface.isLoopback() || netInterface.isVirtual()
                       || !netInterface.isUp()) {
                   continue;
               }

               if (!netInterface.getDisplayName().contains("Intel")
                       && !netInterface.getDisplayName().contains("Realtek")) {
                   continue;
               }
               Enumeration<InetAddress> addresses = netInterface
                       .getInetAddresses();
               while (addresses.hasMoreElements()) {
                   InetAddress ip = addresses.nextElement();
                   // ipv4
                   if (ip instanceof Inet4Address) {
                       return ip.getHostAddress();
                   }
               }
               break;
           }
       }catch (Exception e){
          log.error("本机IP获取失败");
       }
        return "127.0.0.1";
    }
}
