package cn.zyroo.user.service;

import cn.zyroo.user.model.Users;
import java.util.List;

public interface UserService {
    Users findByEmail(String email);
    boolean existsByEmail(String email);
    Users saveUser(Users user);
    Users findById(Long userId);
    void deleteUser(Long userId);
    List<String> findAllEmails();
}