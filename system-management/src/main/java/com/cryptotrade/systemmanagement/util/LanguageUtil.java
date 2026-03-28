/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.util;

import java.util.Arrays;
import java.util.List;

/**
 * 系统支持的语言工具类
 * 系统默认支持16种语言（中文为默认语言）
 */
public class LanguageUtil {
    
    /**
     * 系统支持的所有语言代码列表
     */
    public static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
        "zh-CN",  // 中文（默认）
        "en-US",  // 英语
        "ja-JP",  // 日语
        "ko-KR",  // 韩语
        "ru-RU",  // 俄语
        "es-ES",  // 西班牙语
        "fr-FR",  // 法语
        "de-DE",  // 德语
        "it-IT",  // 意大利语
        "pt-PT",  // 葡萄牙语
        "nl-NL",  // 荷兰语
        "pl-PL",  // 波兰语
        "tr-TR",  // 土耳其语
        "vi-VN",  // 越南语
        "th-TH",  // 泰语
        "ar-SA"   // 阿拉伯语
    );
    
    /**
     * 默认语言代码（中文）
     */
    public static final String DEFAULT_LANGUAGE = "zh-CN";
    
    /**
     * 检查语言代码是否支持
     */
    public static boolean isSupported(String languageCode) {
        return SUPPORTED_LANGUAGES.contains(languageCode);
    }
    
    /**
     * 获取默认语言代码
     */
    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }
}














