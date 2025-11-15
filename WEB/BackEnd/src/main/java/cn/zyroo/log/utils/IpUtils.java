package cn.zyroo.log.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * IP地址工具类
 * 提供IP地址解析、地理位置查询、用户代理解析等功能
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Component
public class IpUtils {

    // IP地址正则表达式
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    /**
     * 从HTTP请求中获取客户端真实IP地址
     */
    public String getClientIpAddress(HttpServletRequest request) {
        String ip = null;

        // 检查各种代理头信息
        String[] headers = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : headers) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                // X-Forwarded-For可能包含多个IP，取第一个
                if ("X-Forwarded-For".equalsIgnoreCase(header) && ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                break;
            }
        }

        // 如果没有找到，使用request.getRemoteAddr()
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理本地IPv6地址
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    /**
     * 验证IP地址是否有效
     */
    public boolean isValidIp(String ip) {
        if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }

        // 检查是否为IPv4地址
        if (IPV4_PATTERN.matcher(ip).matches()) {
            return true;
        }

        // 检查是否为IPv6地址
        if (IPV6_PATTERN.matcher(ip).matches()) {
            return true;
        }

        return false;
    }

    /**
     * 判断是否为内网IP地址
     */
    public boolean isInternalIp(String ip) {
        if (!isValidIp(ip)) {
            return false;
        }

        // IPv4内网地址范围
        String[] internalRanges = {
                "10.0.0.0", "10.255.255.255",     // 10.0.0.0/8
                "172.16.0.0", "172.31.255.255",   // 172.16.0.0/12
                "192.168.0.0", "192.168.255.255", // 192.168.0.0/16
                "127.0.0.0", "127.255.255.255"    // 127.0.0.0/8
        };

        try {
            long ipNum = ipToLong(ip);
            for (int i = 0; i < internalRanges.length; i += 2) {
                long start = ipToLong(internalRanges[i]);
                long end = ipToLong(internalRanges[i + 1]);
                if (ipNum >= start && ipNum <= end) {
                    return true;
                }
            }
        } catch (Exception e) {
            // 忽略转换异常
        }

        return false;
    }

    /**
     * IP地址转换为长整型
     */
    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result += Long.parseLong(parts[i]) << (8 * (3 - i));
        }
        return result;
    }

    /**
     * 根据IP地址获取地理位置信息
     * 这里使用简单的地理位置映射，实际项目中可以集成第三方IP地理位置服务
     */
    public String getLocationByIp(String ip) {
        if (!isValidIp(ip)) {
            return "未知位置";
        }

        // 如果是内网IP，直接返回
        if (isInternalIp(ip)) {
            return "内网地址";
        }

        // 这里可以集成第三方IP地理位置查询服务，如：
        // - 淘宝IP地址库
        // - 新浪IP地址库
        // - 百度IP定位服务
        // - GeoIP2等

        // 简单的地理位置映射示例
        Map<String, String> locationMap = new HashMap<>();
        locationMap.put("8.8.8.8", "美国谷歌");
        locationMap.put("114.114.114.114", "中国电信");
        locationMap.put("223.5.5.5", "中国阿里云");
        locationMap.put("119.29.29.29", "中国腾讯云");

        return locationMap.getOrDefault(ip, "未知位置");
    }

    /**
     * 解析用户代理字符串，提取浏览器和操作系统信息
     */
    public Map<String, String> parseUserAgent(String userAgent) {
        Map<String, String> result = new HashMap<>();

        if (!StringUtils.hasText(userAgent)) {
            result.put("browser", "未知浏览器");
            result.put("os", "未知操作系统");
            return result;
        }

        // 解析浏览器信息
        String browser = parseBrowser(userAgent);
        result.put("browser", browser);

        // 解析操作系统信息
        String os = parseOperatingSystem(userAgent);
        result.put("os", os);

        return result;
    }

    /**
     * 解析浏览器信息
     */
    private String parseBrowser(String userAgent) {
        String ua = userAgent.toLowerCase();

        if (ua.contains("edg/")) {
            return "Microsoft Edge";
        } else if (ua.contains("chrome/")) {
            if (ua.contains("edg/")) {
                return "Microsoft Edge";
            }
            return "Google Chrome";
        } else if (ua.contains("firefox/")) {
            return "Mozilla Firefox";
        } else if (ua.contains("safari/") && !ua.contains("chrome")) {
            return "Safari";
        } else if (ua.contains("opera/") || ua.contains("opr/")) {
            return "Opera";
        } else if (ua.contains("msie") || ua.contains("trident")) {
            return "Internet Explorer";
        } else if (ua.contains("micromessenger")) {
            return "微信浏览器";
        } else if (ua.contains("qqbrowser")) {
            return "QQ浏览器";
        } else if (ua.contains("ucbrowser")) {
            return "UC浏览器";
        } else if (ua.contains("baiduboxapp")) {
            return "百度浏览器";
        } else if (ua.contains("sogoumobilebrowser")) {
            return "搜狗浏览器";
        } else {
            return "未知浏览器";
        }
    }

    /**
     * 解析操作系统信息
     */
    private String parseOperatingSystem(String userAgent) {
        String ua = userAgent.toLowerCase();

        if (ua.contains("windows nt 10")) {
            return "Windows 10";
        } else if (ua.contains("windows nt 6.3")) {
            return "Windows 8.1";
        } else if (ua.contains("windows nt 6.2")) {
            return "Windows 8";
        } else if (ua.contains("windows nt 6.1")) {
            return "Windows 7";
        } else if (ua.contains("windows nt 6.0")) {
            return "Windows Vista";
        } else if (ua.contains("windows nt 5.1") || ua.contains("windows xp")) {
            return "Windows XP";
        } else if (ua.contains("windows")) {
            return "Windows";
        } else if (ua.contains("android")) {
            return "Android";
        } else if (ua.contains("iphone") || ua.contains("ipad")) {
            return "iOS";
        } else if (ua.contains("mac os x")) {
            return "macOS";
        } else if (ua.contains("linux")) {
            if (ua.contains("ubuntu")) {
                return "Ubuntu";
            } else if (ua.contains("debian")) {
                return "Debian";
            } else if (ua.contains("centos")) {
                return "CentOS";
            } else {
                return "Linux";
            }
        } else if (ua.contains("unix")) {
            return "Unix";
        } else {
            return "未知操作系统";
        }
    }

    /**
     * 获取IP地址的整数表示形式（用于存储和比较）
     */
    public Long ipToNumeric(String ip) {
        if (!isValidIp(ip)) {
            return null;
        }

        try {
            String[] parts = ip.split("\\.");
            long result = 0;
            for (int i = 0; i < 4; i++) {
                result += Long.parseLong(parts[i]) << (8 * (3 - i));
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将数字IP地址转换回字符串格式
     */
    public String numericToIp(Long ipNumeric) {
        if (ipNumeric == null) {
            return null;
        }

        long ip = ipNumeric;
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xFF,
                (ip >> 16) & 0xFF,
                (ip >> 8) & 0xFF,
                ip & 0xFF);
    }

    /**
     * 检查IP地址是否在指定的CIDR范围内
     */
    public boolean isIpInCidrRange(String ip, String cidr) {
        try {
            String[] parts = cidr.split("/");
            String networkIp = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);

            long ipLong = ipToLong(ip);
            long networkLong = ipToLong(networkIp);
            long mask = (0xFFFFFFFFL << (32 - prefixLength)) & 0xFFFFFFFFL;

            return (ipLong & mask) == (networkLong & mask);
        } catch (Exception e) {
            return false;
        }
    }
}