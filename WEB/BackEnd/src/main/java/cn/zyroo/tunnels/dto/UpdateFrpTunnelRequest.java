package cn.zyroo.tunnels.dto;

import cn.zyroo.tunnels.model.FrpTunnel;
import lombok.Data;

@Data
public class UpdateFrpTunnelRequest {
  private FrpTunnel frpTunnel;
  private String token;

  // Getters and Setters
  public FrpTunnel getFrpTunnel() {
    return frpTunnel;
  }

  public void setFrpTunnel(FrpTunnel frpTunnel) {
    this.frpTunnel = frpTunnel;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
