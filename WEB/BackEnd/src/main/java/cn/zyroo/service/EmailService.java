package cn.zyroo.service;

import cn.zyroo.model.Email;
import cn.zyroo.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
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
 private void sendEmail(String toEmail, String code, String type) throws MailException {
  SimpleMailMessage message = new SimpleMailMessage();
  message.setFrom(fromEmail);
  message.setTo(toEmail);
  String subject = emailTypeMap.get(type);

  if (subject == null) {
   throw new IllegalArgumentException("未知的邮件类型: " + type);
  }

  message.setSubject("验证码 - " + subject);
  message.setText("您的验证码为： " + code + "\n请不要泄露给他人，验证码有效期为 5 分钟。");

  mailSender.send(message);
 }

 // 生成4位验证码逻辑
 private String generateRandomCode() {
  Random random = new Random();
  int randomCode = 1000 + random.nextInt(9000);
  return String.valueOf(randomCode);
 }


 @Scheduled(fixedRate = 1000 * 60 * 1)
 public void expireOldCodes() {
  emailRepository.expireExpiredCodesByType("REGISTER");
  emailRepository.expireExpiredCodesByType("RESET_PASSWORD");
 }

}
