/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.service;

import java.util.function.Supplier;

/**
 * 翻译服务配置加载器接口
 * 用于从system-management模块加载配置，避免循环依赖
 */
public interface TranslationServiceConfigLoader {
    /**
     * 获取当前启用的翻译服务配置
     */
    Object getActiveConfig();

    /**
     * 获取翻译服务提供者
     */
    NewsTranslationServiceImpl.TranslationProvider getProvider(Object config);
}














