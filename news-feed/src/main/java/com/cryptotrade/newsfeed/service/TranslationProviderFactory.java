/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import com.cryptotrade.newsfeed.service.impl.NewsTranslationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 翻译服务提供者工厂
 * 根据配置动态选择翻译服务
 * 注意：实际的提供者实现由system-management模块的TranslationProviderServiceImpl提供
 */
@Component
public class TranslationProviderFactory {

    @Autowired(required = false)
    private TranslationServiceConfigLoader configLoader;

    /**
     * 获取当前启用的翻译服务提供者
     */
    public NewsTranslationServiceImpl.TranslationProvider getActiveProvider() {
        if (configLoader == null) {
            return null; // 配置加载器未注入（system-management模块可能未加载）
        }

        Object config = configLoader.getActiveConfig();
        if (config == null) {
            return null; // 没有启用的翻译服务
        }

        return configLoader.getProvider(config);
    }
}
