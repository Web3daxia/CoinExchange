/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.util;

import java.security.SecureRandom;

/**
 * 注册私钥生成工具类
 * 生成256位字母数字组合字符串
 */
public class RegistrationKeyGenerator {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * 生成256位字母数字组合注册私钥
     * @return 256位字母数字组合字符串
     */
    public static String generateRegistrationKey() {
        StringBuilder sb = new StringBuilder(KEY_LENGTH);
        for (int i = 0; i < KEY_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
    
    /**
     * 验证注册私钥格式（256位字母数字组合）
     * @param key 待验证的私钥
     * @return 是否为有效格式
     */
    public static boolean isValidFormat(String key) {
        if (key == null || key.length() != KEY_LENGTH) {
            return false;
        }
        return key.matches("^[A-Za-z0-9]{" + KEY_LENGTH + "}$");
    }
}














