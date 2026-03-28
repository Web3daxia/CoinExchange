/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service;

/**
 * 邮件服务接口
 */
public interface EmailService {
    /**
     * 发送欢迎邮件（注册成功后）
     * @param toEmail 收件人邮箱
     * @param userName 用户名
     * @param languageCode 语言代码
     */
    void sendWelcomeEmail(String toEmail, String userName, String languageCode);

    /**
     * 发送验证码邮件
     * @param toEmail 收件人邮箱
     * @param code 验证码
     * @param languageCode 语言代码
     */
    void sendVerificationCodeEmail(String toEmail, String code, String languageCode);

    /**
     * 发送交易提醒邮件
     * @param toEmail 收件人邮箱
     * @param title 标题
     * @param content 内容
     * @param languageCode 语言代码
     */
    void sendTradeAlertEmail(String toEmail, String title, String content, String languageCode);

    /**
     * 发送通用邮件
     * @param toEmail 收件人邮箱
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否HTML格式
     */
    void sendEmail(String toEmail, String subject, String content, boolean isHtml);
}















