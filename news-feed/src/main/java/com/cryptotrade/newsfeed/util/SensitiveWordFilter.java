/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * 敏感词过滤工具类
 */
public class SensitiveWordFilter {
    
    /**
     * 检测文本中的敏感词
     * @param text 待检测文本
     * @param sensitiveWords 敏感词列表
     * @return 检测到的敏感词列表
     */
    public static List<String> detectSensitiveWords(String text, List<String> sensitiveWords) {
        if (text == null || text.isEmpty() || sensitiveWords == null || sensitiveWords.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> detected = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (String word : sensitiveWords) {
            if (lowerText.contains(word.toLowerCase())) {
                detected.add(word);
            }
        }

        return detected;
    }

    /**
     * 过滤敏感词（替换为*）
     * @param text 待过滤文本
     * @param sensitiveWords 敏感词列表
     * @return 过滤后的文本
     */
    public static String filterSensitiveWords(String text, List<String> sensitiveWords) {
        if (text == null || text.isEmpty() || sensitiveWords == null || sensitiveWords.isEmpty()) {
            return text;
        }

        String result = text;
        for (String word : sensitiveWords) {
            result = result.replaceAll("(?i)" + word, "*".repeat(word.length()));
        }

        return result;
    }

    /**
     * 检查是否包含敏感词
     * @param text 待检查文本
     * @param sensitiveWords 敏感词列表
     * @return 如果包含敏感词返回true
     */
    public static boolean containsSensitiveWords(String text, List<String> sensitiveWords) {
        List<String> detected = detectSensitiveWords(text, sensitiveWords);
        return !detected.isEmpty();
    }
}














