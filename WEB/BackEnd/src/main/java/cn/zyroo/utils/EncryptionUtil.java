package cn.zyroo.utils;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionUtil {

  private static final String SECRET_KEY = "Z8r4XyP1aBvD3eFgHjKlMnOpQrStUvWx";  // AES-256 密钥

  public static String encrypt(String data) {
    try {
      SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] encryptedData = cipher.doFinal(data.getBytes());
      return Base64.getEncoder().encodeToString(encryptedData);
    } catch (Exception e) {
      throw new RuntimeException("加密失败", e);
    }
  }

  public static String decrypt(String encryptedData) {
    try {
      SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] decodedData = Base64.getDecoder().decode(encryptedData);
      byte[] originalData = cipher.doFinal(decodedData);
      return new String(originalData);
    } catch (Exception e) {
      throw new RuntimeException("解密失败", e);
    }
  }
}
