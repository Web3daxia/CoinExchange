/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service;

import com.cryptotrade.leveraged.entity.LeveragedFundTransferRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 杠杆资金转账服务接口
 */
public interface LeveragedFundTransferService {
    /**
     * 转入资金到杠杆账户
     */
    LeveragedFundTransferRecord transferIn(Long userId, String pairName, String currency, BigDecimal amount);

    /**
     * 从杠杆账户转出资金
     */
    LeveragedFundTransferRecord transferOut(Long userId, String pairName, String currency, BigDecimal amount);

    /**
     * 查询转账记录
     */
    List<LeveragedFundTransferRecord> getTransferHistory(Long userId, String pairName, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据转账ID查询转账记录
     */
    LeveragedFundTransferRecord getTransferRecord(String transferId);

    /**
     * 根据转账类型查询转账记录
     */
    List<LeveragedFundTransferRecord> getTransferRecordsByType(String transferType);

    /**
     * 根据状态查询转账记录
     */
    List<LeveragedFundTransferRecord> getTransferRecordsByStatus(String status);
}













