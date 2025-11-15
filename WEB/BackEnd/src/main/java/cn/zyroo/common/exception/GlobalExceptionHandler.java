package cn.zyroo.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BizException.class)
  public Map<String, Object> handleBiz(BizException e) {
    Map<String, Object> map = new HashMap<>();
    map.put("code", 400);
    map.put("msg", e.getMessage());
    return map;
  }
}
