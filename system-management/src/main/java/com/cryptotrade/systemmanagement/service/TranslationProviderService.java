/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.TranslationServiceConfig;

/**
 * 翻译服务提供者Service
 * 提供翻译服务提供者的实现
 */
public interface TranslationProviderService {
    
    /**
     * 根据配置获取翻译服务提供者
     */
    TranslationProvider getProvider(TranslationServiceConfig config);
    
    /**
     * 获取当前启用的翻译服务提供者
     */
    TranslationProvider getActiveProvider();
}



