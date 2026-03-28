/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.EmailSmtpConfig;
import com.cryptotrade.systemmanagement.entity.EmailTemplate;
import java.util.List;

public interface EmailSmtpConfigService {
    EmailSmtpConfig createConfig(EmailSmtpConfig config);
    EmailSmtpConfig updateConfig(Long id, EmailSmtpConfig config);
    EmailSmtpConfig getActiveConfig();
    EmailTemplate createTemplate(EmailTemplate template);
    EmailTemplate updateTemplate(Long id, EmailTemplate template);
    void deleteTemplate(Long id);
    EmailTemplate getTemplate(String templateCode, String languageCode);
    List<EmailTemplate> getTemplatesByCode(String templateCode);
}














