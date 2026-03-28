/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

/**
 * 翻译服务配置加载器接口
 */
public interface TranslationServiceConfigLoader {
    /**
     * 获取当前启用的配置
     * @return 配置对象
     */
    Object getActiveConfig();

    /**
     * 根据配置获取翻译服务提供者
     * @param config 配置对象
     * @return 翻译服务提供者
     */
    TranslationProvider getProvider(Object config);
}












