package cn.zyroo.all.announcements.service;

import cn.zyroo.all.announcements.model.Announcements;
import cn.zyroo.all.announcements.repository.AnnouncementsRepository;
import cn.zyroo.all.common.utils.ApiResponse;
import cn.zyroo.all.common.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementsService {

  @Autowired
  private AnnouncementsRepository announcementsRepository;

  // 获取所有公告
  public ApiResponse<List<Announcements>> getAllAnnouncements() {
    List<Announcements> announcements = announcementsRepository.findAll();
    return announcements.isEmpty()
        ? ApiResponse.error(ResponseCode.ANNOUNCEMENT_FETCH_FAILED)
        : ApiResponse.success(announcements);
  }

  // 获取某个公告
  public ApiResponse<Announcements> getAnnouncementsById(Long id) {
    return announcementsRepository.findById(id)
        .map(ApiResponse::success)
        .orElse(ApiResponse.error(ResponseCode.ANNOUNCEMENT_NOT_FOUND));
  }

  // 创建公告
  public ApiResponse<String> createAnnouncements(Announcements announcements) {
    Announcements created = announcementsRepository.save(announcements);
    return created != null
        ? ApiResponse.success("公告创建成功")
        : ApiResponse.error(ResponseCode.ANNOUNCEMENT_CREATE_FAILED);
  }

  // 更新公告
  public ApiResponse<String> updateAnnouncements(Long id, Announcements announcements) {
    if (!announcementsRepository.existsById(id)) {
      return ApiResponse.error(ResponseCode.ANNOUNCEMENT_NOT_FOUND);
    }
    announcements.setId(id);
    announcements.setUpdated_at(LocalDateTime.now());
    Announcements updated = announcementsRepository.save(announcements);
    return updated != null
        ? ApiResponse.success("公告更新成功")
        : ApiResponse.error(ResponseCode.ANNOUNCEMENT_UPDATE_FAILED);
  }

  // 删除公告
  public ApiResponse<String> deleteAnnouncements(Long id) {
    if (!announcementsRepository.existsById(id)) {
      return ApiResponse.error(ResponseCode.ANNOUNCEMENT_NOT_FOUND);
    }
    try {
      announcementsRepository.deleteById(id);
      return ApiResponse.success("公告删除成功");
    } catch (Exception e) {
      return ApiResponse.error(ResponseCode.ANNOUNCEMENT_DELETE_FAILED);
    }
  }

}
