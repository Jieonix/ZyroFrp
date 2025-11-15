package cn.zyroo.servers.service;

import cn.zyroo.servers.model.Servers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServersRepository extends JpaRepository<Servers, Long> {
  List<Servers> findByStatus(Servers.ServerStatus status);

  Optional<Servers> findByName(String name);

}

