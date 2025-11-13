-- 创建邮件群发记录表
CREATE TABLE IF NOT EXISTS bulk_email (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    subject VARCHAR(255) NOT NULL COMMENT '邮件主题',
    content TEXT NOT NULL COMMENT '邮件内容',
    recipient_count INT NOT NULL DEFAULT 0 COMMENT '收件人数量',
    sender_email VARCHAR(255) NOT NULL COMMENT '发送邮箱',
    send_status VARCHAR(50) NOT NULL DEFAULT 'SUCCESS' COMMENT '发送状态(SUCCESS/FAILED/PARTIAL)',
    failure_count INT NOT NULL DEFAULT 0 COMMENT '失败数量',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NULL COMMENT '更新时间',
    INDEX idx_created_at (created_at) COMMENT '创建时间索引',
    INDEX idx_send_status (send_status) COMMENT '发送状态索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件群发记录表';

-- 插入测试数据（可选）
-- INSERT INTO bulk_email (subject, content, recipient_count, sender_email, send_status)
-- VALUES
-- ('系统维护通知', '尊敬的用户，系统将于今晚进行维护...', 100, 'admin@example.com', 'SUCCESS'),
-- ('新功能发布', '我们很高兴地宣布新功能上线...', 150, 'admin@example.com', 'SUCCESS');