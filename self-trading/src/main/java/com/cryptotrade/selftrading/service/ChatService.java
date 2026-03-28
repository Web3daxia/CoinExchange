/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service;

import com.cryptotrade.selftrading.entity.ChatMessage;

import java.util.List;

/**
 * 聊天服务接口
 */
public interface ChatService {
    /**
     * 发送消息
     * @param orderId 订单ID
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param messageType 消息类型
     * @param content 消息内容
     * @param fileUrl 文件URL（可选）
     * @return 消息
     */
    ChatMessage sendMessage(Long orderId, Long senderId, Long receiverId,
                           String messageType, String content, String fileUrl);

    /**
     * 查询订单聊天记录
     * @param orderId 订单ID
     * @return 消息列表
     */
    List<ChatMessage> getOrderMessages(Long orderId);

    /**
     * 标记消息为已读
     * @param orderId 订单ID
     * @param userId 用户ID
     */
    void markMessagesAsRead(Long orderId, Long userId);
}















