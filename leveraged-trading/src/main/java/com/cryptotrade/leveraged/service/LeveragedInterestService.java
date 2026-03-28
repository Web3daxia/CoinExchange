/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service;

import com.cryptotrade.leveraged.entity.LeveragedInterestRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 杠杆利息服务接口
 */
public interface LeveragedInterestService {
    /**
     * 查询用户利息记录
     */
    List<LeveragedInterestRecord> getInterestRecords(Long userId, String pairName);

    /**
     * 查询实时利息预览
     */
    BigDecimal previewInterest(Long userId, String pairName, BigDecimal amount, Integer leverage);

    /**
     * 计算利息
     */
    BigDecimal calculateInterest(BigDecimal borrowedAmount, BigDecimal interestRate, Integer hours);

    /**
     * 结算利息
     */
    void settleInterest(Long accountId, LocalDateTime settlementTime);

    /**
     * 根据结算周期查询利息记录
     */
    List<LeveragedInterestRecord> getInterestRecordsBySettlementCycle(String settlementCycle);

    /**
     * 根据状态查询利息记录
     */
    List<LeveragedInterestRecord> getInterestRecordsByStatus(String status);
}













