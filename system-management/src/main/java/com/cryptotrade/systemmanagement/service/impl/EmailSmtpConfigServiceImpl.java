/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.EmailSmtpConfig;
import com.cryptotrade.systemmanagement.entity.EmailTemplate;
import com.cryptotrade.systemmanagement.repository.EmailSmtpConfigRepository;
import com.cryptotrade.systemmanagement.repository.EmailTemplateRepository;
import com.cryptotrade.systemmanagement.service.EmailSmtpConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmailSmtpConfigServiceImpl implements EmailSmtpConfigService {
    
    @Autowired
    private EmailSmtpConfigRepository smtpConfigRepository;
    
    @Autowired
    private EmailTemplateRepository templateRepository;
    
    @Override
    @Transactional
    public EmailSmtpConfig createConfig(EmailSmtpConfig config) {
        return smtpConfigRepository.save(config);
    }
    
    @Override
    @Transactional
    public EmailSmtpConfig updateConfig(Long id, EmailSmtpConfig config) {
        EmailSmtpConfig existing = smtpConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在"));
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        return smtpConfigRepository.save(config);
    }
    
    @Override
    public EmailSmtpConfig getActiveConfig() {
        return smtpConfigRepository.findByEnabledTrue()
                .orElseThrow(() -> new RuntimeException("未找到启用的SMTP配置"));
    }
    
    @Override
    @Transactional
    public EmailTemplate createTemplate(EmailTemplate template) {
        return templateRepository.save(template);
    }
    
    @Override
    @Transactional
    public EmailTemplate updateTemplate(Long id, EmailTemplate template) {
        EmailTemplate existing = templateRepository.findById(id)
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
    public EmailTemplate getTemplate(String templateCode, String languageCode) {
        return templateRepository.findByTemplateCodeAndLanguageCode(templateCode, languageCode)
                .orElse(null);
    }
    
    @Override
    public List<EmailTemplate> getTemplatesByCode(String templateCode) {
        return templateRepository.findByTemplateCode(templateCode);
    }
}














