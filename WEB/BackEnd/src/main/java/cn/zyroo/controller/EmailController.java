package cn.zyroo.controller;

import cn.zyroo.service.EmailService;
import cn.zyroo.utils.ApiResponse;
import cn.zyroo.utils.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.hutool.json.JSONObject;

@RestController
@RequestMapping("/emails")
public class EmailController {

  private static final Logger log = LoggerFactory.getLogger(EmailController.class);
  @Autowired
  private EmailService emailService;

  @PostMapping("/send-verification")
  public ApiResponse<Object> sendVerificationCode(@RequestBody JSONObject jsonObject) {
    String email = jsonObject.getStr("toEmail");
    String type = jsonObject.getStr("type");

    try {
      String code = emailService.sendEmailCode(email, type);
      return ApiResponse.success("验证码发送成功");
    } catch (Exception e) {
      log.error("发送验证码失败: {}", e.getMessage(), e);
      return ApiResponse.error(ResponseCode.EMAIL_SEND_FAILED);
    }
  }
}