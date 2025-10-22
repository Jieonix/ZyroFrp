package cn.zyroo.repository;

import cn.zyroo.model.UserAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserAnswersRepository extends JpaRepository<UserAnswers, Long> {

  // 检查某个email在当天是否已经答过题
  boolean existsByEmailAndAnsweredAt(String email, LocalDate answered_at);  // 查询方法参数名与实体类一致
}
