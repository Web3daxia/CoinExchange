/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交割合约账户服务接口
 */
public interface DeliveryAccountService {
    /**
     * 查询合约账户余额
     */
    Map<String, Object> getAccountBalance(Long userId, String currency);

    /**
     * 查询账户保证金信息
     */
    Map<String, Object> getMarginInfo(Long userId, Long contractId);

    /**
     * 查询账户盈亏统计
     */
    Map<String, Object> getPnlStatistics(Long userId, Long contractId, String startTime, 
                                         String endTime, String period);

    /**
     * 转账到合约账户
     */
    Map<String, Object> transferIn(Long userId, String currency, BigDecimal amount);

    /**
     * 从合约账户转出
     */
    Map<String, Object> transferOut(Long userId, String currency, BigDecimal amount);

    /**
     * 查询转账记录
     */
    List<Map<String, Object>> getTransferHistory(Long userId, String currency, String direction,
                                                  String startTime, String endTime);
}













