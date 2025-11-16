package cn.zyroo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    /**
     * VIP状态
     */
    private int vipStatus = 0;

    /**
     * 是否试用用户
     */
    private int isTrialUser = 1;

    /**
     * 剩余流量
     */
    private Long remainingTraffic = 4000L;

    /**
     * 上传限制
     */
    private Long uploadLimit = 1000L;

    /**
     * 下载限制
     */
    private Long downloadLimit = 1000L;

    /**
     * 实名认证状态
     */
    private int realNameStatus = 0;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * VIP开始时间
     */
    private LocalDate vipStartTime;

    /**
     * VIP结束时间
     */
    private LocalDate vipEndTime;

    /**
     * 最后活跃时间
     */
    private LocalDateTime lastActiveTime;
}