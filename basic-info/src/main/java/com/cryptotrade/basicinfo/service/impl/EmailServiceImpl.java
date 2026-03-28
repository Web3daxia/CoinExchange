/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service.impl;

import com.cryptotrade.basicinfo.service.EmailService;
import com.cryptotrade.basicinfo.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Objects;

/**
 * 邮件服务实现类
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private LanguageService languageService;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:Crypto Exchange}")
    private String appName;

    @Override
    public void sendWelcomeEmail(String toEmail, String userName, String languageCode) {
        Map<String, String> translations = languageService.getTranslationData(languageCode);
        
        String subject = translations.getOrDefault("email.welcome.subject", "Welcome to " + appName);
        String content = buildWelcomeEmailContent(userName, translations);

        sendEmail(toEmail, subject, content, true);
    }

    @Override
    public void sendVerificationCodeEmail(String toEmail, String code, String languageCode) {
        Map<String, String> translations = languageService.getTranslationData(languageCode);
        
        String subject = translations.getOrDefault("email.verification.subject", "Verification Code");
        String content = buildVerificationEmailContent(code, translations);

        sendEmail(toEmail, subject, content, true);
    }

    @Override
    public void sendTradeAlertEmail(String toEmail, String title, String content, String languageCode) {
        Map<String, String> translations = languageService.getTranslationData(languageCode);
        String subject = translations.getOrDefault("email.trade.alert.subject", "Trade Alert") + ": " + title;

        sendEmail(toEmail, subject, content, true);
    }

    @Override
    public void sendEmail(String toEmail, String subject, String content, boolean isHtml) {
        // 参数验证，使用 Objects.requireNonNull 确保非 null
        String validToEmail = Objects.requireNonNull(toEmail, "收件人邮箱不能为空");
        String validSubject = Objects.requireNonNull(subject, "邮件主题不能为空");
        String validContent = Objects.requireNonNull(content, "邮件内容不能为空");
        String validFromEmail = Objects.requireNonNull(fromEmail, "发件人邮箱未配置");
        
        if (validToEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("收件人邮箱不能为空");
        }
        if (validFromEmail.trim().isEmpty()) {
            throw new IllegalStateException("发件人邮箱未配置");
        }
        
        try {
            if (isHtml) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(validFromEmail);
                helper.setTo(validToEmail);
                helper.setSubject(validSubject);
                helper.setText(validContent, true);
                mailSender.send(message);
            } else {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(validFromEmail);
                message.setTo(validToEmail);
                message.setSubject(validSubject);
                message.setText(validContent);
                mailSender.send(message);
            }
        } catch (MessagingException e) {
            throw new RuntimeException("发送邮件失败: " + e.getMessage());
        }
    }

    /**
     * 构建欢迎邮件内容
     */
    private String buildWelcomeEmailContent(String userName, Map<String, String> translations) {
        String welcomeText = translations.getOrDefault("email.welcome.content", 
                "Welcome to " + appName + "! Thank you for joining us.");
        
        return "<html><body>" +
                "<h2>" + welcomeText + "</h2>" +
                "<p>" + translations.getOrDefault("email.welcome.greeting", "Hello") + " " + userName + ",</p>" +
                "<p>" + translations.getOrDefault("email.welcome.message", 
                        "We are excited to have you on board. Start trading now!") + "</p>" +
                "</body></html>";
    }

    /**
     * 构建验证码邮件内容
     */
    private String buildVerificationEmailContent(String code, Map<String, String> translations) {
        String verificationText = translations.getOrDefault("email.verification.content", 
                "Your verification code is:");
        
        return "<html><body>" +
                "<h2>" + translations.getOrDefault("email.verification.title", "Verification Code") + "</h2>" +
                "<p>" + verificationText + "</p>" +
                "<h3 style='color: #007bff;'>" + code + "</h3>" +
                "<p>" + translations.getOrDefault("email.verification.warning", 
                        "This code will expire in 10 minutes. Please do not share it with anyone.") + "</p>" +
                "</body></html>";
    }
}














