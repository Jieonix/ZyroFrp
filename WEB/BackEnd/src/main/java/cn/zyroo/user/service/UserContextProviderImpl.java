package cn.zyroo.user.service;

import cn.zyroo.common.dto.UserInfo;
import cn.zyroo.common.service.UserContextProvider;
import cn.zyroo.user.model.Users;
import cn.zyroo.user.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserContextProviderImpl implements UserContextProvider {

    private final UserService userService;
    private final UsersRepository usersRepository;

    public UserContextProviderImpl(UserService userService, UsersRepository usersRepository) {
        this.userService = userService;
        this.usersRepository = usersRepository;
    }

    @Override
    public UserInfo findUserByEmail(String email) {
        Users user = userService.findByEmail(email);
        return user != null ? convertToUserInfo(user) : null;
    }

    @Override
    public UserInfo findUserById(Long userId) {
        Users user = userService.findById(userId);
        return user != null ? convertToUserInfo(user) : null;
    }

    @Override
    public boolean userExists(String email) {
        return userService.existsByEmail(email);
    }

    @Override
    public UserInfo saveUser(UserInfo userInfo) {
        Users user = convertToUsers(userInfo);
        Users savedUser = userService.saveUser(user);
        return convertToUserInfo(savedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }

    @Override
    public List<String> findAllEmails() {
        return userService.findAllEmails();
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
        return user;
    }
}