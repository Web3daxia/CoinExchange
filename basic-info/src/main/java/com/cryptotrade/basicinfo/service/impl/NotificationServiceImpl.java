/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.basicinfo.service.impl;

import com.cryptotrade.basicinfo.entity.Notification;
import com.cryptotrade.basicinfo.entity.UserPreference;
import com.cryptotrade.basicinfo.repository.NotificationRepository;
import com.cryptotrade.basicinfo.repository.UserPreferenceRepository;
import com.cryptotrade.basicinfo.service.EmailService;
import com.cryptotrade.basicinfo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 通知服务实现类
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private EmailService emailService;

    // TODO: 注入推送服务
    // @Autowired
    // private PushService pushService;

    @Override
    @Transactional
    public Notification createNotification(Long userId, String type, String title, String content, String linkUrl) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setLinkUrl(linkUrl);
        notification.setIsRead(false);
        notification.setIsEmailSent(false);
        notification.setIsPushSent(false);

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public Notification sendNotification(Long userId, String type, String title, String content,
                                        String linkUrl, Boolean sendEmail, Boolean sendPush) {
        Notification notification = createNotification(userId, type, title, content, linkUrl);

        // 发送邮件
        if (Boolean.TRUE.equals(sendEmail)) {
            try {
                // TODO: 获取用户邮箱
                String userEmail = getUserEmail(userId);
                if (userEmail != null) {
                    UserPreference preference = getUserPreference(userId);
                    emailService.sendTradeAlertEmail(userEmail, title, content, preference.getLanguageCode());
                    notification.setIsEmailSent(true);
                }
            } catch (Exception e) {
                // 记录错误但不影响通知创建
                System.err.println("发送邮件失败: " + e.getMessage());
            }
        }

        // 发送推送
        if (Boolean.TRUE.equals(sendPush)) {
            try {
                // TODO: 实现推送服务
                // pushService.sendPush(userId, title, content);
                notification.setIsPushSent(true);
            } catch (Exception e) {
                System.err.println("发送推送失败: " + e.getMessage());
            }
        }

        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId, String type) {
        if (type != null && !type.isEmpty()) {
            return notificationRepository.findByUserIdAndTypeOrderByCreatedAtDesc(userId, type);
        }
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("通知不存在"));

        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此通知");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }

    /**
     * 获取用户邮箱（需要从用户模块获取）
     */
    private String getUserEmail(Long userId) {
        // TODO: 从用户模块获取用户邮箱
        return null;
    }

    /**
     * 获取用户偏好
     */
    private UserPreference getUserPreference(Long userId) {
        Optional<UserPreference> preference = userPreferenceRepository.findByUserId(userId);
        if (preference.isPresent()) {
            return preference.get();
        }
        UserPreference defaultPref = new UserPreference();
        defaultPref.setUserId(userId);
        defaultPref.setLanguageCode("en");
        defaultPref.setCurrency("USD");
        return defaultPref;
    }
}















