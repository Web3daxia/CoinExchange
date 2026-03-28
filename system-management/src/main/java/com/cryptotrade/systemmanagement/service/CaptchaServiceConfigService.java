/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.CaptchaServiceConfig;
import java.util.List;

public interface CaptchaServiceConfigService {
    CaptchaServiceConfig createConfig(CaptchaServiceConfig config);
    CaptchaServiceConfig updateConfig(Long id, CaptchaServiceConfig config);
    void deleteConfig(Long id);
    CaptchaServiceConfig getConfigById(Long id);
    CaptchaServiceConfig getDefaultConfig();
    List<CaptchaServiceConfig> getAllConfigs();
    List<CaptchaServiceConfig> getEnabledConfigs();
    void setDefault(Long id);
}














