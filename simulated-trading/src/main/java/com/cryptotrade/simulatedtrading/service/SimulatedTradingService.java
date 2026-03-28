/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.service;

import com.cryptotrade.simulatedtrading.entity.SimulatedTradingRecord;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * 模拟交易Service接口
 */
public interface SimulatedTradingService {
    
    /**
     * 下单（支持所有交易类型）
     */
    SimulatedTradingRecord placeOrder(Long userId, Long accountId, String tradeType, String contractType,
                                      String pairName, String orderType, String side, Integer leverage,
                                      BigDecimal quantity, BigDecimal price, BigDecimal stopLossPrice,
                                      BigDecimal takeProfitPrice);
    
    /**
     * 平仓（用于合约交易）
     */
    SimulatedTradingRecord closePosition(Long userId, Long tradeId, BigDecimal exitPrice);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long tradeId);
    
    /**
     * 根据ID获取交易记录
     */
    SimulatedTradingRecord getTradeById(Long tradeId);
    
    /**
     * 获取账户的交易记录
     */
    List<SimulatedTradingRecord> getAccountTrades(Long accountId);
    
    /**
     * 获取用户的分页交易记录
     */
    Page<SimulatedTradingRecord> getUserTrades(Long userId, Integer page, Integer size);
    
    /**
     * 获取账户的持仓记录
     */
    List<SimulatedTradingRecord> getAccountOpenPositions(Long accountId);
    
    /**
     * 计算盈亏
     */
    BigDecimal calculateProfitLoss(BigDecimal entryPrice, BigDecimal exitPrice, BigDecimal quantity,
                                    String tradeType, String positionSide, Integer leverage);
}














