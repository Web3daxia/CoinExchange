/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.VerificationLimitConfig;
import com.cryptotrade.systemmanagement.entity.VerificationLimitRecord;
import java.util.List;

public interface VerificationLimitService {
    VerificationLimitConfig createConfig(VerificationLimitConfig config);
    VerificationLimitConfig updateConfig(Long id, VerificationLimitConfig config);
    List<VerificationLimitConfig> getAllConfigs();
    boolean checkLimit(String limitKey, String limitType, String verificationType, String actionType);
    void recordLimit(String limitKey, String limitType, String verificationType, String actionType);
    void cleanExpiredRecords();
}














