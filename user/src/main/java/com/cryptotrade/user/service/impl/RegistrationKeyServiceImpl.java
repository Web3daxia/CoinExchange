/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service.impl;

import com.cryptotrade.user.entity.UserRegistrationKey;
import com.cryptotrade.user.repository.UserRegistrationKeyRepository;
import com.cryptotrade.user.service.RegistrationKeyService;
import com.cryptotrade.user.util.RegistrationKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 注册私钥服务实现
 */
@Service
public class RegistrationKeyServiceImpl implements RegistrationKeyService {
    
    @Autowired
    private UserRegistrationKeyRepository registrationKeyRepository;
    
    @Override
    @Transactional
    public UserRegistrationKey generateRegistrationKey(Long userId) {
        // 检查是否已存在
        Optional<UserRegistrationKey> existing = registrationKeyRepository.findByUserId(userId);
        if (existing.isPresent()) {
            throw new RuntimeException("用户已存在注册私钥");
        }
        
        // 生成新的注册私钥
        String registrationKey;
        do {
            registrationKey = RegistrationKeyGenerator.generateRegistrationKey();
        } while (registrationKeyRepository.existsByRegistrationKey(registrationKey));
        
        // 保存到数据库
        UserRegistrationKey userRegistrationKey = new UserRegistrationKey();
        userRegistrationKey.setUserId(userId);
        userRegistrationKey.setRegistrationKey(registrationKey);
        userRegistrationKey.setStatus(1);
        
        return registrationKeyRepository.save(userRegistrationKey);
    }
    
    @Override
    public UserRegistrationKey getRegistrationKey(Long userId) {
        return registrationKeyRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("未找到用户的注册私钥"));
    }
    
    @Override
    public Long getUserIdByRegistrationKey(String registrationKey) {
        UserRegistrationKey userRegistrationKey = registrationKeyRepository.findByRegistrationKey(registrationKey)
                .orElseThrow(() -> new RuntimeException("无效的注册私钥"));
        
        if (userRegistrationKey.getStatus() == 0) {
            throw new RuntimeException("注册私钥已失效");
        }
        
        return userRegistrationKey.getUserId();
    }
    
    @Override
    @Transactional
    public void invalidateRegistrationKey(Long userId) {
        UserRegistrationKey userRegistrationKey = getRegistrationKey(userId);
        userRegistrationKey.setStatus(0);
        registrationKeyRepository.save(userRegistrationKey);
    }
    
    @Override
    @Transactional
    public UserRegistrationKey regenerateRegistrationKey(Long userId) {
        // 使旧的私钥失效
        Optional<UserRegistrationKey> existing = registrationKeyRepository.findByUserId(userId);
        if (existing.isPresent()) {
            existing.get().setStatus(0);
            registrationKeyRepository.save(existing.get());
        }
        
        // 生成新的注册私钥
        return generateRegistrationKey(userId);
    }
}














