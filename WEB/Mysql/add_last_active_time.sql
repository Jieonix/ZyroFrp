-- 添加用户最后活跃时间字段
ALTER TABLE Users
ADD COLUMN last_active_time TIMESTAMP NULL DEFAULT NULL COMMENT '用户最后活跃时间';

-- 为最后活跃时间字段添加索引，提高查询性能
CREATE INDEX idx_users_last_active ON Users(last_active_time);