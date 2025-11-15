package cn.zyroo.all.common.utils;

public class ApiResponse<T> {
  private String code;   // 状态码改为 String，兼容 ResponseCode
  private String message;
  private T data;

  public ApiResponse(String code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  // 成功返回
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>("200", "操作成功", data);
  }

  // 失败返回（自定义错误码）
  public static <T> ApiResponse<T> error(String code, String message) {
    return new ApiResponse<>(code, message, null);
  }

  // 失败返回（使用 ResponseCode）
  public static <T> ApiResponse<T> error(ResponseCode responseCode) {
    return new ApiResponse<>(responseCode.getCode(), responseCode.getMessage(), null);
  }

  // 失败返回（只有消息）
  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>("500", message, null);
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public T getData() {
    return data;
  }
}
