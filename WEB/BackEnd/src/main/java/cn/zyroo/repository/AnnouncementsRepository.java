package cn.zyroo.repository;

import cn.zyroo.model.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsRepository extends JpaRepository<Announcements, Long> {

}
