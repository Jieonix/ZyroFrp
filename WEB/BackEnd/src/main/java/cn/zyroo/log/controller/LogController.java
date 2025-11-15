package cn.zyroo.all.log.controller;

import cn.zyroo.all.log.model.OperationLog;
import cn.zyroo.all.log.model.SensitiveDataBackup;
import cn.zyroo.all.log.service.LogService;
import cn.zyroo.all.common.utils.ApiResponse;
import cn.zyroo.all.user.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志管理控制器
 * 提供日志查询、统计、导出等功能的API接口
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
public class LogController {

    private static final Logger log = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取所有日志（不分页）
     */
    @GetMapping("/all")
    public ApiResponse<?> getAllLogs(
            @RequestParam(required = false) String logType,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestHeader(value = "Authorization", required = false) String token) {

        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问日志数据");
            }

            // 创建排序参数
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);

            // 查询所有日志（不分页）
            List<OperationLog> logs = logService.getAllLogs(
                    logType, userId, userEmail, operation, module, ipAddress, status, startTime, endTime, sort);

            return ApiResponse.success(logs);
        } catch (Exception e) {
            log.error("查询所有日志失败", e);
            return ApiResponse.error("500", "查询日志失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询日志
     */
    @GetMapping
    public ApiResponse<?> getLogs(
            @RequestParam(required = false) String logType,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestHeader(value = "Authorization", required = false) String token) {

        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问日志数据");
            }

            // 创建分页和排序参数
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            // 查询日志
            Page<OperationLog> logs = logService.getLogsByPage(
                    logType, userId, userEmail, operation, module, ipAddress, status, startTime, endTime, pageable);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("content", logs.getContent());
            result.put("totalElements", logs.getTotalElements());
            result.put("totalPages", logs.getTotalPages());
            result.put("currentPage", logs.getNumber());
            result.put("pageSize", logs.getSize());
            result.put("first", logs.isFirst());
            result.put("last", logs.isLast());

            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("查询日志失败", e);
            return ApiResponse.error("500", "查询日志失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询日志详情
     */
    @GetMapping("/{id}")
    public ApiResponse<?> getLogById(@PathVariable Long id,
                                   @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问日志详情");
            }

            OperationLog operationLog = logService.getLogById(id);
            if (operationLog == null) {
                return ApiResponse.error("404", "日志不存在");
            }

            return ApiResponse.success(operationLog);
        } catch (Exception e) {
            log.error("查询日志详情失败: id={}", id, e);
            return ApiResponse.error("500", "查询日志详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取日志的敏感数据
     */
    @GetMapping("/{id}/sensitive-data")
    public ApiResponse<?> getSensitiveData(@PathVariable Long id,
                                         @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 验证超级管理员权限
            if (!hasSuperAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问敏感数据");
            }

            List<SensitiveDataBackup> sensitiveData = logService.getSensitiveDataByLogId(id);

            // 解密敏感数据
            for (SensitiveDataBackup backup : sensitiveData) {
                try {
                    String decryptedData = logService.decryptSensitiveData(
                            backup.getEncryptedData(), backup.getEncryptionKeyId());
                    // 这里可以创建一个DTO来包含解密后的数据
                } catch (Exception e) {
                    log.warn("解密敏感数据失败: logId={}, dataType={}", id, backup.getDataType(), e);
                }
            }

            return ApiResponse.success(sensitiveData);
        } catch (Exception e) {
            log.error("获取敏感数据失败: logId={}", id, e);
            return ApiResponse.error("500", "获取敏感数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取日志统计信息
     */
    @GetMapping("/statistics")
    public ApiResponse<?> getLogStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestHeader(value = "Authorization", required = false) String token) {

        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问统计信息");
            }

            // 设置默认时间范围（最近7天）
            if (startTime == null) {
                startTime = LocalDateTime.now().minusDays(7);
            }
            if (endTime == null) {
                endTime = LocalDateTime.now();
            }

            Map<String, Object> statistics = logService.getLogStatistics(startTime, endTime);

            return ApiResponse.success(statistics);
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return ApiResponse.error("500", "获取统计信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户操作历史
     */
    @GetMapping("/user/{userId}/history")
    public ApiResponse<?> getUserOperationHistory(@PathVariable Long userId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size,
                                                @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 验证管理员权限或用户本人
            if (!hasAdminPermission(token) && !isCurrentUser(userId, token)) {
                return ApiResponse.error("403", "无权限访问用户操作历史");
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
            List<OperationLog> logs = logService.getUserOperationHistory(userId, pageable);

            return ApiResponse.success(logs);
        } catch (Exception e) {
            log.error("获取用户操作历史失败: userId={}", userId, e);
            return ApiResponse.error("500", "获取用户操作历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取IP操作历史
     */
    @GetMapping("/ip/{ipAddress}/history")
    public ApiResponse<?> getIpOperationHistory(@PathVariable String ipAddress,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问IP操作历史");
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
            List<OperationLog> logs = logService.getIpOperationHistory(ipAddress, pageable);

            return ApiResponse.success(logs);
        } catch (Exception e) {
            log.error("获取IP操作历史失败: ipAddress={}", ipAddress, e);
            return ApiResponse.error("500", "获取IP操作历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取失败日志
     */
    @GetMapping("/failed")
    public ApiResponse<?> getFailedLogs(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestHeader(value = "Authorization", required = false) String token) {

        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问失败日志");
            }

            // 设置默认时间范围（最近24小时）
            if (startTime == null) {
                startTime = LocalDateTime.now().minusHours(24);
            }
            if (endTime == null) {
                endTime = LocalDateTime.now();
            }

            List<OperationLog> failedLogs = logService.getFailedLogs(startTime, endTime);

            return ApiResponse.success(failedLogs);
        } catch (Exception e) {
            log.error("获取失败日志失败", e);
            return ApiResponse.error("500", "获取失败日志失败: " + e.getMessage());
        }
    }

    /**
     * 获取安全日志
     */
    @GetMapping("/security")
    public ApiResponse<?> getSecurityLogs(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "50") int size,
                                        @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问安全日志");
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
            List<OperationLog> securityLogs = logService.getSecurityLogs(pageable);

            return ApiResponse.success(securityLogs);
        } catch (Exception e) {
            log.error("获取安全日志失败", e);
            return ApiResponse.error("500", "获取安全日志失败: " + e.getMessage());
        }
    }

    /**
     * 搜索日志
     */
    @GetMapping("/search")
    public ApiResponse<?> searchLogs(@RequestParam String keyword,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size,
                                    @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限搜索日志");
            }

            // 设置默认时间范围（最近7天）
            if (startTime == null) {
                startTime = LocalDateTime.now().minusDays(7);
            }
            if (endTime == null) {
                endTime = LocalDateTime.now();
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
            Page<OperationLog> logs = logService.searchLogs(keyword, startTime, endTime, pageable);

            return ApiResponse.success(logs);
        } catch (Exception e) {
            log.error("搜索日志失败: keyword={}", keyword, e);
            return ApiResponse.error("500", "搜索日志失败: " + e.getMessage());
        }
    }

    /**
     * 导出日志数据
     */
    @GetMapping("/export")
    public ResponseEntity<?> exportLogs(
            @RequestParam(required = false) String logType,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "json") String format,
            @RequestHeader(value = "Authorization", required = false) String token) {

        try {
            // 验证超级管理员权限
            if (!hasSuperAdminPermission(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("无权限导出日志数据");
            }

            // 设置默认时间范围（最近30天）
            if (startTime == null) {
                startTime = LocalDateTime.now().minusDays(30);
            }
            if (endTime == null) {
                endTime = LocalDateTime.now();
            }

            List<OperationLog> logs = logService.exportLogs(logType, userId, operation, startTime, endTime);

            // 根据格式导出数据
            if ("csv".equalsIgnoreCase(format)) {
                String csvContent = convertToCsv(logs);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logs.csv")
                        .contentType(MediaType.parseMediaType("text/csv"))
                        .body(csvContent);
            } else {
                // 默认JSON格式
                String jsonContent = logService.toJson(logs);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=logs.json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jsonContent);
            }

        } catch (Exception e) {
            log.error("导出日志失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("导出日志失败: " + e.getMessage());
        }
    }

    /**
     * 获取最活跃的用户
     */
    @GetMapping("/analytics/active-users")
    public ApiResponse<?> getMostActiveUsers(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "10") int limit,
            @RequestHeader(value = "Authorization", required = false) String token) {

        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问分析数据");
            }

            // 设置默认时间范围（最近7天）
            if (startTime == null) {
                startTime = LocalDateTime.now().minusDays(7);
            }
            if (endTime == null) {
                endTime = LocalDateTime.now();
            }

            Pageable pageable = PageRequest.of(0, limit);
            List<Object[]> activeUsers = logService.getMostActiveUsers(startTime, endTime, pageable);

            return ApiResponse.success(activeUsers);
        } catch (Exception e) {
            log.error("获取最活跃用户失败", e);
            return ApiResponse.error("500", "获取最活跃用户失败: " + e.getMessage());
        }
    }

    /**
     * 获取最频繁的操作
     */
    @GetMapping("/analytics/frequent-operations")
    public ApiResponse<?> getMostFrequentOperations(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "10") int limit,
            @RequestHeader(value = "Authorization", required = false) String token) {

        try {
            // 验证管理员权限
            if (!hasAdminPermission(token)) {
                return ApiResponse.error("403", "无权限访问分析数据");
            }

            // 设置默认时间范围（最近7天）
            if (startTime == null) {
                startTime = LocalDateTime.now().minusDays(7);
            }
            if (endTime == null) {
                endTime = LocalDateTime.now();
            }

            Pageable pageable = PageRequest.of(0, limit);
            List<Object[]> frequentOperations = logService.getMostFrequentOperations(startTime, endTime, pageable);

            return ApiResponse.success(frequentOperations);
        } catch (Exception e) {
            log.error("获取最频繁操作失败", e);
            return ApiResponse.error("500", "获取最频繁操作失败: " + e.getMessage());
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 检查是否有管理员权限
     */
    private boolean hasAdminPermission(String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return false;
            }

            token = token.substring(7);
            String role = jwtUtil.getRoleFromToken(token);
            return "Admin".equals(role) || "SuperAdmin".equals(role);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查是否有超级管理员权限
     */
    private boolean hasSuperAdminPermission(String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return false;
            }

            token = token.substring(7);
            String role = jwtUtil.getRoleFromToken(token);
            return "SuperAdmin".equals(role);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查是否为当前用户本人
     */
    private boolean isCurrentUser(Long userId, String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return false;
            }

            token = token.substring(7);
            String email = jwtUtil.getEmailFromToken(token);
            // 这里需要根据邮箱获取用户ID进行比较
            // Users user = usersService.findByEmail(email);
            // return user != null && user.getUser_id().equals(userId);
            return false; // 暂时返回false，需要实现用户服务
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 转换为CSV格式
     */
    private String convertToCsv(List<OperationLog> logs) {
        StringBuilder csv = new StringBuilder();

        // CSV头部
        csv.append("ID,日志类型,用户ID,用户邮箱,操作,模块,描述,请求方法,请求URL,IP地址,状态,执行时间,创建时间\n");

        // 数据行
        for (OperationLog log : logs) {
            csv.append(String.format("%d,%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%d,%s\n",
                    log.getId(),
                    log.getLogType(),
                    log.getUserId(),
                    log.getUserEmail(),
                    log.getOperation(),
                    log.getModule(),
                    log.getDescription(),
                    log.getRequestMethod(),
                    log.getRequestUrl(),
                    log.getIpAddress(),
                    log.getStatus(),
                    log.getExecutionTime(),
                    log.getCreatedTime()
            ));
        }

        return csv.toString();
    }
}