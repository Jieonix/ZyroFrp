package cn.zyroo.controller;

import cn.zyroo.dto.TokenRequest;
import cn.zyroo.model.Users;
import cn.zyroo.repository.UsersRepository;
import cn.zyroo.service.UserInfoService;
import cn.zyroo.service.UsersService;
import cn.zyroo.utils.ApiResponse;
import cn.zyroo.utils.JwtUtil;
import cn.zyroo.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserInfoController {

  @Autowired
  private JwtUtil jwtUtil;

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
      email = jwtUtil.getEmailFromToken(request.getToken());
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
    String role = jwtUtil.getRoleFromToken(token);

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
    String role = jwtUtil.getRoleFromToken(token);

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
      String email = jwtUtil.getEmailFromToken(token);

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