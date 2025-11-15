package cn.zyroo.user.service;

import cn.zyroo.user.model.Users;
import cn.zyroo.user.repository.UsersRepository;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.user.utils.EncryptionUtil;
import cn.zyroo.user.utils.JwtUtil;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdCardService {

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private EncryptionUtil encryptionUtil;

  // 实名认证添加数据库逻辑
  public ApiResponse<?> setIdCard(String idCard, String realName, String token) {
    String email = jwtUtil.getEmailFromToken(token);

    Users user = usersRepository.findByEmail(email);
    if (user == null) {
      return ApiResponse.error(ResponseCode.USERINFO_USER_NOT_FOUND);
    }
    String encryptedIdCard = encryptionUtil.encrypt(idCard);
    user.setId_card(encryptedIdCard);
    user.setReal_name(realName);
    user.setReal_name_status(1);
    usersRepository.save(user);
    return ApiResponse.success("实名认证已完成！");
  }

  // 获取实名认证状态
  public ApiResponse<?> getReal_name_status(String token) {
    String email = jwtUtil.getEmailFromToken(token);

    Users user = usersRepository.findByEmail(email);
    if (user == null) {
      return ApiResponse.error(ResponseCode.USERINFO_USER_NOT_FOUND);
    }
    return ApiResponse.success(user.getReal_name_status());
  }

}
