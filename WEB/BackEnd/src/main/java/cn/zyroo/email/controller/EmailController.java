package cn.zyroo.email.controller;

import cn.zyroo.email.model.BulkEmail;
import cn.zyroo.email.repository.BulkEmailRepository;
import cn.zyroo.user.repository.UsersRepository;
import cn.zyroo.email.service.EmailService;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.user.utils.JwtUtil;
import cn.zyroo.common.utils.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import cn.hutool.json.JSONObject;
import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailController {

  private static final Logger log = LoggerFactory.getLogger(EmailController.class);
  @Autowired
  private EmailService emailService;

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private BulkEmailRepository bulkEmailRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Value("${spring.mail.username}")
  private String fromEmail;

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

  // 获取所有用户邮箱地址
  @GetMapping("/all-emails")
  public ApiResponse<List<String>> getAllEmails(@RequestHeader("Authorization") String token) {
    try {
      String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
      log.info("收到的token: {}", actualToken.substring(0, Math.min(10, actualToken.length())) + "...");

      try {
        String userRole = jwtUtil.getClaimFromToken(actualToken, "role");
        log.info("用户角色: {}", userRole);
        if (!"Admin".equals(userRole)) {
          return ApiResponse.error(ResponseCode.PERMISSION_DENIED);
        }
      } catch (Exception tokenEx) {
        log.error("Token解析失败: {}", tokenEx.getMessage());
        return ApiResponse.error("Token解析失败: " + tokenEx.getMessage());
      }

      List<String> emails = usersRepository.findAllEmails();
      log.info("获取到邮箱数量: {}", emails.size());
      for (String email : emails) {
        log.debug("邮箱: {}", email);
      }
      return ApiResponse.success(emails);
    } catch (Exception e) {
      log.error("获取邮箱列表失败: {}", e.getMessage(), e);
      return ApiResponse.error("获取邮箱列表失败: " + e.getMessage());
    }
  }

  // 群发邮件
  @PostMapping("/send-bulk")
  public ApiResponse<Object> sendBulkEmail(@RequestBody JSONObject jsonObject,
                                          @RequestHeader("Authorization") String token) {
    try {
      // 验证管理员权限 - 移除Bearer前缀
      String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
      String userRole = jwtUtil.getClaimFromToken(actualToken, "role");
      if (!"Admin".equals(userRole)) {
        return ApiResponse.error(ResponseCode.PERMISSION_DENIED);
      }

      String subject = jsonObject.getStr("subject");
      String content = jsonObject.getStr("content");

      if (subject == null || subject.trim().isEmpty()) {
        return ApiResponse.error("邮件主题不能为空");
      }
      if (content == null || content.trim().isEmpty()) {
        return ApiResponse.error("邮件内容不能为空");
      }

      // 获取所有用户邮箱
      List<String> emailList = usersRepository.findAllEmails();

      if (emailList.isEmpty()) {
        return ApiResponse.error("没有找到有效的邮箱地址");
      }

      // 发送群发邮件
      emailService.sendBulkEmail(emailList, subject, content);

      // 保存发送记录到数据库
      BulkEmail bulkEmail = new BulkEmail(subject, content, emailList.size(), fromEmail);
      bulkEmailRepository.save(bulkEmail);

      log.info("管理员发送群发邮件，主题: {}, 收件人数量: {}, 记录ID: {}",
               subject, emailList.size(), bulkEmail.getId());

      JSONObject result = new JSONObject();
      result.set("subject", subject);
      result.set("recipientCount", emailList.size());
      result.set("emailId", bulkEmail.getId());
      result.set("message", "邮件群发成功");

      return ApiResponse.success(result);
    } catch (Exception e) {
      log.error("群发邮件失败: {}", e.getMessage(), e);
      return ApiResponse.error("邮件发送失败: " + e.getMessage());
    }
  }

  // 获取历史邮件记录
  @GetMapping("/history")
  public ApiResponse<List<BulkEmail>> getEmailHistory(@RequestHeader("Authorization") String token,
                                                     @RequestParam(defaultValue = "20") int limit) {
    try {
      // 验证管理员权限 - 移除Bearer前缀
      String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
      String userRole = jwtUtil.getClaimFromToken(actualToken, "role");
      if (!"Admin".equals(userRole)) {
        return ApiResponse.error(ResponseCode.PERMISSION_DENIED);
      }

      // 获取历史邮件记录
      List<BulkEmail> emailHistory = bulkEmailRepository.findTopEmails(limit);

      return ApiResponse.success(emailHistory);
    } catch (Exception e) {
      log.error("获取邮件历史记录失败: {}", e.getMessage(), e);
      return ApiResponse.error(ResponseCode.FAILED);
    }
  }

  // 获取详细邮件记录
  @GetMapping("/history/{id}")
  public ApiResponse<BulkEmail> getEmailDetail(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id) {
    try {
      // 验证管理员权限 - 移除Bearer前缀
      String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
      String userRole = jwtUtil.getClaimFromToken(actualToken, "role");
      if (!"Admin".equals(userRole)) {
        return ApiResponse.error(ResponseCode.PERMISSION_DENIED);
      }

      // 获取邮件详情
      BulkEmail email = bulkEmailRepository.findById(id)
              .orElse(null);

      if (email == null) {
        return ApiResponse.error("邮件记录不存在");
      }

      return ApiResponse.success(email);
    } catch (Exception e) {
      log.error("获取邮件详情失败: {}", e.getMessage(), e);
      return ApiResponse.error(ResponseCode.FAILED);
    }
  }
}