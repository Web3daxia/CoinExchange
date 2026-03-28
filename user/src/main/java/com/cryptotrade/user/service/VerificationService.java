/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service;

import java.util.Random;

public interface VerificationService {
    /**
     * 发送验证码
     */
    void sendVerificationCode(String type, String contact, String countryCode);

    /**
     * 验证验证码
     */
    boolean verifyCode(String contact, String code);

    /**
     * 生成6位数字验证码
     */
    default String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}















