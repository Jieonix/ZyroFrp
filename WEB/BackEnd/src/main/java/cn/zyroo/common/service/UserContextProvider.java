package cn.zyroo.common.service;

import cn.zyroo.common.dto.UserInfo;

import java.util.List;

/**
 * 用户上下文提供者接口
 * 定义通用的用户信息获取方法
 * 业务模块需要实现此接口
 */
public interface UserContextProvider {

    /**
     * 根据邮箱查找用户信息
     *
     * @param email 用户邮箱
     * @return 用户信息，如果不存在返回null
     */
    UserInfo findUserByEmail(String email);

    /**
     * 根据用户ID查找用户信息
     *
     * @param userId 用户ID
     * @return 用户信息，如果不存在返回null
     */
    UserInfo findUserById(Long userId);

    /**
     * 检查用户是否存在
     *
     * @param email 用户邮箱
     * @return 是否存在
     */
    boolean userExists(String email);

    /**
     * 保存用户信息
     *
     * @param userInfo 用户信息
     * @return 保存后的用户信息
     */
    UserInfo saveUser(UserInfo userInfo);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 获取所有用户邮箱列表
     *
     * @return 邮箱列表
     */
    List<String> findAllEmails();
}