package cn.zyroo.dto;

import lombok.Data;

@Data
public class IdCardRequest {
  private String idCard;
  private String real_name;
  private String token;


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getReal_name() {
    return real_name;
  }

  public void setReal_name(String real_name) {
    this.real_name = real_name;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }
}
