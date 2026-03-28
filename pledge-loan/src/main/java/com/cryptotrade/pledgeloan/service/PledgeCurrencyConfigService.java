/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import com.cryptotrade.pledgeloan.entity.PledgeCurrencyConfig;

import java.util.List;

/**
 * 质押币种配置Service接口
 */
public interface PledgeCurrencyConfigService {
    
    /**
     * 获取所有配置
     */
    List<PledgeCurrencyConfig> getAllConfigs();
    
    /**
     * 根据ID获取配置
     */
    PledgeCurrencyConfig getConfigById(Long id);
    
    /**
     * 根据币种代码获取配置
     */
    PledgeCurrencyConfig getConfigByCurrencyCode(String currencyCode);
    
    /**
     * 创建配置
     */
    PledgeCurrencyConfig createConfig(PledgeCurrencyConfig config);
    
    /**
     * 更新配置
     */
    PledgeCurrencyConfig updateConfig(Long id, PledgeCurrencyConfig config);
    
    /**
     * 删除配置
     */
    void deleteConfig(Long id);
    
    /**
     * 启用/停用配置
     */
    PledgeCurrencyConfig updateStatus(Long id, String status);
}














