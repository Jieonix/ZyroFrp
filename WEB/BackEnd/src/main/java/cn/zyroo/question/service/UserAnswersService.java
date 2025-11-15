package cn.zyroo.question.service;

import cn.zyroo.question.model.UserAnswers;
import cn.zyroo.question.repository.UserAnswersRepository;
import cn.zyroo.user.model.Users;
import cn.zyroo.common.service.UserContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    Users user = userContextService.findUserByEmail(email);

    if (user != null) {
      user.setRemaining_traffic(user.getRemaining_traffic() + trafficAmount);
      userContextService.saveUser(user);
    } else {
      throw new RuntimeException("用户不存在");
    }
  }

}
