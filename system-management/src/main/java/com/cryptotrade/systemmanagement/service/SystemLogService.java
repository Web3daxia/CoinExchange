/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.SystemLogSearchRequest;
import com.cryptotrade.systemmanagement.entity.SystemLog;
import org.springframework.data.domain.Page;

/**
 * 系统日志Service接口
 */
public interface SystemLogService {
    
    /**
     * 保存系统日志
     */
    void saveLog(SystemLog log);
    
    /**
     * 搜索系统日志
     */
    Page<SystemLog> searchLogs(SystemLogSearchRequest request);
    
    /**
     * 根据ID获取日志
     */
    SystemLog getLogById(Long id);
    
    /**
     * 根据用户ID获取日志列表
     */
    Page<SystemLog> getLogsByUserId(Long userId, Integer page, Integer size);
    
    /**
     * 根据模块获取日志列表
     */
    Page<SystemLog> getLogsByModule(String module, Integer page, Integer size);
    
    /**
     * 删除旧日志（根据保留天数）
     */
    void deleteOldLogs(int daysToKeep);
}














