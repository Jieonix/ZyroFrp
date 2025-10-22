package cn.zyroo.model;

import jakarta.persistence.*;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_answers")
public class UserAnswers {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private Long question_id;

  private String user_answer;

  private boolean is_correct;

  @Column(name = "answered_at")
  private LocalDate answeredAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getQuestion_id() {
    return question_id;
  }

  public void setQuestion_id(Long question_id) {
    this.question_id = question_id;
  }

  public String getUser_answer() {
    return user_answer;
  }

  public void setUser_answer(String user_answer) {
    this.user_answer = user_answer;
  }

  public boolean isIs_correct() {
    return is_correct;
  }

  public void setIs_correct(boolean is_correct) {
    this.is_correct = is_correct;
  }

  public LocalDate getAnsweredAt() {
    return answeredAt;
  }

  public void setAnsweredAt(LocalDate answeredAt) {
    this.answeredAt = answeredAt;
  }

}
