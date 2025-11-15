package cn.zyroo.log.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 敏感数据工具类
 * 提供敏感数据脱敏、加密解密等功能
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Component
public class SensitiveDataUtils {

    private static final Logger log = LoggerFactory.getLogger(SensitiveDataUtils.class);

    // AES加密相关配置
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final int AES_KEY_SIZE = 256;

    // 敏感数据脱敏正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})");
    private static final Pattern PHONE_PATTERN = Pattern.compile("(1[3-9]\\d)(\\d{4})(\\d{4})");
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("(\\d{6})(\\d{8})(\\d{4})");
    private static final Pattern BANK_CARD_PATTERN = Pattern.compile("(\\d{4})(\\d+)(\\d{4})");

    // 默认加密密钥（生产环境应该从安全配置中获取）
    @Value("${app.security.encryption.key:ZyroFrp2025SecretKeyForEncryption!}")
    private String defaultEncryptionKey;

    // 当前使用的密钥ID
    private String currentKeyId = "default";

    // 密钥缓存
    private Map<String, SecretKey> keyCache = new HashMap<>();

    /**
     * 敏感数据脱敏处理
     */
    public String maskSensitiveData(String data, String dataType) {
        if (data == null || data.trim().isEmpty()) {
            return data;
        }

        try {
            switch (dataType.toUpperCase()) {
                case "EMAIL":
                    return maskEmail(data);
                case "PHONE":
                case "MOBILE":
                    return maskPhone(data);
                case "ID_CARD":
                    return maskIdCard(data);
                case "BANK_CARD":
                    return maskBankCard(data);
                case "PASSWORD":
                case "SECRET":
                case "PRIVATE_KEY":
                    return maskComplete(data);
                case "TOKEN":
                    return maskToken(data);
                case "API_KEY":
                    return maskApiKey(data);
                default:
                    return maskPartial(data);
            }
        } catch (Exception e) {
            log.error("数据脱敏失败: dataType={}, data={}", dataType, data, e);
            return "脱敏失败";
        }
    }

    /**
     * 邮箱脱敏
     */
    private String maskEmail(String email) {
        if (email == null || email.length() < 6) {
            return email;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email;
        }

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        // 用户名只显示前2位和最后1位，中间用*替代
        if (username.length() <= 3) {
            return username.charAt(0) + "***" + domain;
        } else {
            return username.substring(0, 2) + "***" + username.charAt(username.length() - 1) + domain;
        }
    }

    /**
     * 手机号脱敏
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 身份证脱敏
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 4) + "**********" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 银行卡脱敏
     */
    private String maskBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + " **** **** " + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 完全脱敏（密码、密钥等）
     */
    private String maskComplete(String data) {
        if (data == null) {
            return null;
        }
        return "******";
    }

    /**
     * Token脱敏
     */
    private String maskToken(String token) {
        if (token == null || token.length() < 12) {
            return "******";
        }
        return token.substring(0, 6) + "***" + token.substring(token.length() - 3);
    }

    /**
     * API Key脱敏
     */
    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() < 8) {
            return "******";
        }
        return apiKey.substring(0, 4) + "***" + apiKey.substring(apiKey.length() - 4);
    }

    /**
     * 部分脱敏
     */
    private String maskPartial(String data) {
        if (data == null) {
            return null;
        }
        if (data.length() <= 6) {
            return "******";
        }
        return data.substring(0, 3) + "***" + data.substring(data.length() - 3);
    }

    /**
     * 加密敏感数据
     */
    public String encrypt(String plainText) {
        return encrypt(plainText, currentKeyId);
    }

    /**
     * 使用指定密钥加密敏感数据
     */
    public String encrypt(String plainText, String keyId) {
        try {
            if (plainText == null || plainText.trim().isEmpty()) {
                return plainText;
            }

            SecretKey secretKey = getOrCreateKey(keyId);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("数据加密失败", e);
            throw new RuntimeException("数据加密失败", e);
        }
    }

    /**
     * 解密敏感数据
     */
    public String decrypt(String encryptedText) {
        return decrypt(encryptedText, currentKeyId);
    }

    /**
     * 使用指定密钥解密敏感数据
     */
    public String decrypt(String encryptedText, String keyId) {
        try {
            if (encryptedText == null || encryptedText.trim().isEmpty()) {
                return encryptedText;
            }

            SecretKey secretKey = getOrCreateKey(keyId);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("数据解密失败", e);
            throw new RuntimeException("数据解密失败", e);
        }
    }

    /**
     * 获取或创建密钥
     */
    private SecretKey getOrCreateKey(String keyId) {
        return keyCache.computeIfAbsent(keyId, this::generateKey);
    }

    /**
     * 生成密钥
     */
    private SecretKey generateKey(String keyId) {
        try {
            // 对于默认密钥，使用配置的密钥字符串
            if ("default".equals(keyId) && defaultEncryptionKey != null) {
                byte[] keyBytes = defaultEncryptionKey.getBytes(StandardCharsets.UTF_8);
                // 确保密钥长度为32字节（256位）
                byte[] key = new byte[32];
                System.arraycopy(keyBytes, 0, key, 0, Math.min(keyBytes.length, 32));
                return new SecretKeySpec(key, AES_ALGORITHM);
            }

            // 生成新的随机密钥
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(AES_KEY_SIZE, new SecureRandom());
            return keyGenerator.generateKey();
        } catch (Exception e) {
            log.error("生成密钥失败: keyId={}", keyId, e);
            throw new RuntimeException("生成密钥失败", e);
        }
    }

    /**
     * 检测数据类型
     */
    public String detectDataType(String fieldName, String value) {
        if (value == null) {
            return "TEXT";
        }

        String lowerFieldName = fieldName.toLowerCase();
        String lowerValue = value.toLowerCase();

        // 根据字段名判断
        if (lowerFieldName.contains("password") || lowerFieldName.contains("pwd") ||
            lowerFieldName.contains("secret") || lowerFieldName.contains("private_key")) {
            return "PASSWORD";
        } else if (lowerFieldName.contains("token")) {
            return "TOKEN";
        } else if (lowerFieldName.contains("api_key") || lowerFieldName.contains("apikey")) {
            return "API_KEY";
        } else if (lowerFieldName.contains("email")) {
            return "EMAIL";
        } else if (lowerFieldName.contains("phone") || lowerFieldName.contains("mobile")) {
            return "PHONE";
        } else if (lowerFieldName.contains("id_card") || lowerFieldName.contains("idcard")) {
            return "ID_CARD";
        } else if (lowerFieldName.contains("bank") || lowerFieldName.contains("card")) {
            return "BANK_CARD";
        }

        // 根据值的内容判断
        if (EMAIL_PATTERN.matcher(value).matches()) {
            return "EMAIL";
        } else if (PHONE_PATTERN.matcher(value).matches()) {
            return "PHONE";
        } else if (ID_CARD_PATTERN.matcher(value).matches()) {
            return "ID_CARD";
        } else if (value.length() > 20 && (lowerValue.contains("token") || lowerValue.contains("jwt"))) {
            return "TOKEN";
        } else if (value.length() > 10 && lowerFieldName.contains("key")) {
            return "API_KEY";
        }

        return "TEXT";
    }

    /**
     * 自动脱敏处理
     */
    public String autoMask(String fieldName, String value) {
        String dataType = detectDataType(fieldName, value);
        return maskSensitiveData(value, dataType);
    }

    /**
     * 批量脱敏处理
     */
    public Map<String, String> batchMask(Map<String, String> data) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String maskedValue = autoMask(entry.getKey(), entry.getValue());
            result.put(entry.getKey(), maskedValue);
        }
        return result;
    }

    /**
     * 获取当前密钥ID
     */
    public String getCurrentKeyId() {
        return currentKeyId;
    }

    /**
     * 设置当前密钥ID
     */
    public void setCurrentKeyId(String keyId) {
        this.currentKeyId = keyId;
    }

    /**
     * 生成新的密钥并返回密钥ID
     */
    public String generateNewKeyId() {
        String newKeyId = "key_" + System.currentTimeMillis();
        getOrCreateKey(newKeyId); // 预先生成密钥
        return newKeyId;
    }

    /**
     * 验证数据是否为敏感数据
     */
    public boolean isSensitiveData(String fieldName, String value) {
        String dataType = detectDataType(fieldName, value);
        return !("TEXT".equals(dataType));
    }
}