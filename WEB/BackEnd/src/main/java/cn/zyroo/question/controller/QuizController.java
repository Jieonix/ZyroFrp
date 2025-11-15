package cn.zyroo.question.controller;

import cn.zyroo.question.dto.AnswerRequest;
import cn.zyroo.question.model.Question;
import cn.zyroo.question.service.QuestionService;
import cn.zyroo.question.service.UserAnswersService;
import cn.zyroo.common.utils.ApiResponse;
import cn.zyroo.user.utils.JwtUtil;
import cn.zyroo.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/quiz")
public class QuizController {

  @Autowired
  private QuestionService questionService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private UserAnswersService userAnswersService;

  @GetMapping
  public ApiResponse<List<Question>> getAllQuestions() {
    List<Question> questions = questionService.getAllQuestions();
    if (questions == null || questions.isEmpty()) {
      return ApiResponse.error(ResponseCode.QUIZ_NOT_FOUND);
    }
    return ApiResponse.success(questions);
  }

  @GetMapping("/today")
  public ApiResponse<Question> getTodayQuestion() {
    Question question = questionService.getTodayQuestion();
    if (question == null) {
      return ApiResponse.error(ResponseCode.QUIZ_NOT_FOUND);
    }
    return ApiResponse.success(question);
  }

  @PostMapping("/questions")
  public ApiResponse<Question> addQuestion(@RequestBody Question question) {
    try {
      Question savedQuestion = questionService.addQuestion(question);
      return ApiResponse.success(savedQuestion);
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.QUIZ_CREATE_FAILED);
    }
  }

  @PostMapping("/answer")
  public ApiResponse<String> submitAnswer(@RequestBody AnswerRequest answerRequest) {
    String email;
    try {
      email = jwtUtil.getEmailFromToken(answerRequest.getToken());
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.QUIZ_SUBMIT_FAILED);
    }

    Question question = questionService.getTodayQuestion();
    if (question == null) {
      return ApiResponse.error(ResponseCode.QUIZ_NOT_FOUND);
    }

    boolean alreadyAnswered = userAnswersService.hasAnsweredToday(email);
    if (alreadyAnswered) {
      return ApiResponse.error(ResponseCode.QUIZ_ALREADY_ANSWERED);
    }

    boolean isCorrect = question.getCorrect_answer().equals(answerRequest.getUserAnswer());
    userAnswersService.saveUserAnswer(email, question.getId(), answerRequest.getUserAnswer(), isCorrect);

    Random random = new Random();
    long trafficRewardGB;

    if (isCorrect) {
      trafficRewardGB = random.nextInt(91) + 10;
    } else {
      trafficRewardGB = random.nextInt(21) + 5;
    }

    userAnswersService.trafficReward(email, trafficRewardGB);

    double trafficRewardTB = trafficRewardGB / 1024.0;
    String trafficRewardStr = String.format("%.3f", trafficRewardTB);

    return ApiResponse.success(
        (isCorrect ? "回答正确！\n奖励流量：" : "别灰心，下次继续努力！\n补偿流量：")
            + trafficRewardStr + " TB"
    );


  }

  @GetMapping("/todayStatus")
  public ApiResponse<Boolean> getTodayQuizStatus(@RequestHeader("Token") String token) {
    try {
      String email = jwtUtil.getEmailFromToken(token);

      Boolean isQuiz = userAnswersService.hasAnsweredToday(email);

      return ApiResponse.success(isQuiz);

    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.QUIZ_STATUS_FAILED);
    }
  }

}