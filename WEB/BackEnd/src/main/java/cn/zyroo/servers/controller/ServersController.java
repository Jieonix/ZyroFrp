package cn.zyroo.servers.controller;

import cn.zyroo.servers.model.Servers;
import cn.zyroo.servers.service.ServersRepository;
import cn.zyroo.common.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servers")
public class ServersController {

  @Autowired
  private ServersRepository serversRepository;

  @GetMapping
  public ApiResponse<List<Servers>> getAvailableServers() {
    List<Servers> servers = serversRepository.findAll();
    return ApiResponse.success(servers);
  }

  @PostMapping
  public ApiResponse<Servers> addServer(@RequestBody Servers server) {
    Servers savedServer = serversRepository.save(server);
    return ApiResponse.success(savedServer);
  }

}

