package cn.zyroo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日志统计实体类
 * 用于存储日志的统计信息，便于快速查询和分析
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Entity
@Table(name = "log_statistics",
    uniqueConstraints = @UniqueConstraint(name = "uk_stat_date_type",
        columnNames = {"statDate", "logType", "operation", "module"}),
    indexes = {
        @Index(name = "idx_stat_date", columnList = "statDate"),
        @Index(name = "idx_log_type", columnList = "logType"),
        @Index(name = "idx_operation", columnList = "operation"),
        @Index(name = "idx_module", columnList = "module")
    }
)
public class LogStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "log_type", nullable = false, length = 20)
    private String logType;

    @Column(name = "operation", length = 100)
    private String operation;

    @Column(name = "module", length = 50)
    private String module;

    @Column(name = "total_count")
    private Long totalCount = 0L;

    @Column(name = "success_count")
    private Long successCount = 0L;

    @Column(name = "failed_count")
    private Long failedCount = 0L;

    @Column(name = "error_count")
    private Long errorCount = 0L;

    @Column(name = "unique_users")
    private Long uniqueUsers = 0L;

    @Column(name = "unique_ips")
    private Long uniqueIps = 0L;

    @Column(name = "avg_execution_time", precision = 10, scale = 2)
    private BigDecimal avgExecutionTime = BigDecimal.ZERO;

    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }

    // 构造函数
    public LogStatistics() {}

    public LogStatistics(LocalDate statDate, String logType, String operation, String module) {
        this.statDate = statDate;
        this.logType = logType;
        this.operation = operation;
        this.module = module;
    }

    // 业务方法
    public void incrementTotalCount() {
        this.totalCount++;
    }

    public void incrementSuccessCount() {
        this.successCount++;
        this.totalCount++;
    }

    public void incrementFailedCount() {
        this.failedCount++;
        this.totalCount++;
    }

    public void incrementErrorCount() {
        this.errorCount++;
        this.totalCount++;
    }

    public void updateExecutionTime(Long executionTime) {
        if (executionTime != null && executionTime > 0) {
            // 计算新的平均执行时间
            if (this.totalCount > 0) {
                BigDecimal newTotal = this.avgExecutionTime.multiply(new BigDecimal(this.totalCount - 1))
                    .add(new BigDecimal(executionTime));
                this.avgExecutionTime = newTotal.divide(new BigDecimal(this.totalCount), 2, BigDecimal.ROUND_HALF_UP);
            } else {
                this.avgExecutionTime = new BigDecimal(executionTime);
            }
        }
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStatDate() {
        return statDate;
    }

    public void setStatDate(LocalDate statDate) {
        this.statDate = statDate;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Long successCount) {
        this.successCount = successCount;
    }

    public Long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }

    public Long getUniqueUsers() {
        return uniqueUsers;
    }

    public void setUniqueUsers(Long uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
    }

    public Long getUniqueIps() {
        return uniqueIps;
    }

    public void setUniqueIps(Long uniqueIps) {
        this.uniqueIps = uniqueIps;
    }

    public BigDecimal getAvgExecutionTime() {
        return avgExecutionTime;
    }

    public void setAvgExecutionTime(BigDecimal avgExecutionTime) {
        this.avgExecutionTime = avgExecutionTime;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "LogStatistics{" +
                "id=" + id +
                ", statDate=" + statDate +
                ", logType='" + logType + '\'' +
                ", operation='" + operation + '\'' +
                ", module='" + module + '\'' +
                ", totalCount=" + totalCount +
                ", successCount=" + successCount +
                ", failedCount=" + failedCount +
                ", errorCount=" + errorCount +
                ", uniqueUsers=" + uniqueUsers +
                ", uniqueIps=" + uniqueIps +
                ", avgExecutionTime=" + avgExecutionTime +
                '}';
    }
}