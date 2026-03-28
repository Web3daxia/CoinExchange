/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.CaptchaServiceConfig;
import com.cryptotrade.systemmanagement.repository.CaptchaServiceConfigRepository;
import com.cryptotrade.systemmanagement.service.CaptchaServiceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CaptchaServiceConfigServiceImpl implements CaptchaServiceConfigService {
    
    @Autowired
    private CaptchaServiceConfigRepository repository;
    
    @Override
    @Transactional
    public CaptchaServiceConfig createConfig(CaptchaServiceConfig config) {
        if (repository.existsByServiceProvider(config.getServiceProvider())) {
            throw new RuntimeException("该服务提供商配置已存在");
        }
        return repository.save(config);
    }
    
    @Override
    @Transactional
    public CaptchaServiceConfig updateConfig(Long id, CaptchaServiceConfig config) {
        CaptchaServiceConfig existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在"));
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        return repository.save(config);
    }
    
    @Override
    @Transactional
    public void deleteConfig(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public CaptchaServiceConfig getConfigById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("配置不存在"));
    }
    
    @Override
    public CaptchaServiceConfig getDefaultConfig() {
        return repository.findByEnabledTrueAndIsDefaultTrue()
                .orElseThrow(() -> new RuntimeException("未找到默认配置"));
    }
    
    @Override
    public List<CaptchaServiceConfig> getAllConfigs() {
        return repository.findAll();
    }
    
    @Override
    public List<CaptchaServiceConfig> getEnabledConfigs() {
        return repository.findByEnabledTrue();
    }
    
    @Override
    @Transactional
    public void setDefault(Long id) {
        // 取消所有默认
        repository.findAll().forEach(config -> {
            config.setIsDefault(false);
            repository.save(config);
        });
        // 设置新的默认
        CaptchaServiceConfig config = getConfigById(id);
        config.setIsDefault(true);
        config.setEnabled(true);
        repository.save(config);
    }
}














