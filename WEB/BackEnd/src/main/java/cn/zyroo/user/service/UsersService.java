package cn.zyroo.user.service;

import cn.zyroo.user.dto.EditUserRequest;
import cn.zyroo.email.model.Email;
import cn.zyroo.user.model.Users;
import cn.zyroo.user.repository.UsersRepository;
import cn.zyroo.email.repository.EmailRepository;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.user.utils.EncryptionUtil;
import cn.zyroo.user.utils.JwtUtil;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;

import java.time.LocalDateTime;

@Service
public class UsersService {

  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private EmailRepository emailRepository;

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  @Autowired
  private JwtUtil jwtUtil;

  // 注册功能
  public ApiResponse<?> register(String email, String password, String emailcode) {

    if (usersRepository.existsByEmail(email)) {
      return ApiResponse.error(ResponseCode.REGISTER_USER_EXISTS);
    }

    Optional<Email> emailRecord = emailRepository.findByEmailAndCodeAndType(email, emailcode, "REGISTER");

    if (emailRecord.isEmpty()) {
      return ApiResponse.error(ResponseCode.REGISTER_INVALID_VERIFICATION_CODE);
    }

    Email emailVerification = emailRecord.get();

    if ("1".equals(emailVerification.getStatus())) {
      return ApiResponse.error(ResponseCode.REGISTER_CODE_USED);
    }

    if ("-1".equals(emailVerification.getStatus())) {
      return ApiResponse.error(ResponseCode.REGISTER_CODE_EXPIRED);
    }

    if ("-2".equals(emailVerification.getStatus())) {
      return ApiResponse.error(ResponseCode.REGISTER_CODE_INVALID);
    }

    if ("0".equals(emailVerification.getStatus())) {
      emailVerification.setStatus("1");
      emailRepository.save(emailVerification);


      Users user = new Users();
      user.setEmail(email);
      user.setPassword(passwordEncoder.encode(password));
      user.setUser_key(user.generateToken());
      user.setCreated_at(LocalDateTime.now());
      user.setUpdated_at(LocalDateTime.now());
      usersRepository.save(user);

      return ApiResponse.success("注册成功！");
    }

    return ApiResponse.error(ResponseCode.REGISTER_FAILED);
  }

  // 后台用户新增
  public ApiResponse<?> register_backstage(Users users) {
    if (usersRepository.existsByEmail(users.getEmail())) {
      return ApiResponse.error(ResponseCode.REGISTER_USER_EXISTS);
    }

    Users user = new Users();
    user.setEmail(users.getEmail());
    user.setPassword(passwordEncoder.encode(users.getPassword()));
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






  // 普通用户登录功能
  public ApiResponse<?> login(String email, String password) {
    Users user = usersRepository.findByEmail(email);

    if (user == null) {
      return ApiResponse.error(ResponseCode.LOGIN_USER_NOT_FOUND);
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    if (!encoder.matches(password, user.getPassword())) {
      return ApiResponse.error(ResponseCode.LOGIN_PASSWORD_ERROR);
    }

    // 更新最后活跃时间
    user.setLast_active_time(LocalDateTime.now());

    String token = generateToken(user);

    if (token != null) {
      // 保存用户信息（包含更新后的活跃时间）
      usersRepository.save(user);
      return ApiResponse.success(token);
    }

    return ApiResponse.error(ResponseCode.LOGIN_FAILED);
  }

  // 生成一个随机密钥
  private String generateSecretKey() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[32];
    random.nextBytes(bytes);
    return Base64.getEncoder().encodeToString(bytes);
  }

  // 生成 token
  public String generateToken(Users user) {
    long expirationTime = 1000 * 60 * 60 * 24;

    String secretKey = generateSecretKey();

    return JWT.create()
        .withSubject(user.getEmail())
        .withClaim("role", user.getRole())
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
        .sign(Algorithm.HMAC256(secretKey));
  }

  // 管理员登录功能
  public ApiResponse<?> admin_login(String email, String password) {

    Users user = usersRepository.findByEmail(email);

    if (user == null) {
      return ApiResponse.error(ResponseCode.LOGIN_USER_NOT_FOUND);
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    if (!encoder.matches(password, user.getPassword())) {
      return ApiResponse.error(ResponseCode.LOGIN_PASSWORD_ERROR);
    }

    if (!user.getRole().equals("Admin") && !user.getRole().equals("SuperAdmin")) {
      return ApiResponse.error(ResponseCode.LOGIN_INSUFFICIENT_PERMISSIONS);
    }

    // 更新最后活跃时间
    user.setLast_active_time(LocalDateTime.now());

    String token = generateToken(user);

    if (token != null) {
      // 保存用户信息（包含更新后的活跃时间）
      usersRepository.save(user);
      return ApiResponse.success(token);
    }

    return ApiResponse.error(ResponseCode.LOGIN_FAILED);

  }




  // 找回密码功能
  public ApiResponse<?> resetPassword(String email, String newpassword, String emailcode) {

    Users user = usersRepository.findByEmail(email);

    if (user == null) {
      return ApiResponse.error(ResponseCode.PASSWORD_RESET_NOT_FOUND);
    }

    Optional<Email> emailRecord = emailRepository.findByEmailAndCodeAndType(email, emailcode, "RESET_PASSWORD");

    if (emailRecord.isEmpty()) {
      return ApiResponse.error(ResponseCode.PASSWORD_RESET_INVALID_VERIFICATION_CODE);
    }

    Email emailVerification = emailRecord.get();

    if ("1".equals(emailVerification.getStatus())) {
      return ApiResponse.error(ResponseCode.PASSWORD_RESET_CODE_USED);
    }

    if ("-1".equals(emailVerification.getStatus())) {
      return ApiResponse.error(ResponseCode.PASSWORD_RESET_CODE_EXPIRED);
    }

    if ("-2".equals(emailVerification.getStatus())) {
      return ApiResponse.error(ResponseCode.PASSWORD_RESET_CODE_INVALID);
    }

    if ("0".equals(emailVerification.getStatus())) {

      if (passwordEncoder.matches(newpassword, user.getPassword())) {
        return ApiResponse.error(ResponseCode.PASSWORD_SAME);
      }

      emailVerification.setStatus("1");
      emailRepository.save(emailVerification);

      user.setPassword(passwordEncoder.encode(newpassword));
      usersRepository.save(user);

      return ApiResponse.success("密码修改成功！");
    }

    return ApiResponse.error(ResponseCode.PASSWORD_RESET_FAILED);
  }




  public ApiResponse<?> delete_user(Long user_id, String token) {
    String role = jwtUtil.getRoleFromToken(token);
    String email = jwtUtil.getEmailFromToken(token);

    Users user = usersRepository.findByUserId(user_id);

    if (user == null) {
      return ApiResponse.error(ResponseCode.USER_NOT_FOUND);
    }

    if (!("Admin".equals(role) || "SuperAdmin".equals(role)) && !email.equals(user.getEmail())) {
      return ApiResponse.error(ResponseCode.UNAUTHORIZED_TO_DEACTIVATE_ACCOUNT);
    }

    usersRepository.deleteById(user_id);
    return ApiResponse.success("用户删除成功");
  }





  public ApiResponse<?> editUser(EditUserRequest req, String token) {
    String role = jwtUtil.getRoleFromToken(token);

    Users user = usersRepository.findByUserId(req.getUserId());

    if (user == null) {
      return ApiResponse.error(ResponseCode.USER_NOT_FOUND);
    }

    if (!("Admin".equals(role) || "SuperAdmin".equals(role))) {
      return ApiResponse.error(ResponseCode.UNAUTHORIZED_TO_DEACTIVATE_ACCOUNT);
    }

    if (req.getEmail() != null) user.setEmail(req.getEmail());
    if (req.getPassword() != null) user.setPassword(passwordEncoder.encode(req.getPassword()));
    if (req.getRole() != null) user.setRole(req.getRole());
    if (req.getRemainingTraffic() != null) user.setRemaining_traffic(req.getRemainingTraffic().longValue());
    if (req.getUploadLimit() != null) user.setUpload_limit(req.getUploadLimit());
    if (req.getDownloadLimit() != null) user.setDownload_limit(req.getDownloadLimit());
    if (req.getIsTrialUser() != null) user.setIs_trial_user(req.getIsTrialUser());
    if (req.getRealName() != null) user.setReal_name(req.getRealName());

    if (req.getIdCard() != null) {
      String encryptedIdCard = EncryptionUtil.encrypt(req.getIdCard());
      user.setId_card(encryptedIdCard);
    }

    if (req.getRealNameStatus() != null) user.setReal_name_status(req.getRealNameStatus());
    if (req.getVipStartTime() != null) user.setVip_start_time(req.getVipStartTime());
    if (req.getVipEndTime() != null) user.setVip_end_time(req.getVipEndTime());
    if (req.getVipStatus() != null) user.setVip_status(req.getVipStatus());

    usersRepository.save(user);
    return ApiResponse.success("用户信息修改成功");
  }






  // 检查并更新vip状态
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

  /**
   * 根据邮箱查找用户
   * @param email 用户邮箱
   * @return 用户对象，如果不存在则返回null
   */
  public Users findByEmail(String email) {
    return usersRepository.findByEmail(email);
  }

}