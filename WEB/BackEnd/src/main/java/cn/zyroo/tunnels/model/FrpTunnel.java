package cn.zyroo.tunnels.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "frp_tunnels", uniqueConstraints = @UniqueConstraint(columnNames = {"server_name", "remote_port"}))
public class FrpTunnel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String user_email;

  private String user_key;

  private String server_name;

  private String tunnel_name;

  private String tunnel_type;

  private String local_ip;

  private Integer local_port;

  private Integer remote_port;

  private String server_ip;

  private String custom_domain;

  private String secret_key;

  @CreationTimestamp
  private LocalDateTime created_at;

  @CreationTimestamp
  private LocalDateTime updated_at;



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUser_email() {
    return user_email;
  }

  public void setUser_email(String user_email) {
    this.user_email = user_email;
  }

  public String getServer_name() {
    return server_name;
  }

  public void setServer_name(String server_name) {
    this.server_name = server_name;
  }

  public String getTunnel_name() {
    return tunnel_name;
  }

  public void setTunnel_name(String tunnel_name) {
    this.tunnel_name = tunnel_name;
  }

  public String getTunnel_type() {
    return tunnel_type;
  }

  public void setTunnel_type(String tunnel_type) {
    this.tunnel_type = tunnel_type;
  }

  public String getLocal_ip() {
    return local_ip;
  }

  public void setLocal_ip(String local_ip) {
    this.local_ip = local_ip;
  }

  public Integer getLocal_port() {
    return local_port;
  }

  public void setLocal_port(Integer local_port) {
    this.local_port = local_port;
  }

  public Integer getRemote_port() {
    return remote_port;
  }

  public void setRemote_port(Integer remote_port) {
    this.remote_port = remote_port;
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

  public String getServer_ip() {
    return server_ip;
  }

  public void setServer_ip(String server_ip) {
    this.server_ip = server_ip;
  }

  public String getUser_key() {
    return user_key;
  }

  public void setUser_key(String user_key) {
    this.user_key = user_key;
  }

  public String getCustom_domain() {
    return custom_domain;
  }

  public void setCustom_domain(String custom_domain) {
    this.custom_domain = custom_domain;
  }

  public String getSecret_key() {
    return secret_key;
  }

  public void setSecret_key(String secret_key) {
    this.secret_key = secret_key;
  }

}