package cn.zyroo.service;

import cn.zyroo.model.OperationLog;
import cn.zyroo.model.SensitiveDataBackup;
import cn.zyroo.model.LogStatistics;
import cn.zyroo.model.LogCleanupConfig;
import cn.zyroo.repository.OperationLogRepository;
import cn.zyroo.repository.SensitiveDataBackupRepository;
import cn.zyroo.repository.LogStatisticsRepository;
import cn.zyroo.repository.LogCleanupConfigRepository;
import cn.zyroo.utils.IpUtils;
import cn.zyroo.utils.SensitiveDataUtils;
import cn.zyroo.utils.LogUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 日志服务类
 * 提供日志记录、查询、统计和清理等功能
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Service
@Transactional
public class LogService {

    private static final Logger log = LoggerFactory.getLogger(LogService.class);

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private SensitiveDataBackupRepository sensitiveDataBackupRepository;

    @Autowired
    private LogStatisticsRepository logStatisticsRepository;

    @Autowired
    private LogCleanupConfigRepository logCleanupConfigRepository;

    @Autowired
    private IpUtils ipUtils;

    @Autowired
    private SensitiveDataUtils sensitiveDataUtils;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 记录操作日志
     */
    @Async
    public CompletableFuture<Void> logOperation(OperationLog operationLog) {
        try {
            // 解析用户代理信息
            if (operationLog.getUserAgent() != null) {
                Map<String, String> userAgentInfo = ipUtils.parseUserAgent(operationLog.getUserAgent());
                operationLog.setBrowser(userAgentInfo.get("browser"));
                operationLog.setOs(userAgentInfo.get("os"));
            }

            // 根据IP地址获取地理位置
            if (operationLog.getIpAddress() != null) {
                String location = ipUtils.getLocationByIp(operationLog.getIpAddress());
                operationLog.setLocation(location);
            }

            // 保存日志
            operationLogRepository.save(operationLog);

            // 异步更新统计信息
            updateStatistics(operationLog);

            log.debug("操作日志记录成功: {}", operationLog);
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 记录敏感数据备份
     */
    @Async
    public CompletableFuture<Void> saveSensitiveDataBackup(Long logId, String dataType, String fieldName, String originalData) {
        try {
            if (originalData == null || originalData.trim().isEmpty()) {
                return CompletableFuture.completedFuture(null);
            }

            // 加密敏感数据
            String encryptedData = sensitiveDataUtils.encrypt(originalData);
            String encryptionKeyId = sensitiveDataUtils.getCurrentKeyId();

            SensitiveDataBackup backup = new SensitiveDataBackup();
            backup.setLogId(logId);
            backup.setDataType(dataType);
            backup.setFieldName(fieldName);
            backup.setEncryptedData(encryptedData);
            backup.setEncryptionKeyId(encryptionKeyId);

            sensitiveDataBackupRepository.save(backup);
            log.debug("敏感数据备份记录成功: logId={}, dataType={}, fieldName={}", logId, dataType, fieldName);
        } catch (Exception e) {
            log.error("记录敏感数据备份失败", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 获取所有日志（不分页）
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getAllLogs(String logType, Long userId, String userEmail,
                                         String operation, String module, String ipAddress,
                                         String status, LocalDateTime startTime,
                                         LocalDateTime endTime, Sort sort) {
        // 创建一个大的Pageable来获取所有数据
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        Page<OperationLog> page = operationLogRepository.findByMultipleConditions(
                logType, userId, userEmail, operation, module, ipAddress, status, startTime, endTime, pageable);
        return page.getContent();
    }

    /**
     * 分页查询日志
     */
    @Transactional(readOnly = true)
    public Page<OperationLog> getLogsByPage(String logType, Long userId, String userEmail,
                                           String operation, String module, String ipAddress,
                                           String status, LocalDateTime startTime,
                                           LocalDateTime endTime, Pageable pageable) {
        return operationLogRepository.findByMultipleConditions(
                logType, userId, userEmail, operation, module, ipAddress, status, startTime, endTime, pageable);
    }

    /**
     * 根据日志ID查询日志详情
     */
    @Transactional(readOnly = true)
    public OperationLog getLogById(Long id) {
        return operationLogRepository.findById(id).orElse(null);
    }

    /**
     * 根据日志ID查询敏感数据
     */
    @Transactional(readOnly = true)
    public List<SensitiveDataBackup> getSensitiveDataByLogId(Long logId) {
        return sensitiveDataBackupRepository.findByLogId(logId);
    }

    /**
     * 解密敏感数据
     */
    public String decryptSensitiveData(String encryptedData, String encryptionKeyId) {
        try {
            return sensitiveDataUtils.decrypt(encryptedData, encryptionKeyId);
        } catch (Exception e) {
            log.error("解密敏感数据失败", e);
            return "解密失败";
        }
    }

    /**
     * 获取日志统计信息
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getLogStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> statistics = new java.util.HashMap<>();

        try {
            // 总数统计（查询所有数据，不受时间限制）
            Long totalCount = operationLogRepository.count();
            statistics.put("totalCount", totalCount);

            // 按状态统计（成功/失败）- 查询所有数据，不受时间限制
            Pageable allPageable = PageRequest.of(0, Integer.MAX_VALUE);
            Page<OperationLog> allLogsPage = operationLogRepository.findByMultipleConditions(
                    null, null, null, null, null, null, null, null, null, allPageable);

            Long successCount = 0L;
            Long errorCount = 0L;
            for (OperationLog log : allLogsPage.getContent()) {
                if ("SUCCESS".equals(log.getStatus())) {
                    successCount++;
                } else if ("FAILED".equals(log.getStatus()) || "ERROR".equals(log.getStatus())) {
                    errorCount++;
                }
            }

            statistics.put("successCount", successCount);
            statistics.put("errorCount", errorCount);

            // 按日志类型统计（查询所有数据，不受时间限制）
            List<Object[]> logTypeStats = operationLogRepository.countByLogTypeInTimeRange(
                    LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
            Map<String, Long> logTypeMap = new java.util.HashMap<>();
            for (Object[] stat : logTypeStats) {
                if (stat[0] != null && stat[1] != null) {
                    logTypeMap.put((String) stat[0], (Long) stat[1]);
                }
            }
            statistics.put("logTypeStats", logTypeMap);

            // 按操作类型统计（查询所有数据，不受时间限制）
            List<Object[]> operationStats = operationLogRepository.countByOperationInTimeRange(
                    LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
            Map<String, Long> operationMap = new java.util.HashMap<>();
            for (Object[] stat : operationStats) {
                if (stat[0] != null && stat[1] != null) {
                    operationMap.put((String) stat[0], (Long) stat[1]);
                }
            }
            statistics.put("operationStats", operationMap);

            // 独立用户数（查询所有数据，不受时间限制）
            Long uniqueUsers = operationLogRepository.countDistinctUsersByTimeRange(
                    LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
            statistics.put("uniqueUsers", uniqueUsers != null ? uniqueUsers : 0L);

            // 独立IP数（查询所有数据，不受时间限制）
            Long uniqueIps = operationLogRepository.countDistinctIpsByTimeRange(
                    LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
            statistics.put("uniqueIps", uniqueIps != null ? uniqueIps : 0L);

            // 平均执行时间（查询所有数据，不受时间限制）
            Double avgExecutionTime = operationLogRepository.getAvgExecutionTime(
                    LocalDateTime.of(2020, 1, 1, 0, 0), LocalDateTime.now());
            statistics.put("avgExecutionTime", avgExecutionTime != null ? avgExecutionTime : 0.0);

            log.debug("统计数据生成完成: totalCount={}, successCount={}, errorCount={}, uniqueUsers={}, uniqueIps={}",
                     totalCount, successCount, errorCount, uniqueUsers, uniqueIps);

        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            // 返回默认值防止前端报错
            statistics.put("totalCount", 0L);
            statistics.put("logTypeStats", new java.util.HashMap<>());
            statistics.put("operationStats", new java.util.HashMap<>());
            statistics.put("uniqueUsers", 0L);
            statistics.put("uniqueIps", 0L);
            statistics.put("avgExecutionTime", 0.0);
            statistics.put("successCount", 0L);
            statistics.put("errorCount", 0L);
        }

        return statistics;
    }

    /**
     * 获取用户操作历史
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getUserOperationHistory(Long userId, Pageable pageable) {
        return operationLogRepository.findRecentByUserId(userId, pageable);
    }

    /**
     * 获取IP操作历史
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getIpOperationHistory(String ipAddress, Pageable pageable) {
        return operationLogRepository.findRecentByIpAddress(ipAddress, pageable);
    }

    /**
     * 获取失败日志
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getFailedLogs(LocalDateTime startTime, LocalDateTime endTime) {
        return operationLogRepository.findFailedLogs(startTime, endTime);
    }

    /**
     * 获取安全日志
     */
    @Transactional(readOnly = true)
    public List<OperationLog> getSecurityLogs(Pageable pageable) {
        return operationLogRepository.findSecurityLogs(pageable);
    }

    /**
     * 搜索日志
     */
    @Transactional(readOnly = true)
    public Page<OperationLog> searchLogs(String keyword, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return operationLogRepository.searchByKeyword(keyword, startTime, endTime, pageable);
    }

    /**
     * 更新统计信息
     */
    @Async
    private void updateStatistics(OperationLog operationLog) {
        try {
            LocalDate today = LocalDate.now();
            String logType = operationLog.getLogType();
            String operation = operationLog.getOperation();
            String module = operationLog.getModule();

            // 查找或创建统计记录
            LogStatistics statistics = logStatisticsRepository
                    .findByStatDateAndLogTypeAndOperationAndModule(today, logType, operation, module);

            if (statistics == null) {
                statistics = new LogStatistics(today, logType, operation, module);
            }

            // 更新统计计数
            if ("SUCCESS".equals(operationLog.getStatus())) {
                statistics.incrementSuccessCount();
            } else if ("FAILED".equals(operationLog.getStatus())) {
                statistics.incrementFailedCount();
            } else if ("ERROR".equals(operationLog.getStatus())) {
                statistics.incrementErrorCount();
            }

            // 更新执行时间
            if (operationLog.getExecutionTime() != null) {
                statistics.updateExecutionTime(operationLog.getExecutionTime());
            }

            logStatisticsRepository.save(statistics);
        } catch (Exception e) {
            log.error("更新统计信息失败", e);
        }
    }

    /**
     * 定时清理过期日志
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    public void cleanupExpiredLogs() {
        log.info("开始清理过期日志...");

        try {
            List<LogCleanupConfig> configs = logCleanupConfigRepository.findByEnabledTrue();
            LocalDateTime now = LocalDateTime.now();
            int totalDeleted = 0;

            for (LogCleanupConfig config : configs) {
                LocalDateTime cutoffTime = now.minusMonths(config.getRetentionMonths());

                // 清理操作日志
                int deletedLogs = operationLogRepository.deleteLogsBefore(cutoffTime, config.getLogType());
                totalDeleted += deletedLogs;

                log.info("清理日志类型 {} 的过期数据 {} 条，保留期限 {} 个月",
                        config.getLogType(), deletedLogs, config.getRetentionMonths());
            }

            // 清理过期的敏感数据备份（保留更长时间）
            LocalDateTime sensitiveDataCutoffTime = now.minusMonths(60); // 5年
            int deletedSensitiveData = sensitiveDataBackupRepository.deleteOldData(sensitiveDataCutoffTime);
            totalDeleted += deletedSensitiveData;

            // 清理过期的统计数据（保留2年）
            LocalDate statisticsCutoffDate = now.minusMonths(24).toLocalDate();
            int deletedStatistics = logStatisticsRepository.deleteOldStatistics(statisticsCutoffDate);
            totalDeleted += deletedStatistics;

            log.info("日志清理完成，总共清理 {} 条记录", totalDeleted);
        } catch (Exception e) {
            log.error("清理过期日志失败", e);
        }
    }

    /**
     * 获取最活跃的用户
     */
    @Transactional(readOnly = true)
    public List<Object[]> getMostActiveUsers(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        try {
            // 如果时间范围为空，设置默认范围为所有数据
            if (startTime == null) {
                startTime = LocalDateTime.of(2020, 1, 1, 0, 0);
            }
            if (endTime == null) {
                endTime = LocalDateTime.now();
            }

            List<Object[]> result = operationLogRepository.findMostActiveUsers(startTime, endTime, pageable);

            // 记录调试信息
            if (result.isEmpty()) {
                log.debug("未找到活跃用户数据，时间范围: {} - {}", startTime, endTime);
            } else {
                log.debug("找到 {} 个活跃用户", result.size());
                for (Object[] user : result) {
                    log.debug("活跃用户: userEmail={}, count={}",
                             user.length > 0 ? user[0] : "null",
                             user.length > 1 ? user[1] : "null");
                }
            }

            return result;
        } catch (Exception e) {
            log.error("获取最活跃用户失败", e);
            return new java.util.ArrayList<>();
        }
    }

    /**
     * 获取最频繁的操作
     */
    @Transactional(readOnly = true)
    public List<Object[]> getMostFrequentOperations(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        try {
            // 如果时间范围为空，设置默认范围为所有数据
            if (startTime == null) {
                startTime = LocalDateTime.of(2020, 1, 1, 0, 0);
            }
            if (endTime == null) {
                endTime = LocalDateTime.now();
            }

            List<Object[]> result = operationLogRepository.findMostFrequentOperations(startTime, endTime, pageable);

            // 记录调试信息
            if (result.isEmpty()) {
                log.debug("未找到频繁操作数据，时间范围: {} - {}", startTime, endTime);
            } else {
                log.debug("找到 {} 个频繁操作", result.size());
                for (Object[] operation : result) {
                    log.debug("频繁操作: operation={}, count={}",
                             operation.length > 0 ? operation[0] : "null",
                             operation.length > 1 ? operation[1] : "null");
                }
            }

            return result;
        } catch (Exception e) {
            log.error("获取最频繁操作失败", e);
            return new java.util.ArrayList<>();
        }
    }

    /**
     * 导出日志数据
     */
    @Transactional(readOnly = true)
    public List<OperationLog> exportLogs(String logType, Long userId, String operation,
                                        LocalDateTime startTime, LocalDateTime endTime) {
        // 实现日志导出逻辑
        // 这里可以调用相应的查询方法
        // 返回Page对象，然后转换为List
        Page<OperationLog> page = operationLogRepository.findByMultipleConditions(
                logType, userId, null, operation, null, null, null, startTime, endTime, null);
        return page.getContent();
    }

    /**
     * 将对象转换为JSON字符串
     */
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("对象序列化为JSON失败", e);
            return "{}";
        }
    }
}