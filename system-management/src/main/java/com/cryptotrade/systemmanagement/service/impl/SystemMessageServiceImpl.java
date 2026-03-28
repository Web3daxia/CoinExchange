/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.SystemMessage;
import com.cryptotrade.systemmanagement.repository.SystemMessageRepository;
import com.cryptotrade.systemmanagement.service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SystemMessageServiceImpl implements SystemMessageService {
    
    @Autowired
    private SystemMessageRepository systemMessageRepository;
    
    @Override
    @Transactional
    public SystemMessage createMessage(String messageKey, String languageCode, String messageValue, String module) {
        // 检查是否已存在
        systemMessageRepository.findByMessageKeyAndLanguageCode(messageKey, languageCode)
                .ifPresent(m -> {
                    throw new RuntimeException("消息已存在: " + messageKey + " - " + languageCode);
                });
        
        SystemMessage message = new SystemMessage();
        message.setMessageKey(messageKey);
        message.setLanguageCode(languageCode);
        message.setMessageValue(messageValue);
        message.setModule(module);
        
        return systemMessageRepository.save(message);
    }
    
    @Override
    @Transactional
    public SystemMessage updateMessage(Long messageId, String messageValue) {
        SystemMessage message = systemMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("消息不存在"));
        
        message.setMessageValue(messageValue);
        
        return systemMessageRepository.save(message);
    }
    
    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        systemMessageRepository.deleteById(messageId);
    }
    
    @Override
    public SystemMessage getMessage(String messageKey, String languageCode) {
        return systemMessageRepository.findByMessageKeyAndLanguageCode(messageKey, languageCode)
                .orElse(null);
    }
    
    @Override
    public String getMessageValue(String messageKey, String languageCode) {
        SystemMessage message = getMessage(messageKey, languageCode);
        if (message != null) {
            return message.getMessageValue();
        }
        
        // 如果找不到，尝试返回中文或英文
        SystemMessage defaultMessage = systemMessageRepository.findByMessageKeyAndLanguageCode(messageKey, "zh-CN")
                .orElse(null);
        if (defaultMessage != null) {
            return defaultMessage.getMessageValue();
        }
        
        defaultMessage = systemMessageRepository.findByMessageKeyAndLanguageCode(messageKey, "en-US")
                .orElse(null);
        if (defaultMessage != null) {
            return defaultMessage.getMessageValue();
        }
        
        return messageKey; // 如果都找不到，返回key
    }
    
    @Override
    public Map<String, String> getAllMessagesByLanguage(String languageCode) {
        List<SystemMessage> messages = systemMessageRepository.findByLanguageCode(languageCode);
        return messages.stream()
                .collect(Collectors.toMap(
                        SystemMessage::getMessageKey,
                        SystemMessage::getMessageValue,
                        (v1, v2) -> v1 // 如果有重复key，保留第一个
                ));
    }
    
    @Override
    public List<SystemMessage> getMessagesByModule(String module) {
        return systemMessageRepository.findByModule(module);
    }
    
    @Override
    @Transactional
    public void batchCreateMessages(String messageKey, Map<String, String> languageMessages, String module) {
        for (Map.Entry<String, String> entry : languageMessages.entrySet()) {
            String languageCode = entry.getKey();
            String messageValue = entry.getValue();
            
            // 如果已存在则更新，否则创建
            Optional<SystemMessage> existing = systemMessageRepository.findByMessageKeyAndLanguageCode(messageKey, languageCode);
            if (existing.isPresent()) {
                SystemMessage message = existing.get();
                message.setMessageValue(messageValue);
                systemMessageRepository.save(message);
            } else {
                createMessage(messageKey, languageCode, messageValue, module);
            }
        }
    }
}



