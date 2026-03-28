/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service;

import com.cryptotrade.user.entity.UserRegistrationKey;

/**
 * 注册私钥服务接口
 */
public interface RegistrationKeyService {
    
    /**
     * 为用户生成注册私钥
     * @param userId 用户ID
     * @return 注册私钥实体
     */
    UserRegistrationKey generateRegistrationKey(Long userId);
    
    /**
     * 获取用户的注册私钥
     * @param userId 用户ID
     * @return 注册私钥
     */
    UserRegistrationKey getRegistrationKey(Long userId);
    
    /**
     * 根据注册私钥查找用户ID
     * @param registrationKey 注册私钥
     * @return 用户ID
     */
    Long getUserIdByRegistrationKey(String registrationKey);
    
    /**
     * 使注册私钥失效
     * @param userId 用户ID
     */
    void invalidateRegistrationKey(Long userId);
    
    /**
     * 重新生成注册私钥
     * @param userId 用户ID
     * @return 新的注册私钥
     */
    UserRegistrationKey regenerateRegistrationKey(Long userId);
}














