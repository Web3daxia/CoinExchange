/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service.impl;

import com.cryptotrade.user.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationServiceImpl implements VerificationService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    private static final String CODE_PREFIX = "verification:code:";
    private static final int CODE_EXPIRE_MINUTES = 5;

    @Override
    public void sendVerificationCode(String type, String contact, String countryCode) {
        String code = generateCode();
        String key = CODE_PREFIX + contact;

        // 存储验证码到Redis，5分钟过期
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        if ("EMAIL".equals(type)) {
            sendEmailCode(contact, code);
        } else if ("PHONE".equals(type)) {
            sendSmsCode(contact, countryCode, code);
        }
    }

    @Override
    public boolean verifyCode(String contact, String code) {
        String key = CODE_PREFIX + contact;
        String storedCode = redisTemplate.opsForValue().get(key);
        
        if (storedCode != null && storedCode.equals(code)) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    private void sendEmailCode(String email, String code) {
        if (mailSender != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("验证码");
            message.setText("您的验证码是: " + code + "，有效期5分钟。");
            mailSender.send(message);
        } else {
            // 开发环境，直接打印到日志
            System.out.println("Email verification code for " + email + ": " + code);
        }
    }

    private void sendSmsCode(String phone, String countryCode, String code) {
        // TODO: 集成第三方短信服务（如Twilio）
        // 开发环境，直接打印到日志
        System.out.println("SMS verification code for " + countryCode + phone + ": " + code);
    }
}















