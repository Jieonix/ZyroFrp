package cn.zyroo.auth.service;

import cn.zyroo.email.model.Email;
import cn.zyroo.email.repository.EmailRepository;
import cn.zyroo.user.model.Users;
import cn.zyroo.common.service.TokenService;
import cn.zyroo.common.service.UserContextService;
import cn.zyroo.common.service.SecurityService;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserContextService userContextService;

    @Autowired
    private SecurityService securityService;

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
            userContextService.saveUser(user);

            return ApiResponse.success("注册成功！");
        }

        return ApiResponse.error(ResponseCode.REGISTER_FAILED);
    }

    public ApiResponse<?> login(String email, String password) {
        Users user = userContextService.findUserByEmail(email);

        if (user == null) {
            return ApiResponse.error(ResponseCode.LOGIN_USER_NOT_FOUND);
        }

        if (!securityService.matchesPassword(password, user.getPassword())) {
            return ApiResponse.error(ResponseCode.LOGIN_PASSWORD_ERROR);
        }

        user.setLast_active_time(LocalDateTime.now());
        String token = tokenService.generateToken(user);

        if (token != null) {
            userContextService.saveUser(user);
            return ApiResponse.success(token);
        }

        return ApiResponse.error(ResponseCode.LOGIN_FAILED);
    }

    public ApiResponse<?> adminLogin(String email, String password) {
        Users user = userContextService.findUserByEmail(email);

        if (user == null) {
            return ApiResponse.error(ResponseCode.LOGIN_USER_NOT_FOUND);
        }

        if (!securityService.matchesPassword(password, user.getPassword())) {
            return ApiResponse.error(ResponseCode.LOGIN_PASSWORD_ERROR);
        }

        if (!securityService.isAdminRole(user.getRole())) {
            return ApiResponse.error(ResponseCode.LOGIN_INSUFFICIENT_PERMISSIONS);
        }

        user.setLast_active_time(LocalDateTime.now());
        String token = tokenService.generateToken(user);

        if (token != null) {
            userContextService.saveUser(user);
            return ApiResponse.success(token);
        }

        return ApiResponse.error(ResponseCode.LOGIN_FAILED);
    }

    public ApiResponse<?> resetPassword(String email, String newpassword, String emailcode) {
        Users user = userContextService.findUserByEmail(email);

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
            if (securityService.matchesPassword(newpassword, user.getPassword())) {
                return ApiResponse.error(ResponseCode.PASSWORD_SAME);
            }

            emailVerification.setStatus("1");
            emailRepository.save(emailVerification);

            user.setPassword(securityService.encryptPassword(newpassword));
            userContextService.saveUser(user);

            return ApiResponse.success("密码修改成功！");
        }

        return ApiResponse.error(ResponseCode.PASSWORD_RESET_FAILED);
    }
}