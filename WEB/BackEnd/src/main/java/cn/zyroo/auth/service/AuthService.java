package cn.zyroo.auth.service;

import cn.zyroo.email.model.Email;
import cn.zyroo.email.repository.EmailRepository;
import cn.zyroo.user.model.Users;
import cn.zyroo.common.dto.UserInfo;
import cn.zyroo.common.service.TokenService;
import cn.zyroo.common.service.UserContextService;
import cn.zyroo.common.service.SecurityService;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final EmailRepository emailRepository;
    private final TokenService tokenService;
    private final UserContextService userContextService;
    private final SecurityService securityService;

    public AuthService(EmailRepository emailRepository, TokenService tokenService,
                      UserContextService userContextService, SecurityService securityService) {
        this.emailRepository = emailRepository;
        this.tokenService = tokenService;
        this.userContextService = userContextService;
        this.securityService = securityService;
    }

    private UserInfo convertToUserInfo(Users user) {
        if (user == null) {
            return null;
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getUser_id());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(user.getRole());
        userInfo.setUserKey(user.getUser_key());
        userInfo.setPassword(user.getPassword());
        userInfo.setCreatedAt(user.getCreated_at());
        userInfo.setUpdatedAt(user.getUpdated_at());
        userInfo.setVipStatus(user.getVip_status());
        userInfo.setIsTrialUser(user.getIs_trial_user());
        userInfo.setRemainingTraffic(user.getRemaining_traffic());
        userInfo.setUploadLimit(user.getUpload_limit());
        userInfo.setDownloadLimit(user.getDownload_limit());
        userInfo.setRealNameStatus(user.getReal_name_status());
        userInfo.setRealName(user.getReal_name());
        userInfo.setIdCard(user.getId_card());
        userInfo.setVipStartTime(user.getVip_start_time());
        userInfo.setVipEndTime(user.getVip_end_time());
        userInfo.setLastActiveTime(user.getLast_active_time());
        return userInfo;
    }

    private Users convertToUsers(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }

        Users user = new Users();
        user.setUser_id(userInfo.getUserId());
        user.setEmail(userInfo.getEmail());
        user.setRole(userInfo.getRole());
        user.setUser_key(userInfo.getUserKey());
        user.setPassword(userInfo.getPassword());
        user.setCreated_at(userInfo.getCreatedAt());
        user.setUpdated_at(userInfo.getUpdatedAt());
        user.setVip_status(userInfo.getVipStatus());
        user.setIs_trial_user(userInfo.getIsTrialUser());
        user.setRemaining_traffic(userInfo.getRemainingTraffic());
        user.setUpload_limit(userInfo.getUploadLimit());
        user.setDownload_limit(userInfo.getDownloadLimit());
        user.setReal_name_status(userInfo.getRealNameStatus());
        user.setReal_name(userInfo.getRealName());
        user.setId_card(userInfo.getIdCard());
        user.setVip_start_time(userInfo.getVipStartTime());
        user.setVip_end_time(userInfo.getVipEndTime());
        user.setLast_active_time(userInfo.getLastActiveTime());
        return user;
    }

    public ApiResponse<?> register(String email, String password, String emailcode) {
        if (userContextService.userExists(email)) {
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
            user.setPassword(securityService.encryptPassword(password));
            user.setUser_key(user.generateToken());
            user.setCreated_at(LocalDateTime.now());
            user.setUpdated_at(LocalDateTime.now());

            UserInfo userInfo = convertToUserInfo(user);
            userContextService.saveUser(userInfo);

            return ApiResponse.success("注册成功！");
        }

        return ApiResponse.error(ResponseCode.REGISTER_FAILED);
    }

    public ApiResponse<?> login(String email, String password) {
        UserInfo userInfo = userContextService.findUserByEmail(email);

        if (userInfo == null) {
            return ApiResponse.error(ResponseCode.LOGIN_USER_NOT_FOUND);
        }

        Users user = convertToUsers(userInfo);
        if (!securityService.matchesPassword(password, user.getPassword())) {
            return ApiResponse.error(ResponseCode.LOGIN_PASSWORD_ERROR);
        }

        // 只更新 last_active_time，不保存整个用户对象，避免数据丢失
        user.setLast_active_time(LocalDateTime.now());
        String token = tokenService.generateToken(userInfo);

        if (token != null) {
            return ApiResponse.success(token);
        }

        return ApiResponse.error(ResponseCode.LOGIN_FAILED);
    }

    public ApiResponse<?> adminLogin(String email, String password) {
        UserInfo userInfo = userContextService.findUserByEmail(email);

        if (userInfo == null) {
            return ApiResponse.error(ResponseCode.LOGIN_USER_NOT_FOUND);
        }

        Users user = convertToUsers(userInfo);
        if (!securityService.matchesPassword(password, user.getPassword())) {
            return ApiResponse.error(ResponseCode.LOGIN_PASSWORD_ERROR);
        }

        if (!securityService.isAdminRole(user.getRole())) {
            return ApiResponse.error(ResponseCode.LOGIN_INSUFFICIENT_PERMISSIONS);
        }

        user.setLast_active_time(LocalDateTime.now());
        UserInfo updatedUserInfo = convertToUserInfo(user);
        String token = tokenService.generateToken(updatedUserInfo);

        if (token != null) {
            userContextService.saveUser(updatedUserInfo);
            return ApiResponse.success(token);
        }

        return ApiResponse.error(ResponseCode.LOGIN_FAILED);
    }

    public ApiResponse<?> resetPassword(String email, String newpassword, String emailcode) {
        UserInfo userInfo = userContextService.findUserByEmail(email);

        if (userInfo == null) {
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
            Users user = convertToUsers(userInfo);
            if (securityService.matchesPassword(newpassword, user.getPassword())) {
                return ApiResponse.error(ResponseCode.PASSWORD_SAME);
            }

            emailVerification.setStatus("1");
            emailRepository.save(emailVerification);

            user.setPassword(securityService.encryptPassword(newpassword));
            UserInfo updatedUserInfo = convertToUserInfo(user);
            userContextService.saveUser(updatedUserInfo);

            return ApiResponse.success("密码修改成功！");
        }

        return ApiResponse.error(ResponseCode.PASSWORD_RESET_FAILED);
    }
}