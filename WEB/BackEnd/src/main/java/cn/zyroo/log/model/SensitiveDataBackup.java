package cn.zyroo.log.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 敏感数据备份实体类
 * 用于存储脱敏前的原始敏感数据，支持必要时候的解密查看
 *
 * @author Claude
 * @version 1.0
 * @since 2025-11-14
 */
@Entity
@Table(name = "sensitive_data_backup", indexes = {
    @Index(name = "idx_log_id", columnList = "logId"),
    @Index(name = "idx_data_type", columnList = "dataType"),
    @Index(name = "idx_created_time", columnList = "createdTime")
})
public class SensitiveDataBackup {

    /**
     * 敏感数据类型枚举
     */
    public enum DataType {
        PASSWORD("密码"),
        TOKEN("令牌"),
        API_KEY("密钥"),
        ID_CARD("身份证"),
        PHONE("手机号"),
        EMAIL("邮箱"),
        BANK_CARD("银行卡"),
        PRIVATE_KEY("私钥"),
        SECRET_KEY("密钥"),
        OTHER("其他");

        private final String description;

        DataType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sensitive_data_log"))
    private OperationLog log;

    @Column(name = "log_id", insertable = false, updatable = false)
    private Long logId;

    @Column(name = "data_type", nullable = false, length = 50)
    private String dataType;

    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;

    @Column(name = "encrypted_data", nullable = false, columnDefinition = "TEXT")
    private String encryptedData;

    @Column(name = "encryption_key_id", length = 50)
    private String encryptionKeyId;

    @Column(name = "original_length")
    private Integer originalLength;

    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }

    // 构造函数
    public SensitiveDataBackup() {}

    public SensitiveDataBackup(OperationLog log, String dataType, String fieldName, String encryptedData) {
        this.log = log;
        this.dataType = dataType;
        this.fieldName = fieldName;
        this.encryptedData = encryptedData;
        if (encryptedData != null) {
            this.originalLength = encryptedData.length();
        }
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationLog getLog() {
        return log;
    }

    public void setLog(OperationLog log) {
        this.log = log;
        if (log != null) {
            this.logId = log.getId();
        }
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
        if (encryptedData != null) {
            this.originalLength = encryptedData.length();
        }
    }

    public String getEncryptionKeyId() {
        return encryptionKeyId;
    }

    public void setEncryptionKeyId(String encryptionKeyId) {
        this.encryptionKeyId = encryptionKeyId;
    }

    public Integer getOriginalLength() {
        return originalLength;
    }

    public void setOriginalLength(Integer originalLength) {
        this.originalLength = originalLength;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "SensitiveDataBackup{" +
                "id=" + id +
                ", logId=" + logId +
                ", dataType='" + dataType + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", encryptedData='[PROTECTED]'" +
                ", encryptionKeyId='" + encryptionKeyId + '\'' +
                ", originalLength=" + originalLength +
                ", createdTime=" + createdTime +
                '}';
    }
}