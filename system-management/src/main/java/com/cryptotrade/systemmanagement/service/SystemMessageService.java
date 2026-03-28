/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.SystemMessage;
import java.util.List;
import java.util.Map;

public interface SystemMessageService {
    SystemMessage createMessage(String messageKey, String languageCode, String messageValue, String module);
    
    SystemMessage updateMessage(Long messageId, String messageValue);
    
    void deleteMessage(Long messageId);
    
    SystemMessage getMessage(String messageKey, String languageCode);
    
    String getMessageValue(String messageKey, String languageCode);
    
    Map<String, String> getAllMessagesByLanguage(String languageCode);
    
    List<SystemMessage> getMessagesByModule(String module);
    
    void batchCreateMessages(String messageKey, Map<String, String> languageMessages, String module);
}














