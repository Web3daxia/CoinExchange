/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.VerificationLimitConfig;
import com.cryptotrade.systemmanagement.entity.VerificationLimitRecord;
import com.cryptotrade.systemmanagement.repository.VerificationLimitConfigRepository;
import com.cryptotrade.systemmanagement.repository.VerificationLimitRecordRepository;
import com.cryptotrade.systemmanagement.service.VerificationLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class VerificationLimitServiceImpl implements VerificationLimitService {
    
    @Autowired
    private VerificationLimitConfigRepository configRepository;
    
    @Autowired
    private VerificationLimitRecordRepository recordRepository;
    
    @Override
    @Transactional
    public VerificationLimitConfig createConfig(VerificationLimitConfig config) {
        return configRepository.save(config);
    }
    
    @Override
    @Transactional
    public VerificationLimitConfig updateConfig(Long id, VerificationLimitConfig config) {
        VerificationLimitConfig existing = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在"));
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        return configRepository.save(config);
    }
    
    @Override
    public List<VerificationLimitConfig> getAllConfigs() {
        return configRepository.findAll();
    }
    
    @Override
    @Transactional
    public boolean checkLimit(String limitKey, String limitType, String verificationType, String actionType) {
        VerificationLimitConfig config = configRepository
                .findByLimitTypeAndVerificationType(limitType, verificationType)
                .orElse(null);
        
        if (config == null || !config.getEnabled()) {
            return true; // 未配置或未启用，允许操作
        }
        
        // 计算时间周期
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime periodStart = calculatePeriodStart(now, config.getTimePeriod(), config.getTimeValue());
        LocalDateTime periodEnd = calculatePeriodEnd(periodStart, config.getTimePeriod(), config.getTimeValue());
        
        // 查询当前周期的记录数
        Integer currentCount = recordRepository.countByLimitKeyAndTypeAndTimeRange(
                limitKey, limitType, verificationType, actionType, now);
        
        // 检查是否超过限制
        if ("SEND_VERIFICATION".equals(actionType)) {
            return currentCount == null || currentCount < config.getMaxVerificationCount();
        } else if ("REGISTER".equals(actionType)) {
            Integer maxRegister = config.getMaxRegisterCount();
            if (maxRegister == null) {
                return true;
            }
            return currentCount == null || currentCount < maxRegister;
        }
        
        return true;
    }
    
    @Override
    @Transactional
    public void recordLimit(String limitKey, String limitType, String verificationType, String actionType) {
        VerificationLimitConfig config = configRepository
                .findByLimitTypeAndVerificationType(limitType, verificationType)
                .orElse(null);
        
        if (config == null || !config.getEnabled()) {
            return; // 未配置或未启用，不记录
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime periodStart = calculatePeriodStart(now, config.getTimePeriod(), config.getTimeValue());
        LocalDateTime periodEnd = calculatePeriodEnd(periodStart, config.getTimePeriod(), config.getTimeValue());
        
        VerificationLimitRecord record = new VerificationLimitRecord();
        record.setLimitKey(limitKey);
        record.setLimitType(limitType);
        record.setVerificationType(verificationType);
        record.setActionType(actionType);
        record.setCount(1);
        record.setPeriodStartTime(periodStart);
        record.setPeriodEndTime(periodEnd);
        
        recordRepository.save(record);
    }
    
    @Override
    @Transactional
    public void cleanExpiredRecords() {
        LocalDateTime expireTime = LocalDateTime.now().minusDays(7);
        recordRepository.deleteExpiredRecords(expireTime);
    }
    
    private LocalDateTime calculatePeriodStart(LocalDateTime now, String timePeriod, Integer timeValue) {
        switch (timePeriod) {
            case "MINUTE":
                return now.truncatedTo(ChronoUnit.MINUTES).minusMinutes(timeValue - 1);
            case "HOUR":
                return now.truncatedTo(ChronoUnit.HOURS).minusHours(timeValue - 1);
            case "DAY":
                return now.truncatedTo(ChronoUnit.DAYS).minusDays(timeValue - 1);
            default:
                return now;
        }
    }
    
    private LocalDateTime calculatePeriodEnd(LocalDateTime periodStart, String timePeriod, Integer timeValue) {
        switch (timePeriod) {
            case "MINUTE":
                return periodStart.plusMinutes(timeValue);
            case "HOUR":
                return periodStart.plusHours(timeValue);
            case "DAY":
                return periodStart.plusDays(timeValue);
            default:
                return periodStart;
        }
    }
}














