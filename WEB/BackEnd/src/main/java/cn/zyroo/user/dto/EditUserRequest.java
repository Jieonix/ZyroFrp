package cn.zyroo.all.user.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EditUserRequest {
  private Long userId;
  private String email;
  private String password;
  private String role;
  private Double remainingTraffic;
  private Long uploadLimit;
  private Long downloadLimit;
  private Integer isTrialUser;
  private String realName;
  private String idCard;
  private Integer realNameStatus;
  private LocalDate vipStartTime;
  private LocalDate vipEndTime;
  private Integer vipStatus;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Double getRemainingTraffic() {
    return remainingTraffic;
  }

  public void setRemainingTraffic(Double remainingTraffic) {
    this.remainingTraffic = remainingTraffic;
  }

  public Long getUploadLimit() {
    return uploadLimit;
  }

  public void setUploadLimit(Long uploadLimit) {
    this.uploadLimit = uploadLimit;
  }

  public Long getDownloadLimit() {
    return downloadLimit;
  }

  public void setDownloadLimit(Long downloadLimit) {
    this.downloadLimit = downloadLimit;
  }

  public Integer getIsTrialUser() {
    return isTrialUser;
  }

  public void setIsTrialUser(Integer isTrialUser) {
    this.isTrialUser = isTrialUser;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public Integer getRealNameStatus() {
    return realNameStatus;
  }

  public void setRealNameStatus(Integer realNameStatus) {
    this.realNameStatus = realNameStatus;
  }

  public LocalDate getVipStartTime() {
    return vipStartTime;
  }

  public void setVipStartTime(LocalDate vipStartTime) {
    this.vipStartTime = vipStartTime;
  }

  public LocalDate getVipEndTime() {
    return vipEndTime;
  }

  public void setVipEndTime(LocalDate vipEndTime) {
    this.vipEndTime = vipEndTime;
  }

  public Integer getVipStatus() {
    return vipStatus;
  }

  public void setVipStatus(Integer vipStatus) {
    this.vipStatus = vipStatus;
  }

}

