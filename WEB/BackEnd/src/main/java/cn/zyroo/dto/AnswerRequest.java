package cn.zyroo.dto;

public class AnswerRequest {
  private String token;           // 用于获取用户的email
  private String userAnswer;      // 用户提交的答案

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUserAnswer() {
    return userAnswer;
  }

  public void setUserAnswer(String userAnswer) {
    this.userAnswer = userAnswer;
  }
}
