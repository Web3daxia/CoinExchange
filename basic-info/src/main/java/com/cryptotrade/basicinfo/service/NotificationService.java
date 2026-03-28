/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service;

import com.cryptotrade.basicinfo.entity.Notification;

import java.util.List;

/**
 * 通知服务接口
 */
public interface NotificationService {
    /**
     * 创建通知
     * @param userId 用户ID
     * @param type 通知类型
     * @param title 标题
     * @param content 内容
     * @param linkUrl 跳转链接（可选）
     * @return 通知
     */
    Notification createNotification(Long userId, String type, String title, String content, String linkUrl);

    /**
     * 发送通知（创建并发送邮件/推送）
     * @param userId 用户ID
     * @param type 通知类型
     * @param title 标题
     * @param content 内容
     * @param linkUrl 跳转链接（可选）
     * @param sendEmail 是否发送邮件
     * @param sendPush 是否发送推送
     * @return 通知
     */
    Notification sendNotification(Long userId, String type, String title, String content,
                                 String linkUrl, Boolean sendEmail, Boolean sendPush);

    /**
     * 查询用户通知
     * @param userId 用户ID
     * @param type 通知类型（可选）
     * @return 通知列表
     */
    List<Notification> getUserNotifications(Long userId, String type);

    /**
     * 查询未读通知
     * @param userId 用户ID
     * @return 通知列表
     */
    List<Notification> getUnreadNotifications(Long userId);

    /**
     * 标记通知为已读
     * @param userId 用户ID
     * @param notificationId 通知ID
     */
    void markAsRead(Long userId, Long notificationId);

    /**
     * 标记所有通知为已读
     * @param userId 用户ID
     */
    void markAllAsRead(Long userId);
}















