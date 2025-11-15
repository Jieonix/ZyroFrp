package cn.zyroo.all.servers.controller;

import cn.zyroo.all.servers.model.Servers;
import cn.zyroo.all.servers.service.ServersRepository;
import cn.zyroo.all.common.utils.ApiResponse;
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

