/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import com.cryptotrade.pledgeloan.entity.LoanCurrencyConfig;

import java.util.List;

/**
 * 借款币种配置Service接口
 */
public interface LoanCurrencyConfigService {
    
    /**
     * 获取所有配置
     */
    List<LoanCurrencyConfig> getAllConfigs();
    
    /**
     * 根据ID获取配置
     */
    LoanCurrencyConfig getConfigById(Long id);
    
    /**
     * 根据币种代码获取配置
     */
    LoanCurrencyConfig getConfigByCurrencyCode(String currencyCode);
    
    /**
     * 创建配置
     */
    LoanCurrencyConfig createConfig(LoanCurrencyConfig config);
    
    /**
     * 更新配置
     */
    LoanCurrencyConfig updateConfig(Long id, LoanCurrencyConfig config);
    
    /**
     * 删除配置
     */
    void deleteConfig(Long id);
    
    /**
     * 启用/停用配置
     */
    LoanCurrencyConfig updateStatus(Long id, String status);
}














