package cn.zyroo.question.repository;

import cn.zyroo.question.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
  @Query(value = "SELECT * FROM questions q LIMIT 1 OFFSET :offset", nativeQuery = true)
  Question findWithLimitOffset(@Param("offset") int offset);
}