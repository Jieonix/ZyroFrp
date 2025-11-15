package cn.zyroo.all.log.utils;

import cn.zyroo.all.log.model.OperationLog;
import cn.zyroo.all.common.utils.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志工具类
 * 提供日志记录相关的辅助功能
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Component
public class LogUtils {

    private static final Logger log = LoggerFactory.getLogger(LogUtils.class);

    private final ObjectMapper objectMapper;

    public LogUtils() {
        this.objectMapper = new ObjectMapper();
        // 注册Java 8时间模块，支持LocalDateTime等类型的序列化
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // 缓存方法信息，提高性能
    private final Map<String, Map<String, String>> methodCache = new ConcurrentHashMap<>();

    /**
     * 操作类型映射
     */
    private static final Map<String, String> OPERATION_MAPPING = new HashMap<>();
    static {
        OPERATION_MAPPING.put("login", "用户登录");
        OPERATION_MAPPING.put("register", "用户注册");
        OPERATION_MAPPING.put("logout", "用户登出");
        OPERATION_MAPPING.put("create", "创建操作");
        OPERATION_MAPPING.put("update", "更新操作");
        OPERATION_MAPPING.put("delete", "删除操作");
        OPERATION_MAPPING.put("query", "查询操作");
        OPERATION_MAPPING.put("send", "发送操作");
        OPERATION_MAPPING.put("reset", "重置操作");
        OPERATION_MAPPING.put("verify", "验证操作");
    }

    /**
     * 从请求中提取基本信息创建日志对象
     */
    public OperationLog createBasicLog(HttpServletRequest request, String logType, String operation, String module) {
        OperationLog operationLog = new OperationLog();
        operationLog.setLogType(logType);
        operationLog.setOperation(operation);
        operationLog.setModule(module);
        operationLog.setRequestMethod(request.getMethod());
        operationLog.setRequestUrl(request.getRequestURL().toString());
        operationLog.setUserAgent(request.getHeader("User-Agent"));
        operationLog.setSessionId(request.getSession().getId());

        // 生成追踪ID
        operationLog.setTraceId(generateTraceId());

        return operationLog;
    }

    /**
     * 从方法注解中提取操作信息
     */
    public Map<String, String> extractOperationInfo(Method method) {
        String cacheKey = method.getDeclaringClass().getName() + "." + method.getName();

        return methodCache.computeIfAbsent(cacheKey, k -> {
            Map<String, String> info = new HashMap<>();

            // 从方法名推断操作类型
            String methodName = method.getName().toLowerCase();
            String operation = inferOperationFromMethodName(methodName);
            String module = inferModuleFromClassName(method.getDeclaringClass().getSimpleName());

            info.put("operation", operation);
            info.put("module", module);

            return info; // 返回Map而不是toString
        });
    }

    /**
     * 从方法名推断操作类型
     */
    public String inferOperationFromMethodName(String methodName) {
        for (Map.Entry<String, String> entry : OPERATION_MAPPING.entrySet()) {
            if (methodName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // 默认操作类型
        if (methodName.startsWith("get") || methodName.startsWith("find") || methodName.startsWith("query")) {
            return "查询操作";
        } else if (methodName.startsWith("save") || methodName.startsWith("create")) {
            return "创建操作";
        } else if (methodName.startsWith("update") || methodName.startsWith("modify")) {
            return "更新操作";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "删除操作";
        } else {
            return "其他操作";
        }
    }

    /**
     * 从类名推断模块名称
     */
    public String inferModuleFromClassName(String className) {
        String lowerClassName = className.toLowerCase();

        if (lowerClassName.contains("auth")) {
            return "认证模块";
        } else if (lowerClassName.contains("user")) {
            return "用户模块";
        } else if (lowerClassName.contains("tunnel") || lowerClassName.contains("frp")) {
            return "隧道模块";
        } else if (lowerClassName.contains("email") || lowerClassName.contains("mail")) {
            return "邮件模块";
        } else if (lowerClassName.contains("server")) {
            return "服务器模块";
        } else if (lowerClassName.contains("admin")) {
            return "管理模块";
        } else if (lowerClassName.contains("announcement") || lowerClassName.contains("notice")) {
            return "公告模块";
        } else {
            return "其他模块";
        }
    }

    /**
     * 将对象序列化为JSON字符串
     */
    public String toJson(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("对象序列化失败: {}", obj, e);
            return obj.toString();
        }
    }

    /**
     * 将JSON字符串反序列化为对象
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.warn("JSON反序列化失败: {}", json, e);
            return null;
        }
    }

    /**
     * 生成唯一的追踪ID
     */
    public String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成会话ID
     */
    public String generateSessionId() {
        return "session_" + System.currentTimeMillis() + "_" + Thread.currentThread().getId();
    }

    /**
     * 计算执行时间
     */
    public long calculateExecutionTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * 判断是否为成功状态
     */
    public boolean isSuccessStatus(Object result) {
        if (result == null) {
            return false;
        }

        // 如果是ApiResponse类型，检查其状态
        if (result instanceof ApiResponse) {
            ApiResponse<?> response = (ApiResponse<?>) result;
            return "200".equals(response.getCode()); // 修复类型比较
        }

        // 其他类型的成功判断逻辑
        return true;
    }

    /**
     * 获取错误信息
     */
    public String getErrorMessage(Throwable throwable) {
        if (throwable == null) {
            return null;
        }

        String message = throwable.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = throwable.getClass().getSimpleName();
        }

        return message;
    }

    /**
     * 获取异常堆栈信息
     */
    public String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return null;
        }

        try {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            throwable.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
            log.warn("获取堆栈信息失败", e);
            return throwable.toString();
        }
    }

    /**
     * 脱敏处理请求参数
     */
    public String maskRequestParams(Map<String, String[]> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        Map<String, String> maskedParams = new HashMap<>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String[] values = entry.getValue();
            if (values != null && values.length > 0) {
                // 取第一个值进行脱敏处理
                String value = values[0];
                if (isSensitiveField(entry.getKey())) {
                    value = maskSensitiveValue(entry.getKey(), value);
                }
                maskedParams.put(entry.getKey(), value);
            }
        }

        return toJson(maskedParams);
    }

    /**
     * 判断是否为敏感字段
     */
    private boolean isSensitiveField(String fieldName) {
        String lowerFieldName = fieldName.toLowerCase();
        return lowerFieldName.contains("password") ||
               lowerFieldName.contains("pwd") ||
               lowerFieldName.contains("token") ||
               lowerFieldName.contains("secret") ||
               lowerFieldName.contains("key") ||
               lowerFieldName.contains("id_card") ||
               lowerFieldName.contains("phone") ||
               lowerFieldName.contains("email");
    }

    /**
     * 脱敏敏感值
     */
    private String maskSensitiveValue(String fieldName, String value) {
        if (value == null || value.length() <= 6) {
            return "******";
        }

        String lowerFieldName = fieldName.toLowerCase();
        if (lowerFieldName.contains("password") || lowerFieldName.contains("pwd") ||
            lowerFieldName.contains("secret")) {
            return "******";
        } else if (lowerFieldName.contains("token")) {
            return value.substring(0, Math.min(6, value.length())) + "***";
        } else if (lowerFieldName.contains("email")) {
            return maskEmail(value);
        } else if (lowerFieldName.contains("phone")) {
            return value.substring(0, 3) + "****" + value.substring(value.length() - 4);
        } else {
            return value.substring(0, 3) + "***" + value.substring(value.length() - 3);
        }
    }

    /**
     * 邮箱脱敏
     */
    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return "***" + email.substring(atIndex);
        }
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }

    /**
     * 创建登录日志
     */
    public OperationLog createLoginLog(HttpServletRequest request, String email, String status, String errorMessage) {
        OperationLog log = createBasicLog(request, OperationLog.LogType.LOGIN.name(),
                "用户登录", "认证模块");
        log.setUserEmail(email);
        log.setStatus(status);
        log.setErrorMessage(errorMessage);
        log.setDescription("用户尝试登录系统" + ("SUCCESS".equals(status) ? "并成功" : "但失败"));

        return log;
    }

    /**
     * 创建操作日志
     */
    public OperationLog createOperationLog(HttpServletRequest request, Long userId, String userEmail,
                                         String operation, String module, String description) {
        OperationLog log = createBasicLog(request, OperationLog.LogType.OPERATION.name(),
                operation, module);
        log.setUserId(userId);
        log.setUserEmail(userEmail);
        log.setDescription(description);

        return log;
    }

    /**
     * 创建错误日志
     */
    public OperationLog createErrorLog(HttpServletRequest request, String operation, String module,
                                     Throwable throwable) {
        OperationLog log = createBasicLog(request, OperationLog.LogType.ERROR.name(),
                operation, module);
        log.setStatus(OperationLog.Status.ERROR.name());
        log.setErrorMessage(getErrorMessage(throwable));
        log.setStackTrace(getStackTrace(throwable));
        log.setDescription("系统执行" + operation + "时发生错误");

        return log;
    }

    /**
     * 创建安全日志
     */
    public OperationLog createSecurityLog(HttpServletRequest request, String operation, String description) {
        OperationLog log = createBasicLog(request, OperationLog.LogType.SECURITY.name(),
                operation, "安全模块");
        log.setDescription(description);

        return log;
    }

    /**
     * 格式化时间
     */
    public String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toString();
    }

    /**
     * 清理缓存
     */
    public void clearCache() {
        methodCache.clear();
    }

    /**
     * 获取缓存大小
     */
    public int getCacheSize() {
        return methodCache.size();
    }
}