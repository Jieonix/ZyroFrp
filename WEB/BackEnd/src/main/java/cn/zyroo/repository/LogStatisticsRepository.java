package cn.zyroo.repository;

import cn.zyroo.model.LogStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 日志统计数据访问层
 * 提供日志统计数据的存储和查询功能
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Repository
public interface LogStatisticsRepository extends JpaRepository<LogStatistics, Long> {

    /**
     * 根据统计日期查询统计数据
     */
    List<LogStatistics> findByStatDate(LocalDate statDate);

    /**
     * 根据日志类型查询统计数据
     */
    List<LogStatistics> findByLogType(String logType);

    /**
     * 根据统计日期和日志类型查询统计数据
     */
    LogStatistics findByStatDateAndLogType(LocalDate statDate, String logType);

    /**
     * 根据统计日期、日志类型、操作类型和模块查询统计数据
     */
    LogStatistics findByStatDateAndLogTypeAndOperationAndModule(LocalDate statDate,
                                                              String logType,
                                                              String operation,
                                                              String module);

    /**
     * 查询指定日期范围内的统计数据
     */
    @Query("SELECT s FROM LogStatistics s WHERE s.statDate BETWEEN :startDate AND :endDate ORDER BY s.statDate DESC")
    List<LogStatistics> findByDateRange(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    /**
     * 查询指定日期范围内指定日志类型的统计数据
     */
    @Query("SELECT s FROM LogStatistics s WHERE s.logType = :logType AND s.statDate BETWEEN :startDate AND :endDate ORDER BY s.statDate DESC")
    List<LogStatistics> findByLogTypeAndDateRange(@Param("logType") String logType,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内的总日志数量
     */
    @Query("SELECT SUM(s.totalCount) FROM LogStatistics s WHERE s.statDate BETWEEN :startDate AND :endDate")
    Long sumTotalCountByDateRange(@Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内成功的日志数量
     */
    @Query("SELECT SUM(s.successCount) FROM LogStatistics s WHERE s.statDate BETWEEN :startDate AND :endDate")
    Long sumSuccessCountByDateRange(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内失败的日志数量
     */
    @Query("SELECT SUM(s.failedCount) FROM LogStatistics s WHERE s.statDate BETWEEN :startDate AND :endDate")
    Long sumFailedCountByDateRange(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);

    /**
     * 统计指定日期范围内错误的日志数量
     */
    @Query("SELECT SUM(s.errorCount) FROM LogStatistics s WHERE s.statDate BETWEEN :startDate AND :endDate")
    Long sumErrorCountByDateRange(@Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    /**
     * 查询最近N天的统计数据
     */
    @Query("SELECT s FROM LogStatistics s WHERE s.statDate >= :sinceDate ORDER BY s.statDate DESC")
    List<LogStatistics> findRecentStatistics(@Param("sinceDate") LocalDate sinceDate);

    /**
     * 删除指定日期之前的统计数据（用于数据清理）
     */
    @Modifying
    @Query("DELETE FROM LogStatistics s WHERE s.statDate < :cutoffDate")
    int deleteOldStatistics(@Param("cutoffDate") LocalDate cutoffDate);

    /**
     * 查询最受欢迎的操作类型（按统计次数排序）
     */
    @Query("SELECT s.operation, SUM(s.totalCount) as totalCount FROM LogStatistics s WHERE s.operation IS NOT NULL AND s.statDate BETWEEN :startDate AND :endDate GROUP BY s.operation ORDER BY totalCount DESC")
    List<Object[]> findMostPopularOperations(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    /**
     * 查询最活跃的模块（按统计次数排序）
     */
    @Query("SELECT s.module, SUM(s.totalCount) as totalCount FROM LogStatistics s WHERE s.module IS NOT NULL AND s.statDate BETWEEN :startDate AND :endDate GROUP BY s.module ORDER BY totalCount DESC")
    List<Object[]> findMostActiveModules(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    /**
     * 查询平均执行时间趋势
     */
    @Query("SELECT s.statDate, AVG(s.avgExecutionTime) FROM LogStatistics s WHERE s.statDate BETWEEN :startDate AND :endDate GROUP BY s.statDate ORDER BY s.statDate")
    List<Object[]> findAvgExecutionTimeTrend(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
}