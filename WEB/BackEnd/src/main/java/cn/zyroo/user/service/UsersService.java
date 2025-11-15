package cn.zyroo.user.service;

import cn.zyroo.user.dto.EditUserRequest;
import cn.zyroo.user.model.Users;
import cn.zyroo.user.repository.UsersRepository;
import cn.zyroo.common.service.UserContextService;
import cn.zyroo.common.service.SecurityService;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import java.time.LocalDateTime;

@Service
public class UsersService {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private UserContextService userContextService;

  @Autowired
  private SecurityService securityService;

  public ApiResponse<?> register_backstage(Users users) {
    if (usersRepository.existsByEmail(users.getEmail())) {
      return ApiResponse.error(ResponseCode.REGISTER_USER_EXISTS);
    }

    Users user = new Users();
    user.setEmail(users.getEmail());
    user.setPassword(securityService.encryptPassword(users.getPassword()));
    user.setUser_key(user.generateToken());
    user.setRole(users.getRole());
    user.setRemaining_traffic(users.getRemaining_traffic());
    user.setUpload_limit(users.getUpload_limit());
    user.setDownload_limit(users.getDownload_limit());
    user.setIs_trial_user(users.getIs_trial_user());
    user.setReal_name(users.getReal_name());
    user.setId_card(users.getId_card());
    user.setReal_name_status(users.getReal_name_status());
    user.setVip_start_time(users.getVip_start_time());
    user.setVip_end_time(users.getVip_end_time());
    user.setVip_status(users.getVip_status());
    user.setCreated_at(LocalDateTime.now());
    user.setUpdated_at(LocalDateTime.now());

    usersRepository.save(user);
    return ApiResponse.success("新增用户成功！");
  }

  public ApiResponse<?> delete_user(Long user_id, String token) {
    String role = userContextService.getRoleFromToken(token);
    String email = userContextService.getEmailFromToken(token);

    Users user = usersRepository.findByUserId(user_id);

    if (user == null) {
      return ApiResponse.error(ResponseCode.USER_NOT_FOUND);
    }

    if (!(securityService.isAdminRole(role)) && !email.equals(user.getEmail())) {
      return ApiResponse.error(ResponseCode.UNAUTHORIZED_TO_DEACTIVATE_ACCOUNT);
    }

    usersRepository.deleteById(user_id);
    return ApiResponse.success("用户删除成功");
  }

  public ApiResponse<?> editUser(EditUserRequest req, String token) {
    String role = userContextService.getRoleFromToken(token);

    Users user = usersRepository.findByUserId(req.getUserId());

    if (user == null) {
      return ApiResponse.error(ResponseCode.USER_NOT_FOUND);
    }

    if (!securityService.isAdminRole(role)) {
      return ApiResponse.error(ResponseCode.UNAUTHORIZED_TO_DEACTIVATE_ACCOUNT);
    }

    if (req.getEmail() != null) user.setEmail(req.getEmail());
    if (req.getPassword() != null) user.setPassword(securityService.encryptPassword(req.getPassword()));
    if (req.getRole() != null) user.setRole(req.getRole());
    if (req.getRemainingTraffic() != null) user.setRemaining_traffic(req.getRemainingTraffic().longValue());
    if (req.getUploadLimit() != null) user.setUpload_limit(req.getUploadLimit());
    if (req.getDownloadLimit() != null) user.setDownload_limit(req.getDownloadLimit());
    if (req.getIsTrialUser() != null) user.setIs_trial_user(req.getIsTrialUser());
    if (req.getRealName() != null) user.setReal_name(req.getRealName());

    if (req.getIdCard() != null) {
      String encryptedIdCard = securityService.encryptData(req.getIdCard());
      user.setId_card(encryptedIdCard);
    }

    if (req.getRealNameStatus() != null) user.setReal_name_status(req.getRealNameStatus());
    if (req.getVipStartTime() != null) user.setVip_start_time(req.getVipStartTime());
    if (req.getVipEndTime() != null) user.setVip_end_time(req.getVipEndTime());
    if (req.getVipStatus() != null) user.setVip_status(req.getVipStatus());

    usersRepository.save(user);
    return ApiResponse.success("用户信息修改成功");
  }

  @Scheduled(cron = "0 * * * * ?")
  public void checkAndUpdateVipStatus() {
    LocalDate currentTime = LocalDate.now();
    List<Users> users = usersRepository.findByVipStatusIn(Arrays.asList(0, 1));

    for (Users user : users) {
      if (user.getVip_status() == 1 && user.getVip_end_time() != null && user.getVip_end_time().isBefore(currentTime)) {
        user.setVip_status(0);
      } else if (user.getVip_status() == 0 && user.getVip_start_time() != null && user.getVip_end_time() != null
          && user.getVip_end_time().isAfter(currentTime)) {
        user.setVip_status(1);
      }

      usersRepository.save(user);
    }
  }

  public Users findByEmail(String email) {
    return usersRepository.findByEmail(email);
  }
}