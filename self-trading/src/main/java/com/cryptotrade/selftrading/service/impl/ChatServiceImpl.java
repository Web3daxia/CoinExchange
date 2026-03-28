/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service.impl;

import com.cryptotrade.selftrading.entity.ChatMessage;
import com.cryptotrade.selftrading.repository.ChatMessageRepository;
import com.cryptotrade.selftrading.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 聊天服务实现类
 */
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ChatMessage sendMessage(Long orderId, Long senderId, Long receiverId,
                                  String messageType, String content, String fileUrl) {
        ChatMessage message = new ChatMessage();
        message.setOrderId(orderId);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setMessageType(messageType);
        message.setContent(content);
        message.setFileUrl(fileUrl);
        message.setIsRead(false);

        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessage> getOrderMessages(Long orderId) {
        return chatMessageRepository.findByOrderIdOrderByCreatedAtAsc(orderId);
    }

    @Override
    @Transactional
    public void markMessagesAsRead(Long orderId, Long userId) {
        List<ChatMessage> unreadMessages = chatMessageRepository.findByOrderIdAndIsReadFalse(orderId);
        for (ChatMessage message : unreadMessages) {
            if (message.getReceiverId().equals(userId)) {
                message.setIsRead(true);
                chatMessageRepository.save(message);
            }
        }
    }
}















