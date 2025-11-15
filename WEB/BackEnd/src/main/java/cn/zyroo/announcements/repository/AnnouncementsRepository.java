package cn.zyroo.announcements.repository;

import cn.zyroo.announcements.model.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsRepository extends JpaRepository<Announcements, Long> {

}
