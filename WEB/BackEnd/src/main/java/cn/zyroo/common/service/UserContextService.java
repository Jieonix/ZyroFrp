package cn.zyroo.common.service;

import cn.zyroo.common.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserContextService {

    @Autowired
    private UserContextProvider userContextProvider;

    @Autowired
    private TokenService tokenService;

    public UserInfo findUserByEmail(String email) {
        return userContextProvider.findUserByEmail(email);
    }

    public UserInfo findUserById(Long userId) {
        return userContextProvider.findUserById(userId);
    }

    public UserInfo getUserFromToken(String token) {
        if (token == null || !tokenService.isTokenValid(token)) {
            return null;
        }

        String email = tokenService.getEmailFromToken(token);
        if (email == null) {
            return null;
        }

        return findUserByEmail(email);
    }

    public String getEmailFromToken(String token) {
        return tokenService.getEmailFromToken(token);
    }

    public String getRoleFromToken(String token) {
        return tokenService.getRoleFromToken(token);
    }

    public String getUserKeyFromToken(String token) {
        return tokenService.getUserKeyFromToken(token);
    }

    public boolean userExists(String email) {
        return userContextProvider.userExists(email);
    }

    public UserInfo saveUser(UserInfo userInfo) {
        return userContextProvider.saveUser(userInfo);
    }

    public void deleteUser(Long userId) {
        userContextProvider.deleteUser(userId);
    }

    public boolean isTokenValidWithUser(String token) {
        if (token == null || !tokenService.isTokenValid(token)) {
            return false;
        }

        String email = tokenService.getEmailFromToken(token);
        return email != null && userExists(email);
    }

    public List<String> findAllEmails() {
        return userContextProvider.findAllEmails();
    }
}