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
}