package cn.zyroo.controller;

import cn.zyroo.dto.EditUserRequest;
import cn.zyroo.dto.LoginRequest;
import cn.zyroo.dto.RegisterRequest;
import cn.zyroo.dto.ResetPasswordRequest;
import cn.zyroo.model.Users;
import cn.zyroo.service.UsersService;
import cn.zyroo.utils.ApiResponse;
import cn.zyroo.utils.JwtUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UsersService usersService;

  @PostMapping("/register")
  public ApiResponse<?> register(@RequestBody RegisterRequest request) {
    return usersService.register(request.getEmail(), request.getPassword(), request.getEmailCode());
  }

  @PostMapping("/register_backstage")
  public ApiResponse<?> register_backstage(@RequestBody Users users) {
    return usersService.register_backstage(users);
  }


  @PostMapping("/login")
  public ApiResponse<?> login(@RequestBody LoginRequest request) {
    return usersService.login(request.getEmail(), request.getPassword());
  }

  @PostMapping("/admin_login")
  public ApiResponse<?> admin_login(@RequestBody LoginRequest request) {
    return usersService.admin_login(request.getEmail(), request.getPassword());
  }

  @PutMapping("/password")
  public ApiResponse<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    return usersService.resetPassword(request.getEmail(), request.getNewPassword(), request.getEmailCode());
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
