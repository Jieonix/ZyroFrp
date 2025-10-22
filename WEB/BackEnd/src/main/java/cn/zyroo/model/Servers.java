package cn.zyroo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "Servers")
public class Servers {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String ip_address;

  private int port = 7788;

  private int max_clients = 100;

  private int current_clients = 0;

  private int current_tunnels = 0;

  private boolean vipOnly = false;

  private boolean allowWebsite = true;

  private boolean allowHighTraffic = false;

  private boolean allowUdp = true;

  private String info;

  private String domain;

  private String port_range = "10000-65535";


  @Enumerated(EnumType.STRING)
  private ServerStatus status = ServerStatus.ONLINE;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @CreationTimestamp
  private LocalDateTime updatedAt;


  public enum ServerStatus {
    ONLINE, OFFLINE
  }



  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIp_address() {
    return ip_address;
  }

  public void setIp_address(String ip_address) {
    this.ip_address = ip_address;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public int getMax_clients() {
    return max_clients;
  }

  public void setMax_clients(int max_clients) {
    this.max_clients = max_clients;
  }

  public int getCurrent_clients() {
    return current_clients;
  }

  public void setCurrent_clients(int current_clients) {
    this.current_clients = current_clients;
  }

  public boolean isVipOnly() {
    return vipOnly;
  }

  public void setVipOnly(boolean vipOnly) {
    this.vipOnly = vipOnly;
  }

  public ServerStatus getStatus() {
    return status;
  }

  public void setStatus(ServerStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }


  public int getCurrent_tunnels() {
    return current_tunnels;
  }

  public void setCurrent_tunnels(int current_tunnels) {
    this.current_tunnels = current_tunnels;
  }

  public boolean isAllowWebsite() {
    return allowWebsite;
  }

  public void setAllowWebsite(boolean allowWebsite) {
    this.allowWebsite = allowWebsite;
  }

  public boolean isAllowHighTraffic() {
    return allowHighTraffic;
  }

  public void setAllowHighTraffic(boolean allowHighTraffic) {
    this.allowHighTraffic = allowHighTraffic;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getPort_range() {
    return port_range;
  }

  public void setPort_range(String port_range) {
    this.port_range = port_range;
  }

  public boolean isAllowUdp() {
    return allowUdp;
  }

  public void setAllowUdp(boolean allowUdp) {
    this.allowUdp = allowUdp;
  }

}