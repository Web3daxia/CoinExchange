/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import com.cryptotrade.pledgeloan.entity.PledgeLoanRiskRecord;

import java.util.List;

/**
 * 质押借币风险监控Service接口
 */
public interface PledgeLoanRiskService {
    
    /**
     * 创建风险记录
     */
    PledgeLoanRiskRecord createRiskRecord(Long orderId, String riskLevel, String riskMessage);
    
    /**
     * 获取订单的风险记录
     */
    List<PledgeLoanRiskRecord> getOrderRiskRecords(Long orderId);
    
    /**
     * 获取用户的风险记录
     */
    List<PledgeLoanRiskRecord> getUserRiskRecords(Long userId);
    
    /**
     * 获取未处理的风险记录
     */
    List<PledgeLoanRiskRecord> getUnprocessedRiskRecords();
    
    /**
     * 标记风险记录为已处理
     */
    void markAsProcessed(Long riskRecordId, Long processorId);
    
    /**
     * 通知用户风险
     */
    void notifyUserRisk(Long riskRecordId);
}














