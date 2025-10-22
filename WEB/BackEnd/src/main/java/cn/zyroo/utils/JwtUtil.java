package cn.zyroo.utils;

import cn.zyroo.exception.BizException;
import cn.zyroo.model.Users;
import cn.zyroo.repository.UsersRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final UsersRepository usersRepository;

  public JwtUtil(UsersRepository usersRepository) {
    this.usersRepository = usersRepository;
  }

  public String getEmailFromToken(String token) {
    try {
      DecodedJWT decodedJWT = JWT.decode(token);
      return decodedJWT.getSubject();
    } catch (JWTDecodeException e) {
      throw new BizException("Token 解析失败");
    }
  }

  public String getUserKeyFromToken(String token) {
    try {
      DecodedJWT decodedJWT = JWT.decode(token);
      String email = decodedJWT.getSubject();

      Users user = usersRepository.findByEmail(email);
      if (user == null) {
        throw new BizException("用户不存在");
      }
      return user.getUser_key();

    } catch (JWTDecodeException e) {
      throw new BizException("Token 解析失败");
    }
  }


  public String getRoleFromToken(String token) {
    try {
      DecodedJWT decodedJWT = JWT.decode(token);
      String email = decodedJWT.getSubject();
      String role = decodedJWT.getClaim("role").asString();

      if (!validateUserPermissions(email, role)) {
        throw new BizException("用户权限变更，请重新登录");
      }

      return role;

    } catch (JWTCreationException e) {
      throw new BizException("Token 解析失败");
    }
  }

  private boolean validateUserPermissions(String email, String TokenRole) {
    Users user = usersRepository.findByEmail(email);
    if (user == null || !user.getRole().equals(TokenRole)) {
      return false;
    }
    return true;
  }

}