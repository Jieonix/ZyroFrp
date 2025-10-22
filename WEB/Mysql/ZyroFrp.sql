DROP DATABASE ZyroFrp;

CREATE DATABASE ZyroFrp;







USE ZyroFrp;

DROP TABLE IF EXISTS Users;

CREATE TABLE Users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_key VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    vip_status INT(1) DEFAULT 0 NOT NULL,
    role VARCHAR(20) NOT NULL,
    real_name_status VARCHAR(1) DEFAULT '0' NOT NULL,
    real_name VARCHAR(100) DEFAULT NULL,
    id_card VARCHAR(100) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    remaining_traffic BIGINT NOT NULL ,
    upload_limit BIGINT NOT NULL ,
    download_limit BIGINT NOT NULL
);

ALTER TABLE Users AUTO_INCREMENT 10000;

DELIMITER $$

CREATE TRIGGER set_vip_status_before_insert
BEFORE INSERT ON Users
FOR EACH ROW
BEGIN
    IF NEW.role = 'ADMIN' OR NEW.role = 'SUPER_ADMIN' THEN
        SET NEW.vip_status = 1;
    END IF;
END$$

DELIMITER ;


ALTER TABLE Users
ADD COLUMN real_name_status VARCHAR(1) DEFAULT '0' NOT NULL,  -- 添加实名状态字段，默认为 '0'
ADD COLUMN real_name VARCHAR(100) DEFAULT NULL,  -- 添加姓名字段，允许为空
ADD COLUMN id_card VARCHAR(20) DEFAULT NULL;  -- 添加身份证号字段，允许为空


ALTER TABLE Users
MODIFY COLUMN id_card VARCHAR(100) DEFAULT NULL;  -- 修改 id_card 字段的长度为 100







DROP TABLE Email;

CREATE TABLE Email(
    id BIGINT NOT NULL AUTO_INCREMENT,           -- 唯一ID
    email VARCHAR(255) NOT NULL,                 -- 用户邮箱（如果是通过邮箱验证）
    code VARCHAR(10) NOT NULL,                   -- 验证码（存储验证码内容）
    created_at DATETIME(6) NOT NULL,             -- 创建时间，后端处理
    expired_at DATETIME(6) NOT NULL,             -- 过期时间
    status INT DEFAULT 0 NOT NULL,               -- 状态：已使用、待验证、已过期
    type VARCHAR(50) NOT NULL,                   -- 验证码用途（如：注册、找回密码等）
    PRIMARY KEY (id),                            -- 主键
    INDEX (email)                                -- 索引：优化根据邮箱查询
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;








DROP TABLE Announcements;

CREATE TABLE Announcements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,   -- 唯一ID
    title VARCHAR(255) NOT NULL,            -- 公告标题
    content TEXT NOT NULL,                  -- 公告内容
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL ,  -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL , -- 更新时间
    is_pinned INT DEFAULT 0          -- 是否置顶（0: 否，1: 是）
);







CREATE TABLE questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, -- 题目ID
    question_text TEXT NOT NULL,          -- 题目内容
    option_a VARCHAR(255) NOT NULL,       -- 选项 A
    option_b VARCHAR(255) NOT NULL,       -- 选项 B
    option_c VARCHAR(255) NOT NULL,       -- 选项 C
    option_d VARCHAR(255) NOT NULL,       -- 选项 D
    correct_answer CHAR(1) NOT NULL,      -- 正确答案 (A/B/C/D)
    created_at DATETIME DEFAULT NOW()     -- 题目创建时间
);

DROP TABLE user_answers;

CREATE TABLE user_answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,           -- 关联用户的邮箱
    question_id BIGINT NOT NULL,          -- 题目ID
    user_answer CHAR(1) NOT NULL,         -- 用户提交的答案
    is_correct BOOLEAN NOT NULL,          -- 是否答对
    answered_at DATE NOT NULL,            -- 答题日期
    UNIQUE KEY (email, answered_at)       -- 确保每天只能答一次（只答一道题）
);




ALTER TABLE frp_tunnels DROP FOREIGN KEY fk_server_name;

-- 删除 Servers 表（如果存在）
DROP TABLE IF EXISTS Servers;

-- 创建 Servers 表
CREATE TABLE Servers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,        -- 服务器名称，添加唯一约束
    ip_address VARCHAR(255) NOT NULL,  -- 服务器IP地址
    port INT NOT NULL DEFAULT 7788,    -- frps 监听端口
    max_clients INT DEFAULT 100,       -- 最大用户数
    current_clients INT DEFAULT 0,     -- 当前连接用户数
    vip_only BOOLEAN DEFAULT FALSE,    -- 是否仅限 VIP 使用
    status ENUM('ONLINE', 'OFFLINE') DEFAULT 'ONLINE',  -- 服务器状态
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- 更新时间
);

ALTER TABLE frp_tunnels DROP COLUMN status;
ALTER TABLE frp_tunnels ADD COLUMN status ENUM('active', 'paused', 'unused') DEFAULT 'unused';










USE ZyroFrp;

-- 删除已有的触发器
DROP TRIGGER IF EXISTS before_insert_frp_tunnels_server_ip;
DROP TRIGGER IF EXISTS before_update_frp_tunnels_server_ip;
DROP TRIGGER IF EXISTS after_update_servers_ip;
DROP TRIGGER IF EXISTS before_insert_frp_tunnels_user_key;
DROP TRIGGER IF EXISTS before_update_frp_tunnels_user_key;
DROP TRIGGER IF EXISTS after_update_users_user_key;


-- 删除 frp_tunnels 表（如果存在）
DROP TABLE IF EXISTS frp_tunnels;

-- 创建 frp_tunnels 表
CREATE TABLE frp_tunnels (
  id INT AUTO_INCREMENT PRIMARY KEY,          -- 主键
  user_email VARCHAR(255) NOT NULL,           -- 用户邮箱
  user_key VARCHAR(255) NOT NULL,             -- 用户密钥
  server_name VARCHAR(255) NOT NULL,          -- 服务器名称
  tunnel_name VARCHAR(255) NOT NULL,          -- 隧道名称
  tunnel_type VARCHAR(10) NOT NULL,           -- 隧道类型
  local_ip VARCHAR(15) NOT NULL,              -- 本地 IP 地址
  local_port INT NOT NULL CHECK (local_port < 65535), -- 本地端口
  remote_port INT CHECK (remote_port >= 10000 AND remote_port < 65535), -- 远程端口
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  -- 更新时间
  server_ip VARCHAR(255),                     -- 服务器 IP 地址
  custom_domain VARCHAR(255) NULL,             -- 用于 http/https 的自定义域名
  secret_key VARCHAR(255) NULL,                -- 用于 xtcp/stcp 的密钥
  UNIQUE (server_name, remote_port),          -- 唯一约束，server_name 和 remote_port 的组合
  UNIQUE (server_name, tunnel_name),          -- 唯一约束，server_name 和 tunnel_name 的组合
  CONSTRAINT fk_server_name FOREIGN KEY (server_name) REFERENCES Servers(name), -- 外键约束
  CONSTRAINT chk_custom_domain_http_https CHECK (
    (tunnel_type IN ('http', 'https') AND custom_domain IS NOT NULL) OR
    (tunnel_type NOT IN ('http', 'https') AND custom_domain IS NULL)
  ), -- 确保 http/https 类型有 custom_domain
  CONSTRAINT chk_secret_key_xtcp_stcp CHECK (
    (tunnel_type IN ('xtcp', 'stcp') AND secret_key IS NOT NULL) OR
    (tunnel_type NOT IN ('xtcp', 'stcp') AND secret_key IS NULL)
  ) -- 确保 xtcp/stcp 类型有 secret_key
);

-- 创建触发器，用于插入或更新 frp_tunnels 表时自动填充 server_ip 字段
DELIMITER $$

-- 插入触发器：插入时根据 server_name 获取 IP 并填充到 server_ip 字段
CREATE TRIGGER before_insert_frp_tunnels_server_ip
BEFORE INSERT ON frp_tunnels
FOR EACH ROW
BEGIN
    -- 根据 server_name 从 Servers 表中获取 ip_address 并填充到 server_ip 字段
    SET NEW.server_ip = (SELECT ip_address FROM Servers WHERE name = NEW.server_name LIMIT 1);
END$$

-- 更新触发器：更新时也根据 server_name 更新 server_ip 字段
CREATE TRIGGER before_update_frp_tunnels_server_ip
BEFORE UPDATE ON frp_tunnels
FOR EACH ROW
BEGIN
    -- 根据 server_name 更新 server_ip 字段
    SET NEW.server_ip = (SELECT ip_address FROM Servers WHERE name = NEW.server_name LIMIT 1);
END$$

-- 创建触发器，更新 Servers 表中的 IP 地址时，自动更新 frp_tunnels 中的 server_ip 字段
CREATE TRIGGER after_update_servers_ip
AFTER UPDATE ON Servers
FOR EACH ROW
BEGIN
    -- 更新 frp_tunnels 中所有与该 server_name 相关的记录的 server_ip
    UPDATE frp_tunnels
    SET server_ip = NEW.ip_address
    WHERE server_name = OLD.name;
END$$

-- 插入触发器：根据 user_email 填充 user_key
CREATE TRIGGER before_insert_frp_tunnels_user_key
BEFORE INSERT ON frp_tunnels
FOR EACH ROW
BEGIN
    -- 根据 user_email 从 Users 表获取对应的 user_key
    SET NEW.user_key = (SELECT user_key FROM Users WHERE email = NEW.user_email LIMIT 1);
END$$

-- 更新触发器：更新时根据 user_email 更新 user_key
CREATE TRIGGER before_update_frp_tunnels_user_key
BEFORE UPDATE ON frp_tunnels
FOR EACH ROW
BEGIN
    -- 根据 user_email 从 Users 表获取新的 user_key 并填充到 user_key 字段
    SET NEW.user_key = (SELECT user_key FROM Users WHERE email = NEW.user_email LIMIT 1);
END$$

-- 更新触发器：当 Users 表中的 user_key 更新时，更新 frp_tunnels 中的 user_key 字段
CREATE TRIGGER after_update_users_user_key
AFTER UPDATE ON Users
FOR EACH ROW
BEGIN
    -- 更新 frp_tunnels 中所有与该 user_email 相关的记录的 user_key
    UPDATE frp_tunnels
    SET user_key = NEW.user_key
    WHERE user_email = OLD.email;
END$$

DELIMITER ;

