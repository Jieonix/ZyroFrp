package cn.zyroo.log.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 日志清理配置实体类
 * 用于配置不同类型日志的保留策略和清理方式
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Entity
@Table(name = "log_cleanup_config",
    uniqueConstraints = @UniqueConstraint(name = "uk_log_type", columnNames = "logType")
)
public class LogCleanupConfig {

    /**
     * 清理策略枚举
     */
    public enum CleanupStrategy {
        DELETE("删除"),
        ARCHIVE("归档");

        private final String description;

        CleanupStrategy(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "log_type", nullable = false, length = 20, unique = true)
    private String logType;

    @Column(name = "retention_months", nullable = false)
    private Integer retentionMonths;

    @Column(name = "is_enabled")
    private Boolean enabled = true;

    @Column(name = "cleanup_strategy", length = 20)
    private String cleanupStrategy = CleanupStrategy.DELETE.name();

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

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
    public LogCleanupConfig() {}

    public LogCleanupConfig(String logType, Integer retentionMonths, String description) {
        this.logType = logType;
        this.retentionMonths = retentionMonths;
        this.description = description;
    }

    // 业务方法
    public CleanupStrategy getCleanupStrategyEnum() {
        if (cleanupStrategy != null) {
            return CleanupStrategy.valueOf(cleanupStrategy);
        }
        return CleanupStrategy.DELETE;
    }

    public void setCleanupStrategyEnum(CleanupStrategy strategy) {
        this.cleanupStrategy = strategy.name();
    }

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public Integer getRetentionMonths() {
        return retentionMonths;
    }

    public void setRetentionMonths(Integer retentionMonths) {
        this.retentionMonths = retentionMonths;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getCleanupStrategy() {
        return cleanupStrategy;
    }

    public void setCleanupStrategy(String cleanupStrategy) {
        this.cleanupStrategy = cleanupStrategy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "LogCleanupConfig{" +
                "id=" + id +
                ", logType='" + logType + '\'' +
                ", retentionMonths=" + retentionMonths +
                ", enabled=" + enabled +
                ", cleanupStrategy='" + cleanupStrategy + '\'' +
                ", description='" + description + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}