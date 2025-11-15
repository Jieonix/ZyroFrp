package cn.zyroo.user.service;

import cn.zyroo.user.model.Users;
import cn.zyroo.user.repository.UsersRepository;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.common.service.UserContextService;
import cn.zyroo.common.service.SecurityService;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdCardService {

  @Autowired
  private UserContextService userContextService;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private UsersRepository usersRepository;

  public ApiResponse<?> setIdCard(String idCard, String realName, String token) {
    String email = userContextService.getEmailFromToken(token);

    Users user = usersRepository.findByEmail(email);
    if (user == null) {
      return ApiResponse.error(ResponseCode.USERINFO_USER_NOT_FOUND);
    }
    String encryptedIdCard = securityService.encryptData(idCard);
    user.setId_card(encryptedIdCard);
    user.setReal_name(realName);
    user.setReal_name_status(1);
    usersRepository.save(user);
    return ApiResponse.success("实名认证已完成！");
  }

  public ApiResponse<?> getReal_name_status(String token) {
    String email = userContextService.getEmailFromToken(token);

    Users user = usersRepository.findByEmail(email);
    if (user == null) {
      return ApiResponse.error(ResponseCode.USERINFO_USER_NOT_FOUND);
    }
    return ApiResponse.success(user.getReal_name_status());
  }

}
