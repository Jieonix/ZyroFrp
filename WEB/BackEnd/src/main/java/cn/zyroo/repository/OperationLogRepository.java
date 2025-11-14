package cn.zyroo.repository;

import cn.zyroo.model.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志数据访问层
 * 提供日志数据的增删改查操作
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    /**
     * 根据用户ID分页查询日志
     */
    Page<OperationLog> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据用户邮箱分页查询日志
     */
    Page<OperationLog> findByUserEmail(String userEmail, Pageable pageable);

    /**
     * 根据日志类型分页查询日志
     */
    Page<OperationLog> findByLogType(String logType, Pageable pageable);

    /**
     * 根据操作类型分页查询日志
     */
    Page<OperationLog> findByOperation(String operation, Pageable pageable);

    /**
     * 根据模块名称分页查询日志
     */
    Page<OperationLog> findByModule(String module, Pageable pageable);

    /**
     * 根据IP地址分页查询日志
     */
    Page<OperationLog> findByIpAddress(String ipAddress, Pageable pageable);

    /**
     * 根据状态分页查询日志
     */
    Page<OperationLog> findByStatus(String status, Pageable pageable);

    
    /**
     * 多条件组合查询日志
     */
    @Query("SELECT l FROM OperationLog l WHERE " +
           "(:logType IS NULL OR l.logType = :logType) AND " +
           "(:userId IS NULL OR l.userId = :userId) AND " +
           "(:userEmail IS NULL OR l.userEmail LIKE CONCAT('%', :userEmail, '%')) AND " +
           "(:operation IS NULL OR l.operation = :operation) AND " +
           "(:module IS NULL OR l.module = :module) AND " +
           "(:ipAddress IS NULL OR l.ipAddress = :ipAddress) AND " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:startTime IS NULL OR l.createdTime >= :startTime) AND " +
           "(:endTime IS NULL OR l.createdTime <= :endTime)")
    Page<OperationLog> findByMultipleConditions(@Param("logType") String logType,
                                                @Param("userId") Long userId,
                                                @Param("userEmail") String userEmail,
                                                @Param("operation") String operation,
                                                @Param("module") String module,
                                                @Param("ipAddress") String ipAddress,
                                                @Param("status") String status,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime,
                                                Pageable pageable);

    /**
     * 根据时间范围查询日志
     */
    @Query("SELECT l FROM OperationLog l WHERE l.createdTime BETWEEN :startTime AND :endTime")
    List<OperationLog> findByTimeRange(@Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 根据日志类型和时间范围查询日志
     */
    @Query("SELECT l FROM OperationLog l WHERE l.logType = :logType AND l.createdTime BETWEEN :startTime AND :endTime")
    List<OperationLog> findByLogTypeAndTimeRange(@Param("logType") String logType,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的日志数量（按日志类型分组）
     */
    @Query("SELECT l.logType, COUNT(l) FROM OperationLog l WHERE l.createdTime BETWEEN :startTime AND :endTime GROUP BY l.logType")
    List<Object[]> countByLogTypeInTimeRange(@Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的日志数量（按操作类型分组）
     */
    @Query("SELECT l.operation, COUNT(l) FROM OperationLog l WHERE l.createdTime BETWEEN :startTime AND :endTime GROUP BY l.operation")
    List<Object[]> countByOperationInTimeRange(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的独立用户数
     */
    @Query("SELECT COUNT(DISTINCT l.userId) FROM OperationLog l WHERE l.userId IS NOT NULL AND l.createdTime BETWEEN :startTime AND :endTime")
    Long countDistinctUsersByTimeRange(@Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 统计指定时间范围内的独立IP数
     */
    @Query("SELECT COUNT(DISTINCT l.ipAddress) FROM OperationLog l WHERE l.createdTime BETWEEN :startTime AND :endTime")
    Long countDistinctIpsByTimeRange(@Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);

    /**
     * 根据用户ID查询最近的操作日志
     */
    @Query("SELECT l FROM OperationLog l WHERE l.userId = :userId ORDER BY l.createdTime DESC")
    List<OperationLog> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 根据IP地址查询最近的操作日志
     */
    @Query("SELECT l FROM OperationLog l WHERE l.ipAddress = :ipAddress ORDER BY l.createdTime DESC")
    List<OperationLog> findRecentByIpAddress(@Param("ipAddress") String ipAddress, Pageable pageable);

    /**
     * 查询失败的操作日志
     */
    @Query("SELECT l FROM OperationLog l WHERE l.status IN ('FAILED', 'ERROR') AND l.createdTime BETWEEN :startTime AND :endTime ORDER BY l.createdTime DESC")
    List<OperationLog> findFailedLogs(@Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 查询安全相关的日志
     */
    @Query("SELECT l FROM OperationLog l WHERE l.logType = 'SECURITY' ORDER BY l.createdTime DESC")
    List<OperationLog> findSecurityLogs(Pageable pageable);

    /**
     * 删除指定时间之前的日志（用于日志清理）
     */
    @Modifying
    @Query("DELETE FROM OperationLog l WHERE l.createdTime < :cutoffTime AND l.logType = :logType")
    int deleteLogsBefore(@Param("cutoffTime") LocalDateTime cutoffTime, @Param("logType") String logType);

    /**
     * 查询平均执行时间
     */
    @Query("SELECT AVG(l.executionTime) FROM OperationLog l WHERE l.executionTime IS NOT NULL AND l.createdTime BETWEEN :startTime AND :endTime")
    Double getAvgExecutionTime(@Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);

    /**
     * 查询最活跃的用户
     */
    @Query("SELECT l.userEmail, COUNT(l) as count FROM OperationLog l WHERE l.userEmail IS NOT NULL AND l.createdTime BETWEEN :startTime AND :endTime GROUP BY l.userEmail ORDER BY count DESC")
    List<Object[]> findMostActiveUsers(@Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      Pageable pageable);

    /**
     * 查询最频繁的操作
     */
    @Query("SELECT l.operation, COUNT(l) as count FROM OperationLog l WHERE l.createdTime BETWEEN :startTime AND :endTime GROUP BY l.operation ORDER BY count DESC")
    List<Object[]> findMostFrequentOperations(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime,
                                             Pageable pageable);

    /**
     * 根据会话ID查询日志
     */
    List<OperationLog> findBySessionId(String sessionId, Pageable pageable);

    /**
     * 根据追踪ID查询日志
     */
    List<OperationLog> findByTraceId(String traceId);

    /**
     * 查询包含关键词的日志
     */
    @Query("SELECT l FROM OperationLog l WHERE " +
           "(l.description LIKE CONCAT('%', :keyword, '%') OR " +
           "l.requestUrl LIKE CONCAT('%', :keyword, '%') OR " +
           "l.errorMessage LIKE CONCAT('%', :keyword, '%')) AND " +
           "l.createdTime BETWEEN :startTime AND :endTime")
    Page<OperationLog> searchByKeyword(@Param("keyword") String keyword,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      Pageable pageable);
}