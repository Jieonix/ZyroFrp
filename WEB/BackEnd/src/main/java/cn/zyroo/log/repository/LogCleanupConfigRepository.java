package cn.zyroo.log.repository;

import cn.zyroo.log.model.LogCleanupConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 日志清理配置数据访问层
 * 提供日志清理配置的存储和查询功能
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Repository
public interface LogCleanupConfigRepository extends JpaRepository<LogCleanupConfig, Integer> {

    /**
     * 根据日志类型查询清理配置
     */
    LogCleanupConfig findByLogType(String logType);

    /**
     * 查询所有启用的清理配置
     */
    List<LogCleanupConfig> findByEnabledTrue();

    /**
     * 查询指定清理策略的配置
     */
    @Query("SELECT c FROM LogCleanupConfig c WHERE c.cleanupStrategy = :strategy")
    List<LogCleanupConfig> findByCleanupStrategy(@Param("strategy") String strategy);

    /**
     * 查询保留时间小于指定月数的配置
     */
    @Query("SELECT c FROM LogCleanupConfig c WHERE c.retentionMonths < :months AND c.enabled = true")
    List<LogCleanupConfig> findShortRetentionConfigs(@Param("months") Integer months);

    /**
     * 查询所有日志类型（用于验证配置完整性）
     */
    @Query("SELECT DISTINCT c.logType FROM LogCleanupConfig c")
    List<String> findAllLogTypes();
}