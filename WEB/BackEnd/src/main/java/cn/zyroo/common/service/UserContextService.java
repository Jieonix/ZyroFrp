package cn.zyroo.common.service;

import cn.zyroo.user.model.Users;
import cn.zyroo.user.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserContextService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenService tokenService;

    public Users findUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public Users findUserById(Long userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    public Users getUserFromToken(String token) {
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
        return usersRepository.existsByEmail(email);
    }

    public Users saveUser(Users user) {
        return usersRepository.save(user);
    }

    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }

    public boolean isTokenValidWithUser(String token) {
        if (token == null || !tokenService.isTokenValid(token)) {
            return false;
        }

        String email = tokenService.getEmailFromToken(token);
        return email != null && userExists(email);
    }

    public List<String> findAllEmails() {
        return usersRepository.findAllEmails();
    }
}