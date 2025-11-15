package cn.zyroo.user.model;

import java.security.SecureRandom;
import java.math.BigInteger;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long user_id;

  private String user_key;

  private String email;

  private String password;

  private int vip_status = 0;

  private int is_trial_user = 1;

  private Long remaining_traffic = 4000L;

  private Long upload_limit = 1000L;

  private Long download_limit = 1000L;

  private String role = "User";

  private int real_name_status = 0;

  private String real_name;

  private String id_card;

  private LocalDateTime created_at;

  private LocalDateTime updated_at;

  private LocalDate vip_start_time;

  private LocalDate vip_end_time;

  private LocalDateTime last_active_time;


  public String generateToken() {
    SecureRandom random = new SecureRandom();
    return new BigInteger(130, random).toString(32).substring(0, 20);
  }


  public Long getUser_id() {
    return user_id;
  }

  public void setUser_id(Long user_id) {
    this.user_id = user_id;
  }

  public String getUser_key() {
    return user_key;
  }

  public void setUser_key(String user_key) {
    this.user_key = user_key;
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

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  public LocalDateTime getUpdated_at() {
    return updated_at;
  }

  public void setUpdated_at(LocalDateTime updated_at) {
    this.updated_at = updated_at;
  }

  public int getVip_status() { return vip_status; }

  public void setVip_status(int vip_status) { this.vip_status = vip_status; }

  public String getRole() { return role; }

  public void setRole(String role) { this.role = role; }

  public Long getRemaining_traffic() {
    return remaining_traffic;
  }

  public void setRemaining_traffic(Long remaining_traffic) {
    this.remaining_traffic = remaining_traffic;
  }

  public Long getUpload_limit() { return upload_limit; }

  public void setUpload_limit(Long upload_limit) { this.upload_limit = upload_limit; }

  public Long getDownload_limit() { return download_limit; }

  public void setDownload_limit(Long download_limit) { this.download_limit = download_limit; }

  public int getReal_name_status() {
    return real_name_status;
  }

  public void setReal_name_status(int real_name_status) {
    this.real_name_status = real_name_status;
  }

  public String getReal_name() {
    return real_name;
  }

  public void setReal_name(String real_name) {
    this.real_name = real_name;
  }

  public String getId_card() {
    return id_card;
  }

  public void setId_card(String id_card) {
    this.id_card = id_card;
  }

  public LocalDate getVip_start_time() {
    return vip_start_time;
  }

  public void setVip_start_time(LocalDate vip_start_time) {
    this.vip_start_time = vip_start_time;
  }

  public LocalDate getVip_end_time() {
    return vip_end_time;
  }

  public void setVip_end_time(LocalDate vip_end_time) {
    this.vip_end_time = vip_end_time;
  }

  public int getIs_trial_user() {
    return is_trial_user;
  }

  public void setIs_trial_user(int is_trial_user) {
    this.is_trial_user = is_trial_user;
  }

  public LocalDateTime getLast_active_time() {
    return last_active_time;
  }

  public void setLast_active_time(LocalDateTime last_active_time) {
    this.last_active_time = last_active_time;
  }

}