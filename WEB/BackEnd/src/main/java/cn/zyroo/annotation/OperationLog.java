package cn.zyroo.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作类型
     */
    String operation() default "";

    /**
     * 模块名称
     */
    String module() default "";

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 是否记录请求参数
     */
    boolean recordParams() default true;

    /**
     * 是否记录响应结果
     */
    boolean recordResult() default true;

    /**
     * 是否记录敏感数据
     */
    boolean recordSensitiveData() default false;

    /**
     * 日志类型
     */
    LogType logType() default LogType.OPERATION;

    /**
     * 日志类型枚举
     */
    enum LogType {
        LOGIN,      // 登录日志
        OPERATION,  // 操作日志
        ERROR,      // 错误日志
        SECURITY    // 安全日志
    }
}