/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 质押借币统计Service接口
 */
public interface PledgeLoanStatisticsService {
    
    /**
     * 获取用户质押统计
     */
    Map<String, Object> getUserPledgeStatistics(Long userId);
    
    /**
     * 获取平台的质押统计
     */
    Map<String, Object> getPlatformPledgeStatistics();
    
    /**
     * 获取风险统计
     */
    Map<String, Object> getRiskStatistics();
    
    /**
     * 获取利息收入统计
     */
    Map<String, Object> getInterestIncomeStatistics(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取平仓统计
     */
    Map<String, Object> getLiquidationStatistics(LocalDateTime startTime, LocalDateTime endTime);
}














