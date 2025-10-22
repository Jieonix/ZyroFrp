package db

import (
	"database/sql"
	"fmt"
	"log"

	_ "github.com/go-sql-driver/mysql"
)

// TunnelInfo 包含完整的隧道信息
type TunnelInfo struct {
	TunnelName   string
	TunnelType   string
	ServerName   string
	LocalPort    int
	RemotePort   *int
	CustomDomain *string
	SecretKey    *string
}

var DB *sql.DB

type Config struct {
	User     string
	Password string
	Host     string
	Port     int
	Name     string
}

func Init(cfg Config) error {
	log.Println("[DB] 🟢 开始初始化数据库连接...")
	log.Printf("[DB] 🔹 连接参数: user=%s, host=%s:%d, db=%s\n", cfg.User, cfg.Host, cfg.Port, cfg.Name)

	dsn := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s?parseTime=true",
		cfg.User, cfg.Password, cfg.Host, cfg.Port, cfg.Name)
	log.Printf("[DB] 🔹 DSN: %s\n", dsn)

	log.Printf("[DB] 🔹 正在创建数据库连接池...")
	var err error
	DB, err = sql.Open("mysql", dsn)
	if err != nil {
		log.Printf("[DB] ❌ sql.Open 失败: %v\n", err)
		return err
	}
	log.Println("[DB] ⚡ 连接池创建成功")

	log.Printf("[DB] 🔹 正在测试数据库连接...")
	if err = DB.Ping(); err != nil {
		log.Printf("[DB] ❌ 数据库 Ping 失败: %v\n", err)
		return err
	}

	log.Println("[DB] ✅ 数据库连接成功")
	log.Printf("[DB] 🔹 数据库连接池已就绪，可以处理查询")
	return nil
}

func ValidateUserKey(userKey string) (bool, error) {
	if DB == nil {
		log.Printf("[DB] 数据库未初始化，无法验证 userKey")
		return false, fmt.Errorf("数据库未初始化")
	}

	var exists bool
	err := DB.QueryRow("SELECT EXISTS(SELECT 1 FROM users WHERE user_key=?)", userKey).Scan(&exists)
	if err != nil {
		log.Printf("[DB] 查询用户 key 出错: %v", err)
		return false, err
	}

	if exists {
		log.Printf("[DB] 用户 key '%s' 验证通过", userKey)
	} else {
		log.Printf("[DB] 用户 key '%s' 验证失败", userKey)
	}

	return exists, nil
}

func GetUserTunnels(userKey string) ([]TunnelInfo, error) {
	if DB == nil {
		log.Printf("[DB] 数据库未初始化，无法查询用户隧道")
		return nil, fmt.Errorf("数据库未初始化")
	}

	rows, err := DB.Query("SELECT tunnel_name, tunnel_type, server_name, local_port, remote_port, custom_domain, secret_key FROM frp_tunnels WHERE user_key=?", userKey)
	if err != nil {
		log.Printf("[DB] 查询用户隧道列表出错: %v", err)
		return nil, err
	}
	defer rows.Close()

	var tunnels []TunnelInfo
	for rows.Next() {
		var tunnel TunnelInfo
		var remotePort sql.NullInt64
		var customDomain, secretKey sql.NullString

		if err := rows.Scan(
			&tunnel.TunnelName,
			&tunnel.TunnelType,
			&tunnel.ServerName,
			&tunnel.LocalPort,
			&remotePort,
			&customDomain,
			&secretKey,
		); err != nil {
			log.Printf("[DB] 扫描隧道信息出错: %v", err)
			return nil, err
		}

		// 处理可为空的字段
		if remotePort.Valid {
			port := int(remotePort.Int64)
			tunnel.RemotePort = &port
		}
		if customDomain.Valid {
			tunnel.CustomDomain = &customDomain.String
		}
		if secretKey.Valid {
			tunnel.SecretKey = &secretKey.String
		}

		tunnels = append(tunnels, tunnel)
	}

	if err := rows.Err(); err != nil {
		log.Printf("[DB] 遍历隧道列表出错: %v", err)
		return nil, err
	}

	log.Printf("[DB] 用户 '%s' 共有 %d 个隧道", userKey, len(tunnels))
	return tunnels, nil
}
