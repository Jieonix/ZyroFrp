package cn.zyroo.user.controller;

import cn.zyroo.common.dto.TokenRequest;
import cn.zyroo.user.model.Users;
import cn.zyroo.user.repository.UsersRepository;
import cn.zyroo.user.service.UserInfoService;
import cn.zyroo.user.service.UsersService;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.common.service.UserContextService;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserInfoController {

  
  @Autowired
  private UserContextService userContextService;
  @Autowired
  private UserInfoService userInfoService;
  @Autowired
  private UsersService usersService;
  @Autowired
  private UsersRepository usersRepository;

  @PostMapping("/info")
  public ApiResponse<Users> getUserInfo(@RequestBody TokenRequest request) {
    String email;
    try {
      email = userContextService.getEmailFromToken(request.getToken());
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.USERINFO_TOKEN_INVALID);
    }

    Users user = userInfoService.getUserByEmail(email);
    if (user == null) {
      return ApiResponse.error(ResponseCode.USERINFO_USER_NOT_FOUND);
    }

    return ApiResponse.success(user);
  }

  @GetMapping
  public ApiResponse<List<Users>> getAllUsers(@RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String role = userContextService.getRoleFromToken(token);

    if (!role.equals("Admin") && !role.equals("SuperAdmin")) {
      return ApiResponse.error(ResponseCode.USERINFO_UNAUTHORIZED);
    }

    List<Users> users = userInfoService.getAll();

    for (Users user : users) {
      user.setPassword("");
      user.setId_card("");
    }

    return ApiResponse.success(users);
  }

  @GetMapping("/{userId}")
  public ApiResponse<Users> getUserInfoById(@PathVariable Long userId,
                                            @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String role = userContextService.getRoleFromToken(token);

    if (!role.equals("Admin") && !role.equals("SuperAdmin")) {
      return ApiResponse.error(ResponseCode.USERINFO_UNAUTHORIZED);
    }

    Users user = usersRepository.findByUserId(userId);
    if (user == null) {
      return ApiResponse.error(ResponseCode.USERINFO_USER_NOT_FOUND);
    }

    user.setPassword("");
    user.setId_card("");
    return ApiResponse.success(user);
  }

  @PostMapping("/update-last-active")
  public ApiResponse<String> updateLastActiveTime(@RequestHeader("Authorization") String authHeader) {
    try {
      String token = authHeader.replace("Bearer ", "");
      String email = userContextService.getEmailFromToken(token);

      Users user = usersRepository.findByEmail(email);
      if (user == null) {
        return ApiResponse.error(ResponseCode.USERINFO_USER_NOT_FOUND);
      }

      user.setLast_active_time(LocalDateTime.now());
      usersRepository.save(user);

      return ApiResponse.success("活跃时间更新成功");
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.USERINFO_TOKEN_INVALID);
    }
  }

}