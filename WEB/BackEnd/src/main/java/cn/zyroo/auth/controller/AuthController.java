package cn.zyroo.auth.controller;

import cn.zyroo.user.dto.EditUserRequest;
import cn.zyroo.auth.dto.LoginRequest;
import cn.zyroo.auth.dto.RegisterRequest;
import cn.zyroo.auth.dto.ResetPasswordRequest;
import cn.zyroo.user.model.Users;
import cn.zyroo.auth.service.AuthService;
import cn.zyroo.user.service.UsersService;
import cn.zyroo.common.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private UsersService usersService;

  @PostMapping("/register")
  public ApiResponse<?> register(@RequestBody RegisterRequest request) {
    return authService.register(request.getEmail(), request.getPassword(), request.getEmailCode());
  }

  @PostMapping("/register_backstage")
  public ApiResponse<?> register_backstage(@RequestBody Users users) {
    return usersService.register_backstage(users);
  }

  @PostMapping("/login")
  public ApiResponse<?> login(@RequestBody LoginRequest request) {
    return authService.login(request.getEmail(), request.getPassword());
  }

  @PostMapping("/admin_login")
  public ApiResponse<?> admin_login(@RequestBody LoginRequest request) {
    return authService.adminLogin(request.getEmail(), request.getPassword());
  }

  @PutMapping("/password")
  public ApiResponse<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    return authService.resetPassword(request.getEmail(), request.getNewPassword(), request.getEmailCode());
  }

  @DeleteMapping("/delete/{user_id}")
  public ApiResponse<?> delete_user(@PathVariable Long user_id, @RequestHeader("Authorization") String token) {
    return usersService.delete_user(user_id, token);
  }

  @PostMapping("/editUser")
  public ApiResponse<?> editUser(@RequestBody EditUserRequest req,
                                 @RequestHeader("Authorization") String token) {
    return usersService.editUser(req, token);
  }

}
