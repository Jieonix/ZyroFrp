package cn.zyroo.service;

import cn.zyroo.model.Email;
import cn.zyroo.repository.EmailRepository;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

 @Autowired
 private JavaMailSender mailSender;

 @Autowired
 private EmailRepository emailRepository;

 @Value("${spring.mail.username}")
 private String fromEmail;

 private static final Map<String, String> emailTypeMap = new HashMap<>();

 static {
  emailTypeMap.put("REGISTER", "注册");
  emailTypeMap.put("RESET_PASSWORD", "重置密码");
 }

 // 发送验证码和处理过期验证码逻辑
 public String sendEmailCode(String email, String type) throws MailException {
  emailRepository.updateStatusByEmailAndType(email, "0", "-2", type);
  String verificationCode = generateRandomCode();

  Email verificationCodes = new Email();
  verificationCodes.setEmail(email);
  verificationCodes.setCode(verificationCode);
  verificationCodes.setType(type);
  verificationCodes.setCreatedAt(LocalDateTime.now());
  verificationCodes.setExpiredAt(LocalDateTime.now().plusMinutes(5));
  verificationCodes.setStatus("0");

  emailRepository.save(verificationCodes);
  sendEmail(email, verificationCode, type);

  return verificationCode;
 }

 // 邮件格式
 private void sendEmail(String toEmail, String code, String type) {
  try {
   MimeMessage message = mailSender.createMimeMessage();
   MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

   String subject = emailTypeMap.get(type);
   if (subject == null) {
    throw new IllegalArgumentException("未知的邮件类型: " + type);
   }

   helper.setTo(toEmail);
   helper.setSubject("验证码 - " + subject);
   helper.setText("您的验证码为： " + code + "\n请不要泄露给他人，验证码有效期为 5 分钟。");
   helper.setFrom(new InternetAddress(fromEmail, "ZyroFrp 内网穿透服务平台", "UTF-8"));

   mailSender.send(message);
  } catch (Exception e) {
   e.printStackTrace();
   throw new RuntimeException("邮件发送失败：" + e.getMessage());
  }
 }


 // 生成4位验证码逻辑
 private String generateRandomCode() {
  Random random = new Random();
  int randomCode = 1000 + random.nextInt(9000);
  return String.valueOf(randomCode);
 }


 // 批量发送邮件给所有用户
 // 批量发送邮件给所有用户
 public void sendBulkEmail(List<String> emailList, String subject, String content) {
  try {
   MimeMessage message = mailSender.createMimeMessage();
   MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

   // 转换为字符串数组
   String[] toEmails = emailList.toArray(new String[0]);
   helper.setTo(toEmails);
   helper.setSubject(subject);
   helper.setText(content, false); // false 表示纯文本邮件；true 可支持 HTML
   helper.setFrom(new InternetAddress(fromEmail, "ZyroFrp 内网穿透服务平台", "UTF-8"));

   mailSender.send(message);
  } catch (Exception e) {
   e.printStackTrace();
   throw new RuntimeException("群发邮件失败：" + e.getMessage());
  }
 }
}
