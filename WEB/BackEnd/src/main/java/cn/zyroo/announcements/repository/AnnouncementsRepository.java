package cn.zyroo.all.announcements.repository;

import cn.zyroo.all.announcements.model.Announcements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementsRepository extends JpaRepository<Announcements, Long> {

}
