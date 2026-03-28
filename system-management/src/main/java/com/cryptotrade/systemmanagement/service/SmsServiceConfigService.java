/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.SmsServiceConfig;
import com.cryptotrade.systemmanagement.entity.SmsTemplate;
import java.util.List;

public interface SmsServiceConfigService {
    SmsServiceConfig createConfig(SmsServiceConfig config);
    SmsServiceConfig updateConfig(Long id, SmsServiceConfig config);
    SmsServiceConfig getDefaultConfig();
    List<SmsServiceConfig> getAllConfigs();
    void setDefault(Long id);
    SmsTemplate createTemplate(SmsTemplate template);
    SmsTemplate updateTemplate(Long id, SmsTemplate template);
    void deleteTemplate(Long id);
    SmsTemplate getTemplate(String templateCode, String languageCode);
    List<SmsTemplate> getTemplatesByCode(String templateCode);
}














