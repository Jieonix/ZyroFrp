package cn.zyroo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 通用用户信息DTO
 * 用于在公共模块和业务模块之间传递用户信息
 * 避免直接依赖业务实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户角色
     */
    private String role;

    /**
     * 用户密钥
     */
    private String userKey;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}