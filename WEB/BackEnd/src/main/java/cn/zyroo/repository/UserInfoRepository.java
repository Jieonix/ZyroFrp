package cn.zyroo.repository;

import cn.zyroo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<Users, Long> {
  Users findByEmail(String email);
}
