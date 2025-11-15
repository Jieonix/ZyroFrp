package cn.zyroo.email.repository;

import cn.zyroo.email.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

  // 查找特定邮箱和验证码并且类型为注册的验证码
  Optional<Email> findByEmailAndCodeAndType(String email, String code, String type);

  // 查找特定邮箱和状态的验证码
  Optional<Email> findByEmailAndStatusAndType(String email, String status, String type);

  // 更新验证码状态
  @Modifying
  @Transactional
  @Query("UPDATE Email e SET e.status = ?3 WHERE e.email = ?1 AND e.status = ?2 AND e.type = ?4")
  void updateStatusByEmailAndType(String email, String oldStatus, String newStatus, String type);

  // 更新过期验证码的状态
  @Modifying
  @Transactional
  @Query("UPDATE Email e SET e.status = '-1' WHERE e.expiredAt < CURRENT_TIMESTAMP AND e.status = '0' AND e.type = ?1")
  void expireExpiredCodesByType(String type);
}
