package com.springboot.whb.study.common.util;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: whb
 * @date: 2019/7/12 9:38
 * @description: IP工具类
 */
public class IPUtils {

    private static final Logger logger = LoggerFactory.getLogger(IPUtils.class);

    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件，则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值不止一个而是一串IP地址，
     * 则X-Forwarded-For第一个非unknown的有效IP字符串则为真实IP地址。
     *
     * @param request
     * @return
     */
    public static String getIPAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (com.alibaba.druid.util.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (com.alibaba.druid.util.StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (com.alibaba.druid.util.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (com.alibaba.druid.util.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            logger.error("IPUtils ERROR ", e);
        }

//        //使用代理，则获取第一个IP地址
//        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
//			if(ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}

        return ip;
    }

    /**
     * 判断是否为windows操作系统
     *
     * @return
     */
    public static boolean isWindows() {
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            return true;
        }
        return false;
    }

    /**
     * 获取本机Ip地址（如果本机vmnet1跟vmnet8都是启用状态，则优先获取vmnet1或者vmnet8的地址）
     *
     * @return
     * @throws UnknownHostException
     * @throws SocketException
     */
    public static String getLocalIP() throws UnknownHostException, SocketException {
        if (isWindows()) {
            InetAddress address = Inet4Address.getLocalHost();
            String serviceIp = address.getHostAddress();
            return serviceIp;
        }
        return getLinuxLocalIP();
    }

    /**
     * 获取本地主机名
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalHostName() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostName();
    }

    /**
     * 获取本地Linux系统Ip地址
     *
     * @return
     * @throws SocketException
     */
    public static String getLinuxLocalIP() throws SocketException {
        String ip = "";
        for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
            NetworkInterface networkInterface = (NetworkInterface) en.nextElement();
            String name = networkInterface.getName();
            if (!name.contains("docker") && !name.contains("lo")) {
                for (Enumeration enumeration = networkInterface.getInetAddresses(); enumeration.hasMoreElements(); ) {
                    InetAddress address = (InetAddress) enumeration.nextElement();
                    if (!address.isLoopbackAddress()) {
                        String ipAddress = address.getHostAddress().trim();
                        if (!ipAddress.contains("::") && !ipAddress.contains("0:0:") && !ipAddress.contains("fe80")) {
                            ip = ipAddress;
                        }
                    }
                }
            }
        }
        return ip;
    }

    /**
     * 获取端口号
     *
     * @return
     * @throws AttributeNotFoundException
     * @throws InstanceNotFoundException
     * @throws MBeanException
     * @throws ReflectionException
     */
    public static String getServerPort() throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
        MBeanServer mBeanServer = null;
        if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
            mBeanServer = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
        }

        if (mBeanServer == null) {
            logger.error("调用findMBeanServer查询到的结果为null");
            return "";
        }
        Set names = null;
        try {
            names = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
        } catch (Exception e) {
            return "";
        }
        Iterator ite = names.iterator();
        ObjectName oname = null;
        while (ite.hasNext()) {
            oname = (ObjectName) ite.next();
            String protocol = mBeanServer.getAttribute(oname, "protocol").toString();
            String scheme = mBeanServer.getAttribute(oname, "scheme").toString();
            Boolean secureValue = (Boolean) mBeanServer.getAttribute(oname, "secure");
            Boolean SSLEnabled = (Boolean) mBeanServer.getAttribute(oname, "SSLEnabled");
            if (SSLEnabled != null && SSLEnabled.booleanValue()) {
                secureValue = Boolean.TRUE;
                scheme = "https";
            }
            if ((protocol != null) && (("HTTP/1.1".equals(protocol)) || (protocol.contains("http")))) {
                if ("https".equals(scheme) && secureValue.booleanValue()) {
                    return mBeanServer.getAttribute(oname, "port").toString();
                }
                if (!"https".equals(scheme) && !secureValue.booleanValue()) {
                    return mBeanServer.getAttribute(oname, "port").toString();
                }
            }
        }
        return "";
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getLocalHostName());
        System.out.println(getServerPort());
    }
}
