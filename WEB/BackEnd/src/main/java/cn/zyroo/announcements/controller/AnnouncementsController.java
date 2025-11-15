package cn.zyroo.all.announcements.controller;

import cn.zyroo.all.announcements.model.Announcements;
import cn.zyroo.all.announcements.service.AnnouncementsService;
import cn.zyroo.all.common.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcements")
public class AnnouncementsController {

  @Autowired
  private AnnouncementsService announcementsService;

  @GetMapping
  public ApiResponse<List<Announcements>> getAllAnnouncements() {
    return announcementsService.getAllAnnouncements();
  }

  @GetMapping("/{id}")
  public ApiResponse<Announcements> getAnnouncementsById(@PathVariable Long id) {
    return announcementsService.getAnnouncementsById(id);
  }

  @PostMapping
  public ApiResponse<String> createAnnouncements(@RequestBody Announcements announcements) {
    return announcementsService.createAnnouncements(announcements);
  }

  @PatchMapping("/{id}")
  public ApiResponse<String> updateAnnouncements(@PathVariable Long id, @RequestBody Announcements announcements) {
    return announcementsService.updateAnnouncements(id, announcements);
  }

  @DeleteMapping("/{id}")
  public ApiResponse<String> deleteAnnouncements(@PathVariable Long id) {
    return announcementsService.deleteAnnouncements(id);
  }
}
