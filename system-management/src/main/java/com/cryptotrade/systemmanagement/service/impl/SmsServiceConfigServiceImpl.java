/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.SmsServiceConfig;
import com.cryptotrade.systemmanagement.entity.SmsTemplate;
import com.cryptotrade.systemmanagement.repository.SmsServiceConfigRepository;
import com.cryptotrade.systemmanagement.repository.SmsTemplateRepository;
import com.cryptotrade.systemmanagement.service.SmsServiceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SmsServiceConfigServiceImpl implements SmsServiceConfigService {
    
    @Autowired
    private SmsServiceConfigRepository configRepository;
    
    @Autowired
    private SmsTemplateRepository templateRepository;
    
    @Override
    @Transactional
    public SmsServiceConfig createConfig(SmsServiceConfig config) {
        if (configRepository.existsByServiceProvider(config.getServiceProvider())) {
            throw new RuntimeException("该短信服务提供商配置已存在");
        }
        return configRepository.save(config);
    }
    
    @Override
    @Transactional
    public SmsServiceConfig updateConfig(Long id, SmsServiceConfig config) {
        SmsServiceConfig existing = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在"));
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        return configRepository.save(config);
    }
    
    @Override
    public SmsServiceConfig getDefaultConfig() {
        return configRepository.findByEnabledTrueAndIsDefaultTrue()
                .orElseThrow(() -> new RuntimeException("未找到默认短信服务配置"));
    }
    
    @Override
    public List<SmsServiceConfig> getAllConfigs() {
        return configRepository.findAll();
    }
    
    @Override
    @Transactional
    public void setDefault(Long id) {
        configRepository.findAll().forEach(config -> {
            config.setIsDefault(false);
            configRepository.save(config);
        });
        SmsServiceConfig config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在"));
        config.setIsDefault(true);
        config.setEnabled(true);
        configRepository.save(config);
    }
    
    @Override
    @Transactional
    public SmsTemplate createTemplate(SmsTemplate template) {
        return templateRepository.save(template);
    }
    
    @Override
    @Transactional
    public SmsTemplate updateTemplate(Long id, SmsTemplate template) {
        SmsTemplate existing = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("模板不存在"));
        template.setId(id);
        template.setCreatedAt(existing.getCreatedAt());
        return templateRepository.save(template);
    }
    
    @Override
    @Transactional
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }
    
    @Override
    public SmsTemplate getTemplate(String templateCode, String languageCode) {
        return templateRepository.findByTemplateCodeAndLanguageCode(templateCode, languageCode)
                .orElse(null);
    }
    
    @Override
    public List<SmsTemplate> getTemplatesByCode(String templateCode) {
        return templateRepository.findByTemplateCode(templateCode);
    }
}














