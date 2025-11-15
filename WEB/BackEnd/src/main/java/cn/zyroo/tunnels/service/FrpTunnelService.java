package cn.zyroo.all.tunnels.service;

import cn.zyroo.all.tunnels.model.FrpTunnel;
import cn.zyroo.all.servers.model.Servers;
import cn.zyroo.all.tunnels.repository.FrpTunnelRepository;
import cn.zyroo.all.servers.service.ServersRepository;
import cn.zyroo.all.common.utils.ApiResponse;
import cn.zyroo.all.user.utils.JwtUtil;
import cn.zyroo.all.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FrpTunnelService {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private FrpTunnelRepository frpTunnelRepository;

  @Autowired
  private ServersRepository serversRepository;

  // 添加隧道逻辑
  public ApiResponse<String> addFrpTunnel(FrpTunnel frpTunnel, String token) {
    String email = jwtUtil.getEmailFromToken(token);
    frpTunnel.setUser_email(email);

    String userKey = jwtUtil.getUserKeyFromToken(token);
    frpTunnel.setUser_key(userKey);

    if (frpTunnel.getTunnel_type().equals("http") || frpTunnel.getTunnel_type().equals("https")) {
      if (frpTunnel.getCustom_domain() == null || frpTunnel.getCustom_domain().isEmpty()) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_MISSING_CUSTOM_DOMAIN);
      }
    }

    if (frpTunnel.getTunnel_type().equals("xtcp") || frpTunnel.getTunnel_type().equals("stcp")) {
      if (frpTunnel.getSecret_key() == null || frpTunnel.getSecret_key().isEmpty()) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_MISSING_SECRET_KEY);
      }
    }

    Optional<Servers> serverOptional = serversRepository.findByName(frpTunnel.getServer_name());
    if (serverOptional.isEmpty()) {
      return ApiResponse.error(ResponseCode.FRPTUNNEL_SERVER_NOT_FOUND);
    }

    Servers server = serverOptional.get();
    if (server.getCurrent_clients() >= server.getMax_clients()) {
      return ApiResponse.error(ResponseCode.FRPTUNNEL_SERVER_FULL);
    }

    String serverIp = server.getIp_address();
    frpTunnel.setServer_ip(serverIp);

    frpTunnel.setCreated_at(LocalDateTime.now());
    frpTunnel.setUpdated_at(LocalDateTime.now());

    frpTunnelRepository.save(frpTunnel);

    return ApiResponse.success("隧道添加成功！");
  }

  // 根据邮箱查询隧道
  public List<FrpTunnel> findFrpTunnelsByEmail(String email) {
    return frpTunnelRepository.findByUserEmail(email);
  }

  // 根据邮箱和服务器查询查询隧道
  public List<FrpTunnel> findFrpTunnelsByEmailAndServer(String email, String serverName) {
    return frpTunnelRepository.findByEmailAndServerName(email, serverName);
  }

  // 更新隧道信息
  public boolean updateFrpTunnel(Long id, FrpTunnel updatedTunnel) {
    FrpTunnel tunnel = frpTunnelRepository.findById(id).orElse(null);

    if (tunnel == null) {
      return false;
    }

    tunnel.setTunnel_name(updatedTunnel.getTunnel_name());
    tunnel.setTunnel_type(updatedTunnel.getTunnel_type());
    tunnel.setLocal_ip(updatedTunnel.getLocal_ip());
    tunnel.setLocal_port(updatedTunnel.getLocal_port());
    tunnel.setRemote_port(updatedTunnel.getRemote_port());
    tunnel.setCustom_domain(updatedTunnel.getCustom_domain());
    tunnel.setSecret_key(updatedTunnel.getSecret_key());
    tunnel.setUpdated_at(LocalDateTime.now());
    frpTunnelRepository.save(tunnel);
    return true;
  }

  // 根据id查询隧道
  public FrpTunnel findById(Long id) {
    Optional<FrpTunnel> optionalTunnel = frpTunnelRepository.findById(id);
    return optionalTunnel.orElse(null);
  }

  // 删除隧道
  public void deleteFrpTunnel(Long id, String token) {
    String email = jwtUtil.getEmailFromToken(token);
    FrpTunnel tunnel = frpTunnelRepository.findById(id).orElse(null);

    if (tunnel == null) {
      throw new RuntimeException("隧道不存在");
    }

    if (!tunnel.getUser_email().equals(email)) {
      throw new RuntimeException("权限不足");
    }

    frpTunnelRepository.deleteById(id);
  }

}

