package cn.zyroo.repository;

import cn.zyroo.model.FrpTunnel;
import cn.zyroo.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FrpTunnelRepository extends JpaRepository<FrpTunnel, Long> {

  @Query(value = "SELECT * FROM frp_tunnels WHERE user_email = :email", nativeQuery = true)
  List<FrpTunnel> findByUserEmail(@Param("email") String email);

  @Query(value = "SELECT * FROM frp_tunnels WHERE user_email = ?1 AND server_name = ?2", nativeQuery = true)
  List<FrpTunnel> findByEmailAndServerName(String email, String serverName);

  @Query(value = "SELECT COUNT(DISTINCT user_email) FROM frp_tunnels WHERE server_name = :serverName", nativeQuery = true)
  Long countDistinctUsersByServerName(@Param("serverName") String serverName);

  @Query(value = "SELECT COUNT(*) FROM frp_tunnels WHERE server_name = :serverName", nativeQuery = true)
  Long countTunnelsByServerName(@Param("serverName") String serverName);

}
