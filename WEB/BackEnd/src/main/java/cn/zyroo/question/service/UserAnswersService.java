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
