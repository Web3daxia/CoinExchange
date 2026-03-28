/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.TranslationServiceConfig;

import java.util.List;

/**
 * 翻译服务配置Service接口
 */
public interface TranslationServiceConfigService {
    
    /**
     * 获取所有翻译服务配置
     */
    List<TranslationServiceConfig> getAllConfigs();
    
    /**
     * 根据ID获取配置
     */
    TranslationServiceConfig getConfigById(Long id);
    
    /**
     * 根据服务代码获取配置
     */
    TranslationServiceConfig getConfigByServiceCode(String serviceCode);
    
    /**
     * 创建翻译服务配置
     */
    TranslationServiceConfig createConfig(TranslationServiceConfig config);
    
    /**
     * 更新翻译服务配置
     */
    TranslationServiceConfig updateConfig(Long id, TranslationServiceConfig config);
    
    /**
     * 启用翻译服务（同时禁用其他服务）
     */
    TranslationServiceConfig enableConfig(Long id);
    
    /**
     * 禁用翻译服务
     */
    TranslationServiceConfig disableConfig(Long id);
    
    /**
     * 测试翻译服务配置
     */
    boolean testConfig(Long id);
    
    /**
     * 删除配置
     */
    void deleteConfig(Long id);
    
    /**
     * 获取当前启用的配置
     */
    TranslationServiceConfig getActiveConfig();
    
    /**
     * 重置每日使用量
     */
    void resetDailyUsage(Long id);
}














