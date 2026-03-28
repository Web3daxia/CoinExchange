/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.OssStorageConfig;
import java.util.List;

public interface OssStorageConfigService {
    OssStorageConfig createConfig(OssStorageConfig config);
    OssStorageConfig updateConfig(Long id, OssStorageConfig config);
    void deleteConfig(Long id);
    OssStorageConfig getConfigById(Long id);
    OssStorageConfig getDefaultConfig();
    List<OssStorageConfig> getAllConfigs();
    List<OssStorageConfig> getEnabledConfigs();
    void setDefault(Long id);
}














