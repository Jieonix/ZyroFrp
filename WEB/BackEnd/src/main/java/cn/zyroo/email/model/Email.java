package cn.zyroo.email.model;

import jakarta.persistence.Id;
import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
public class Email {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;  // 设置主键

  private String email;
  private String code;
  private LocalDateTime createdAt;
  private LocalDateTime expiredAt;
  /**
   * status
   * “0” 代表未使用
   * “1” 代表已使用
   * “-1” 代表已过期
   * “-2” 代表已失效
   * **/
  private String status;
  private String type;


  @Override
  public String toString() {
    return "Email{" +
        "email='" + email + '\'' +
        ", code='" + code + '\'' +
        ", createdAt=" + createdAt +
        ", expiredAt=" + expiredAt +
        ", status='" + status + '\'' +
        ", type='" + type + '\'' +
        '}';
  }


  // Getters 和 Setters
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getExpiredAt() {
    return expiredAt;
  }

  public void setExpiredAt(LocalDateTime expiredAt) {
    this.expiredAt = expiredAt;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
