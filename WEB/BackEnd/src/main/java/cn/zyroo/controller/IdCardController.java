package cn.zyroo.controller;

import cn.zyroo.dto.IdCardRequest;
import cn.zyroo.dto.TokenRequest;
import cn.zyroo.model.Users;
import cn.zyroo.service.IdCardService;
import cn.zyroo.service.UsersService;
import cn.zyroo.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/idcard")
public class IdCardController {

  @Autowired
  private IdCardService idCardService;

  @PostMapping
  public ApiResponse<?> getIdCard(@RequestBody IdCardRequest request) {
    return idCardService.setIdCard(request.getIdCard(), request.getReal_name(), request.getToken());
  }

  @GetMapping
  public ApiResponse<?> getIdCard(@RequestParam String token) {
    return idCardService.getReal_name_status(token);
  }

}
