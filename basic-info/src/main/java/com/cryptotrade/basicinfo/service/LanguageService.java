/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service;

import com.cryptotrade.basicinfo.entity.LanguagePack;
import com.cryptotrade.basicinfo.entity.UserPreference;

import java.util.List;
import java.util.Map;

/**
 * 语言服务接口
 */
public interface LanguageService {
    /**
     * 获取所有支持的语言
     * @return 语言列表
     */
    List<LanguagePack> getSupportedLanguages();

    /**
     * 根据IP地址自动识别语言
     * @param ipAddress IP地址
     * @return 语言代码
     */
    String detectLanguageByIp(String ipAddress);

    /**
     * 获取语言包翻译数据
     * @param languageCode 语言代码
     * @return 翻译数据
     */
    Map<String, String> getTranslationData(String languageCode);

    /**
     * 设置用户语言偏好
     * @param userId 用户ID
     * @param languageCode 语言代码
     * @return 用户偏好
     */
    UserPreference setUserLanguage(Long userId, String languageCode);

    /**
     * 获取用户语言偏好
     * @param userId 用户ID
     * @return 用户偏好
     */
    UserPreference getUserPreference(Long userId);
}















