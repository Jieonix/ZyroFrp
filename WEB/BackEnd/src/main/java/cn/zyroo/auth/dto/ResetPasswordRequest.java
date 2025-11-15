package cn.zyroo.all.auth.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
  private String email;
  private String newPassword;
  private String emailCode;


  public String getEmailCode() {
    return emailCode;
  }

  public void setEmailCode(String emailCode) {
    this.emailCode = emailCode;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
