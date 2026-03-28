/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.repository;

import com.cryptotrade.user.entity.UserRegistrationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户注册私钥Repository
 */
@Repository
public interface UserRegistrationKeyRepository extends JpaRepository<UserRegistrationKey, Long> {
    
    /**
     * 根据用户ID查询注册私钥
     */
    Optional<UserRegistrationKey> findByUserId(Long userId);
    
    /**
     * 根据注册私钥查询
     */
    Optional<UserRegistrationKey> findByRegistrationKey(String registrationKey);
    
    /**
     * 检查用户ID是否存在
     */
    boolean existsByUserId(Long userId);
    
    /**
     * 检查注册私钥是否存在
     */
    boolean existsByRegistrationKey(String registrationKey);
}














