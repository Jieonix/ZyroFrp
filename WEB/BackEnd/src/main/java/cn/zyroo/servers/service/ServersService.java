package cn.zyroo.servers.service;

import cn.zyroo.servers.model.Servers;
import cn.zyroo.servers.model.Servers.ServerStatus;
import cn.zyroo.tunnels.repository.FrpTunnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@Service
public class ServersService {

  @Autowired
  private ServersRepository serversRepository;

  @Autowired
  private FrpTunnelRepository frpTunnelRepository;


  public ServersService(ServersRepository serversRepository) {
    this.serversRepository = serversRepository;
  }

  // 定时检查服务器状态
  @Scheduled(fixedRate = 60000)
  public void checkServerStatus() {
    List<Servers> servers = serversRepository.findAll();

    for (Servers server : servers) {
      boolean isOnline = checkLocalServerStatus(server) || checkFrpServer(server);

      if (server.getStatus() != (isOnline ? ServerStatus.ONLINE : ServerStatus.OFFLINE)) {
        server.setStatus(isOnline ? ServerStatus.ONLINE : ServerStatus.OFFLINE);
        serversRepository.save(server);
      }
    }
  }

  // 检查本地服务器在线状态
  public boolean checkLocalServerStatus(Servers server) {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress("127.0.0.1", server.getPort()), 10000);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  //  检查远程服务器在线状态
  public boolean checkFrpServer(Servers server) {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress(server.getIp_address(), server.getPort()), 10000);
      return true;
    } catch (IOException e) {
      return false;
    }
  }


  private boolean checkFrpServer(String ip, int port) {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress(ip, port), 3000);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  // 更新服务器状态
  @Scheduled(fixedRate = 6000)
  public void updateServerStats() {
    List<Servers> servers = serversRepository.findAll();

    for (Servers server : servers) {

      Long currentClients = frpTunnelRepository.countDistinctUsersByServerName(server.getName());

      Long currentTunnels = frpTunnelRepository.countTunnelsByServerName(server.getName());

      server.setCurrent_clients(currentClients != null ? currentClients.intValue() : 0);
      server.setCurrent_tunnels(currentTunnels != null ? currentTunnels.intValue() : 0);

      serversRepository.save(server);
    }
  }

}

