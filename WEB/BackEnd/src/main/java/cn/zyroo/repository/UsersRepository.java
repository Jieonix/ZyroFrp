package cn.zyroo.repository;

import cn.zyroo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users,Long> {
  boolean existsByEmail(String Email);
  Users findByEmail(String Email);

  @Query("SELECT u FROM Users u WHERE u.vip_status IN :vipStatuses")
  List<Users> findByVipStatusIn(@Param("vipStatuses") List<Integer> vipStatuses);

  @Query(value = "SELECT * FROM users WHERE user_id = :user_id", nativeQuery = true)
  Users findByUserId(@Param("user_id") Long userId);

  @Query("SELECT u.email FROM Users u WHERE u.email IS NOT NULL AND u.email != ''")
  List<String> findAllEmails();

}