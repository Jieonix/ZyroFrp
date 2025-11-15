package cn.zyroo.auth.dto;


import lombok.Data;

@Data
public class RegisterRequest {
  private String email;
  private String password;
  private String emailCode;


  public String getEmailCode() {
    return emailCode;
  }

  public void setEmailCode(String emailCode) {
    this.emailCode = emailCode;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
