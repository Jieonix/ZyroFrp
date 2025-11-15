package cn.zyroo.common.exception;

import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.common.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResponse<?>> handleBizException(BizException e) {
        log.warn("业务异常: {}", e.getMessage());
        return ResponseEntity.ok(ApiResponse.error(ResponseCode.BUSINESS_ERROR.getCode(), e.getMessage()));
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("参数校验异常: {}", e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        return ResponseEntity.ok(ApiResponse.error(ResponseCode.PARAM_VALIDATION_ERROR.getCode(), message));
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<?>> handleBindException(BindException e) {
        log.warn("参数绑定异常: {}", e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        return ResponseEntity.ok(ApiResponse.error(ResponseCode.PARAM_VALIDATION_ERROR.getCode(), message));
    }

    /**
     * 处理参数校验异常（ConstraintViolationException）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("参数校验异常: {}", e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String message = violations.isEmpty() ? "参数校验失败" : violations.iterator().next().getMessage();
        return ResponseEntity.ok(ApiResponse.error(ResponseCode.PARAM_VALIDATION_ERROR.getCode(), message));
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(ResponseCode.METHOD_NOT_ALLOWED));
    }

    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("请求参数缺失: {}", e.getMessage());
        return ResponseEntity.ok(ApiResponse.error(ResponseCode.PARAM_MISSING));
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: {}", e.getMessage());
        return ResponseEntity.ok(ApiResponse.error(ResponseCode.PARAM_TYPE_ERROR));
    }

    /**
     * 处理请求体解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求体解析异常: {}", e.getMessage());
        return ResponseEntity.ok(ApiResponse.error(ResponseCode.PARAM_INVALID));
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("接口不存在: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ResponseCode.NOT_FOUND));
    }

    /**
     * 处理其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("系统异常: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ResponseCode.SYSTEM_ERROR));
    }
}
