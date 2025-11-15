package cn.zyroo.log.repository;

import cn.zyroo.log.model.SensitiveDataBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 敏感数据备份数据访问层
 * 提供敏感数据的存储和查询功能
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Repository
public interface SensitiveDataBackupRepository extends JpaRepository<SensitiveDataBackup, Long> {

    /**
     * 根据日志ID查询敏感数据
     */
    List<SensitiveDataBackup> findByLogId(Long logId);

    /**
     * 根据数据类型查询敏感数据
     */
    List<SensitiveDataBackup> findByDataType(String dataType);

    /**
     * 根据字段名称查询敏感数据
     */
    List<SensitiveDataBackup> findByFieldName(String fieldName);

    /**
     * 根据日志ID和数据类型查询敏感数据
     */
    List<SensitiveDataBackup> findByLogIdAndDataType(Long logId, String dataType);

    /**
     * 删除指定时间之前的敏感数据（用于数据清理）
     */
    @Modifying
    @Query("DELETE FROM SensitiveDataBackup s WHERE s.createdTime < :cutoffTime")
    int deleteOldData(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * 统计指定日志ID的敏感数据数量
     */
    @Query("SELECT COUNT(s) FROM SensitiveDataBackup s WHERE s.logId = :logId")
    Long countByLogId(@Param("logId") Long logId);

    /**
     * 根据加密密钥ID查询敏感数据
     */
    List<SensitiveDataBackup> findByEncryptionKeyId(String encryptionKeyId);
}