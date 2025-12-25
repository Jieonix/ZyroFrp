package cn.zyroo.log.aspect;

import cn.zyroo.log.model.OperationLog;
import cn.zyroo.user.model.Users;
import cn.zyroo.log.service.LogService;
import cn.zyroo.common.service.UserContextService;
import cn.zyroo.common.dto.UserInfo;
import cn.zyroo.log.utils.IpUtils;
import cn.zyroo.log.utils.LogUtils;
import cn.zyroo.log.utils.SensitiveDataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 日志记录切面类
 * 通过AOP方式自动记录系统操作日志
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private final LogService logService;
    private final LogUtils logUtils;
    private final IpUtils ipUtils;
    private final SensitiveDataUtils sensitiveDataUtils;
    private final UserContextService userContextService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoggingAspect(LogService logService, LogUtils logUtils, IpUtils ipUtils,
                        SensitiveDataUtils sensitiveDataUtils, UserContextService userContextService) {
        this.logService = logService;
        this.logUtils = logUtils;
        this.ipUtils = ipUtils;
        this.sensitiveDataUtils = sensitiveDataUtils;
        this.userContextService = userContextService;
    }

    /**
     * 定义切点：Controller层所有方法
     */
    @Pointcut("execution(* cn.zyroo..controller..*(..))")
    public void controllerPointcut() {}

    /**
     * 定义切点：Service层所有方法
     */
    @Pointcut("execution(* cn.zyroo..service..*(..))")
    public void servicePointcut() {}

    /**
     * 定义切点：带有操作日志注解的方法
     */
    @Pointcut("@annotation(cn.zyroo.log.annotation.OperationLog)")
    public void operationLogPointcut() {}

    /**
     * 环绕通知：记录Controller层操作日志
     */
    @Around("controllerPointcut()")
    public Object logControllerOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = ipUtils.getClientIpAddress(request);

        // 创建基础日志对象
        OperationLog operationLog = logUtils.createBasicLog(
                request,
                OperationLog.LogType.OPERATION.name(),
                getOperationName(joinPoint),
                getModuleName(joinPoint)
        );

        // 设置用户信息
        setUserInfo(operationLog, request);

        // 设置IP地址
        operationLog.setIpAddress(ipAddress);

        // 记录请求参数
        try {
            String requestParams = getRequestParams(joinPoint);
            operationLog.setRequestParams(requestParams);
        } catch (Exception e) {
            log.warn("记录请求参数失败", e);
        }

        Object result = null;
        Exception exception = null;

        try {
            // 执行目标方法
            result = joinPoint.proceed();

            // 设置成功状态
            operationLog.setStatus(OperationLog.Status.SUCCESS.name());
            operationLog.setDescription("执行" + operationLog.getOperation() + "成功");

        } catch (Exception e) {
            exception = e;

            // 设置失败状态
            operationLog.setStatus(OperationLog.Status.ERROR.name());
            operationLog.setErrorMessage(e.getMessage());
            operationLog.setDescription("执行" + operationLog.getOperation() + "失败");

            // 记录异常堆栈
            String stackTrace = logUtils.getStackTrace(e);
            operationLog.setStackTrace(stackTrace);

            throw e;
        } finally {
            // 计算执行时间
            long executionTime = logUtils.calculateExecutionTime(startTime);
            operationLog.setExecutionTime(executionTime);

            // 记录响应数据（脱敏处理）
            if (result != null) {
                try {
                    String responseData = logUtils.toJson(result);
                    operationLog.setResponseData(responseData);
                } catch (Exception e) {
                    log.warn("记录响应数据失败", e);
                }
            }

            // 异步保存日志
            logService.logOperation(operationLog);
        }

        return result;
    }

    /**
     * 异常通知：记录Service层异常
     */
    @AfterThrowing(pointcut = "servicePointcut()", throwing = "ex")
    public void logServiceException(JoinPoint joinPoint, Exception ex) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            String ipAddress = ipUtils.getClientIpAddress(request);

            // 创建错误日志
            OperationLog operationLog = logUtils.createErrorLog(
                    request,
                    getOperationName(joinPoint),
                    getModuleName(joinPoint),
                    ex
            );

            // 设置IP地址
            operationLog.setIpAddress(ipAddress);

            // 设置用户信息
            setUserInfo(operationLog, request);

            // 异步保存日志
            logService.logOperation(operationLog);

        } catch (Exception e) {
            log.error("记录服务层异常日志失败", e);
        }
    }

    /**
     * 后置通知：记录操作成功日志
     */
    @AfterReturning(pointcut = "operationLogPointcut()", returning = "result")
    public void logOperationSuccess(JoinPoint joinPoint, Object result) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return;
            }

            HttpServletRequest request = attributes.getRequest();
            String ipAddress = ipUtils.getClientIpAddress(request);

            // 创建操作日志
            OperationLog operationLog = logUtils.createOperationLog(
                    request,
                    null,
                    null,
                    getOperationName(joinPoint),
                    getModuleName(joinPoint),
                    "操作执行成功"
            );

            // 设置IP地址
            operationLog.setIpAddress(ipAddress);

            // 设置用户信息
            setUserInfo(operationLog, request);

            // 设置成功状态
            operationLog.setStatus(OperationLog.Status.SUCCESS.name());

            // 记录响应数据
            if (result != null) {
                try {
                    String responseData = logUtils.toJson(result);
                    operationLog.setResponseData(responseData);
                } catch (Exception e) {
                    log.warn("记录响应数据失败", e);
                }
            }

            // 异步保存日志
            logService.logOperation(operationLog);

        } catch (Exception e) {
            log.error("记录操作成功日志失败", e);
        }
    }

    /**
     * 获取操作名称
     */
    private String getOperationName(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        return logUtils.inferOperationFromMethodName(methodName);
    }

    /**
     * 获取模块名称
     */
    private String getModuleName(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        return logUtils.inferModuleFromClassName(className);
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(OperationLog operationLog, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                // 从JWT Token中获取用户信息
                String email = userContextService.getEmailFromToken(token);
                if (email != null) {
                    operationLog.setUserEmail(email);
                    // 通过用户服务获取用户ID等信息
                    UserInfo userInfo = userContextService.findUserByEmail(email);
                    if (userInfo != null) {
                        operationLog.setUserId(userInfo.getUserId());
                        operationLog.setUsername(userInfo.getEmail());
                    }
                }
            }
        } catch (Exception e) {
            log.debug("解析用户信息失败", e);
        }
    }

    /**
     * 获取请求参数（脱敏处理）
     */
    private String getRequestParams(JoinPoint joinPoint) throws Exception {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }

        // 对于Controller方法，通常第一个参数是请求相关的
        // 这里需要根据实际情况进行参数过滤和脱敏
        for (Object arg : args) {
            if (arg != null) {
                // 过滤掉不需要记录的参数类型
                if (arg instanceof HttpServletRequest ||
                    arg instanceof jakarta.servlet.http.HttpServletResponse) {
                    continue;
                }

                try {
                    String jsonStr = logUtils.toJson(arg);
                    if (jsonStr != null && jsonStr.length() > 10000) { // 限制参数长度
                        jsonStr = jsonStr.substring(0, 10000) + "...[参数过长已截断]";
                    }
                    return jsonStr;
                } catch (Exception e) {
                    // 如果无法序列化，返回对象的字符串表示
                    String str = arg.toString();
                    if (str.length() > 1000) {
                        str = str.substring(0, 1000) + "...[参数过长已截断]";
                    }
                    return str;
                }
            }
        }

        return null;
    }
}
