package cn.zyroo.repository;

import cn.zyroo.model.BulkEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BulkEmailRepository extends JpaRepository<BulkEmail, Long> {

    // 按创建时间倒序获取所有邮件记录
    List<BulkEmail> findAllByOrderByCreatedAtDesc();

    // 按创建时间倒序分页获取邮件记录
    @Query("SELECT b FROM BulkEmail b ORDER BY b.createdAt DESC")
    List<BulkEmail> findRecentEmails(@Param("limit") int limit);

    // 按主题搜索邮件
    List<BulkEmail> findBySubjectContainingIgnoreCaseOrderByCreatedAtDesc(String subject);

    // 按发送状态筛选邮件
    List<BulkEmail> findBySendStatusOrderByCreatedAtDesc(String sendStatus);

    // 获取最近的邮件记录（限制数量）
    @Query(value = "SELECT * FROM bulk_email ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<BulkEmail> findTopEmails(@Param("limit") int limit);
}