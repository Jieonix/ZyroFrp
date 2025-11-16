package cn.zyroo.question.service;

import cn.zyroo.question.model.UserAnswers;
import cn.zyroo.question.repository.UserAnswersRepository;
import cn.zyroo.user.model.Users;
import cn.zyroo.common.service.UserContextService;
import cn.zyroo.common.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UserAnswersService {

  @Autowired
  private UserAnswersRepository userAnswersRepository;

  @Autowired
  private UserContextService userContextService;

  // 检查用户是否在当天已经提交过答案
  public boolean hasAnsweredToday(String email) {
    LocalDate today = LocalDate.now();
    return userAnswersRepository.existsByEmailAndAnsweredAt(email, today);
  }

  // 保存题目到数据库
  public void saveUserAnswer(String email, Long questionId, String userAnswer, boolean isCorrect) {
    UserAnswers userAnswerRecord = new UserAnswers();
    userAnswerRecord.setEmail(email);
    userAnswerRecord.setQuestion_id(questionId);
    userAnswerRecord.setUser_answer(userAnswer);
    userAnswerRecord.setIs_correct(isCorrect);
    userAnswerRecord.setAnsweredAt(LocalDate.now());

    userAnswersRepository.save(userAnswerRecord);
  }

  // 答题后流量奖励逻辑
  public void trafficReward(String email, long trafficAmount) {

    UserInfo userInfo = userContextService.findUserByEmail(email);

    if (userInfo != null) {
      Users user = convertToUsers(userInfo);
      user.setRemaining_traffic(user.getRemaining_traffic() + trafficAmount);
      UserInfo updatedUserInfo = convertToUserInfo(user);
      userContextService.saveUser(updatedUserInfo);
    } else {
      throw new RuntimeException("用户不存在");
    }
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