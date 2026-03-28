/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.TranslationServiceConfig;
import com.cryptotrade.systemmanagement.repository.TranslationServiceConfigRepository;
import com.cryptotrade.systemmanagement.service.TranslationServiceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 翻译服务配置Service实现
 */
@Service
public class TranslationServiceConfigServiceImpl implements TranslationServiceConfigService {

    @Autowired
    private TranslationServiceConfigRepository configRepository;

    @Override
    public List<TranslationServiceConfig> getAllConfigs() {
        return configRepository.findAllByOrderByPriorityDesc();
    }

    @Override
    public TranslationServiceConfig getConfigById(Long id) {
        return configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("翻译服务配置不存在: " + id));
    }

    @Override
    public TranslationServiceConfig getConfigByServiceCode(String serviceCode) {
        return configRepository.findByServiceCode(serviceCode)
                .orElseThrow(() -> new RuntimeException("翻译服务配置不存在: " + serviceCode));
    }

    @Override
    @Transactional
    public TranslationServiceConfig createConfig(TranslationServiceConfig config) {
        // 检查服务代码是否已存在
        if (configRepository.findByServiceCode(config.getServiceCode()).isPresent()) {
            throw new RuntimeException("服务代码已存在: " + config.getServiceCode());
        }

        config.setIsEnabled(false); // 新创建的配置默认禁用
        config.setStatus("ACTIVE");
        return configRepository.save(config);
    }

    @Override
    @Transactional
    public TranslationServiceConfig updateConfig(Long id, TranslationServiceConfig config) {
        TranslationServiceConfig existing = getConfigById(id);

        // 更新字段
        existing.setServiceName(config.getServiceName());
        existing.setApiUrl(config.getApiUrl());
        existing.setApiKey(config.getApiKey());
        existing.setApiSecret(config.getApiSecret());
        existing.setAppId(config.getAppId());
        existing.setRegion(config.getRegion());
        existing.setSourceLanguage(config.getSourceLanguage());
        existing.setTargetLanguages(config.getTargetLanguages());
        existing.setConfigParams(config.getConfigParams());
        existing.setPriority(config.getPriority());
        existing.setDailyLimit(config.getDailyLimit());
        existing.setDescription(config.getDescription());

        return configRepository.save(existing);
    }

    @Override
    @Transactional
    public TranslationServiceConfig enableConfig(Long id) {
        TranslationServiceConfig config = getConfigById(id);

        // 禁用所有其他配置
        List<TranslationServiceConfig> enabledConfigs = configRepository.findByIsEnabledTrue();
        for (TranslationServiceConfig enabledConfig : enabledConfigs) {
            if (!enabledConfig.getId().equals(id)) {
                enabledConfig.setIsEnabled(false);
                configRepository.save(enabledConfig);
            }
        }

        // 启用当前配置
        config.setIsEnabled(true);
        config.setStatus("ACTIVE");
        config.setErrorMessage(null);
        return configRepository.save(config);
    }

    @Override
    @Transactional
    public TranslationServiceConfig disableConfig(Long id) {
        TranslationServiceConfig config = getConfigById(id);
        config.setIsEnabled(false);
        return configRepository.save(config);
    }

    @Override
    public boolean testConfig(Long id) {
        TranslationServiceConfig config = getConfigById(id);
        
        // TODO: 实现测试逻辑，调用翻译服务的测试接口
        // 这里可以尝试翻译一个简单的测试文本，如果成功则返回true
        
        try {
            // 简单的配置验证
            if (config.getApiKey() == null || config.getApiKey().isEmpty()) {
                config.setStatus("ERROR");
                config.setErrorMessage("API密钥未配置");
                configRepository.save(config);
                return false;
            }

            // TODO: 实际调用翻译API进行测试
            // 如果测试成功，更新状态
            config.setStatus("ACTIVE");
            config.setErrorMessage(null);
            configRepository.save(config);
            return true;
        } catch (Exception e) {
            config.setStatus("ERROR");
            config.setErrorMessage("测试失败: " + e.getMessage());
            configRepository.save(config);
            return false;
        }
    }

    @Override
    @Transactional
    public void deleteConfig(Long id) {
        if (!configRepository.existsById(id)) {
            throw new RuntimeException("翻译服务配置不存在: " + id);
        }
        configRepository.deleteById(id);
    }

    @Override
    public TranslationServiceConfig getActiveConfig() {
        return configRepository.findFirstByIsEnabledTrueOrderByPriorityDesc()
                .orElse(null);
    }

    @Override
    @Transactional
    public void resetDailyUsage(Long id) {
        TranslationServiceConfig config = getConfigById(id);
        config.setCurrentDailyUsage(0L);
        config.setLastUsageResetTime(LocalDateTime.now());
        configRepository.save(config);
    }
}














