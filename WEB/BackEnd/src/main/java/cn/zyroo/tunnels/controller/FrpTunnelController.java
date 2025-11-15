package cn.zyroo.tunnels.controller;

import cn.zyroo.tunnels.dto.UpdateFrpTunnelRequest;
import cn.zyroo.tunnels.model.FrpTunnel;
import cn.zyroo.tunnels.service.FrpTunnelService;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.user.utils.JwtUtil;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frp-tunnels")
public class FrpTunnelController {

  @Autowired
  private FrpTunnelService frpTunnelService;

  @Autowired
  private JwtUtil jwtUtils;

  @PostMapping
  public ApiResponse<String> addFrpTunnel(@RequestBody FrpTunnel frpTunnel, @RequestHeader("Token") String token) {
    try {
      ApiResponse status = frpTunnelService.addFrpTunnel(frpTunnel, token);
      return status;
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.FRPTUNNEL_ADD_FAILED);
    }
  }

  @GetMapping
  public ApiResponse<List<FrpTunnel>> getFrpTunnelByEmail(@RequestHeader("Token") String token) {
    try {
      String email = jwtUtils.getEmailFromToken(token);

      List<FrpTunnel> tunnels = frpTunnelService.findFrpTunnelsByEmail(email);

      if (tunnels.isEmpty()) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_SHOW_ISEMPTY);
      }

      return ApiResponse.success(tunnels);
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.FRPTUNNEL_SHOW_FAILED);
    }
  }

  @GetMapping("/by-server")
  public ApiResponse<List<FrpTunnel>> getFrpTunnelByServer(
      @RequestHeader("Token") String token,
      @RequestParam("server_name") String serverName) {
    try {
      String email = jwtUtils.getEmailFromToken(token);

      List<FrpTunnel> tunnels = frpTunnelService.findFrpTunnelsByEmailAndServer(email, serverName);

      if (tunnels.isEmpty()) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_SHOW_ISEMPTY);
      }

      return ApiResponse.success(tunnels);
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.FRPTUNNEL_SHOW_FAILED);
    }
  }

  @PutMapping("/{id}")
  public ApiResponse<String> updateFrpTunnel(
      @PathVariable("id") Long id,
      @RequestBody UpdateFrpTunnelRequest updateRequest) {
    try {
      String token = updateRequest.getToken();
      FrpTunnel updatedTunnel = updateRequest.getFrpTunnel();

      String email = jwtUtils.getEmailFromToken(token);

      FrpTunnel existingTunnel = frpTunnelService.findById(id);
      if (existingTunnel == null) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_NOT_FOUND);
      }

      if (!existingTunnel.getUser_email().equals(email)) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_UNAUTHORIZED);
      }

      boolean isUpdated = frpTunnelService.updateFrpTunnel(id, updatedTunnel);
      if (!isUpdated) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_UPDATE_FAILED);
      }

      return ApiResponse.success("隧道更新成功！");
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.FRPTUNNEL_UPDATE_FAILED);
    }
  }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteFrpTunnel(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
      String token = body.get("token");
      if (token == null) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_INVALID_REQUEST);
      }

      try {
        frpTunnelService.deleteFrpTunnel(id, token);
        return ApiResponse.success("隧道删除成功！");
      } catch (Exception e) {
        return ApiResponse.error(ResponseCode.FRPTUNNEL_DELETE_FAILED);
      }
    }
}
