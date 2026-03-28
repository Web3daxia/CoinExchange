/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.util;

import java.util.Arrays;
import java.util.List;

/**
 * 语言工具类，定义系统支持的语言
 */
public class LanguageUtil {

    public static final String DEFAULT_LANGUAGE = "en-US"; // 默认语言（新闻原文通常是英文）

    // 系统支持的16种语言
    public static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
            "zh-CN", // 简体中文
            "en-US", // 英语 (美国)
            "ko-KR", // 韩语
            "ja-JP", // 日语
            "fr-FR", // 法语
            "de-DE", // 德语
            "es-ES", // 西班牙语
            "ru-RU", // 俄语
            "pt-PT", // 葡萄牙语
            "it-IT", // 意大利语
            "vi-VN", // 越南语
            "th-TH", // 泰语
            "ar-SA", // 阿拉伯语
            "tr-TR", // 土耳其语
            "id-ID", // 印尼语
            "hi-IN"  // 印地语
    );

    /**
     * 检查语言代码是否受支持
     * @param languageCode 语言代码
     * @return 如果支持则返回true，否则返回false
     */
    public static boolean isLanguageSupported(String languageCode) {
        return SUPPORTED_LANGUAGES.contains(languageCode);
    }

    /**
     * 获取默认语言
     * @return 默认语言代码
     */
    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }
}

