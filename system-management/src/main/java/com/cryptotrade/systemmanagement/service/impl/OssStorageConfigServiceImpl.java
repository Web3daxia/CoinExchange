/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.OssStorageConfig;
import com.cryptotrade.systemmanagement.repository.OssStorageConfigRepository;
import com.cryptotrade.systemmanagement.service.OssStorageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OssStorageConfigServiceImpl implements OssStorageConfigService {
    
    @Autowired
    private OssStorageConfigRepository repository;
    
    @Override
    @Transactional
    public OssStorageConfig createConfig(OssStorageConfig config) {
        if (repository.existsByProvider(config.getProvider())) {
            throw new RuntimeException("该OSS提供商配置已存在");
        }
        return repository.save(config);
    }
    
    @Override
    @Transactional
    public OssStorageConfig updateConfig(Long id, OssStorageConfig config) {
        OssStorageConfig existing = repository.findById(id)
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
    public OssStorageConfig getConfigById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("配置不存在"));
    }
    
    @Override
    public OssStorageConfig getDefaultConfig() {
        return repository.findByEnabledTrueAndIsDefaultTrue()
                .orElseThrow(() -> new RuntimeException("未找到默认OSS配置"));
    }
    
    @Override
    public List<OssStorageConfig> getAllConfigs() {
        return repository.findAll();
    }
    
    @Override
    public List<OssStorageConfig> getEnabledConfigs() {
        return repository.findByEnabledTrue();
    }
    
    @Override
    @Transactional
    public void setDefault(Long id) {
        repository.findAll().forEach(config -> {
            config.setIsDefault(false);
            repository.save(config);
        });
        OssStorageConfig config = getConfigById(id);
        config.setIsDefault(true);
        config.setEnabled(true);
        repository.save(config);
    }
}














