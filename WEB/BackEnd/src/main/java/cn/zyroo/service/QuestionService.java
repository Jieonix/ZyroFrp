package cn.zyroo.service;

import cn.zyroo.model.Question;
import cn.zyroo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

  @Autowired
  private QuestionRepository questionRepository;

  // 获取今日题目
  public Question getTodayQuestion() {
    long count = questionRepository.count();
    if (count == 0) {
      return null;
    }

    long days = java.time.temporal.ChronoUnit.DAYS.between(
        java.time.LocalDate.of(1970, 1, 1),
        java.time.LocalDate.now()
    );

    int offset = (int) (days % count);
    return questionRepository.findWithLimitOffset(offset);
  }

  // 添加题目
  public Question addQuestion(Question question) {
    return questionRepository.save(question);
  }

  // 获取所有题目
  public List<Question> getAllQuestions() {
    return questionRepository.findAll();
  }
}