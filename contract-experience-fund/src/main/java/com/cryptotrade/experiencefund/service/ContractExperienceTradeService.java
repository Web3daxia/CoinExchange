/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.service;

import com.cryptotrade.experiencefund.entity.ContractExperienceTrade;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * 合约体验金交易Service接口
 */
public interface ContractExperienceTradeService {
    
    /**
     * 开仓
     */
    ContractExperienceTrade openPosition(Long userId, Long accountId, String contractType, String pairName,
                                         String tradeType, String orderType, Integer leverage,
                                         BigDecimal positionSize, BigDecimal entryPrice);
    
    /**
     * 平仓
     */
    ContractExperienceTrade closePosition(Long userId, Long tradeId, BigDecimal exitPrice);
    
    /**
     * 根据ID获取交易记录
     */
    ContractExperienceTrade getTradeById(Long tradeId);
    
    /**
     * 获取账户的交易记录
     */
    List<ContractExperienceTrade> getAccountTrades(Long accountId);
    
    /**
     * 获取用户的分页交易记录
     */
    Page<ContractExperienceTrade> getUserTrades(Long userId, Integer page, Integer size);
    
    /**
     * 获取账户的持仓记录
     */
    List<ContractExperienceTrade> getAccountOpenPositions(Long accountId);
    
    /**
     * 计算盈亏
     */
    BigDecimal calculateProfitLoss(BigDecimal entryPrice, BigDecimal exitPrice, BigDecimal positionSize, String tradeType);
}














