package cn.zyroo.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
  public static void main(String[] args) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String rawPassword = "Shajie@123"; // 用你想要的新密码替换这里
    String hashedPassword = encoder.encode(rawPassword);

    System.out.println("Encrypted Password: " + hashedPassword);
  }
}
