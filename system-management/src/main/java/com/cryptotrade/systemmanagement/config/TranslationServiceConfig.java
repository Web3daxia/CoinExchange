/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.config;

import com.cryptotrade.systemmanagement.service.TranslationServiceConfigLoader;
import com.cryptotrade.systemmanagement.service.impl.TranslationProviderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 翻译服务配置类
 * 将TranslationProviderServiceImpl注册为TranslationServiceConfigLoader的Bean
 */
@Configuration
public class TranslationServiceConfig {

    /**
     * 将TranslationProviderServiceImpl注册为TranslationServiceConfigLoader
     * 这样news-feed模块就可以通过接口注入，避免循环依赖
     */
    @Bean
    public TranslationServiceConfigLoader translationServiceConfigLoader(TranslationProviderServiceImpl service) {
        return service;
    }
}



