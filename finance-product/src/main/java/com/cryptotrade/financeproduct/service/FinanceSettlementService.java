/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.service;

import com.cryptotrade.financeproduct.entity.FinanceProfitSettlement;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 理财产品收益结算Service接口
 */
public interface FinanceSettlementService {
    
    /**
     * 结算单个投资的收益
     */
    FinanceProfitSettlement settleInvestmentProfit(Long investmentId);
    
    /**
     * 批量结算投资的收益（按结算周期）
     */
    List<FinanceProfitSettlement> batchSettleProfits(String settlementCycle);
    
    /**
     * 获取投资的结算记录
     */
    List<FinanceProfitSettlement> getSettlementsByInvestmentId(Long investmentId);
    
    /**
     * 获取用户的分页结算记录
     */
    Page<FinanceProfitSettlement> getUserSettlements(Long userId, Integer page, Integer size);
    
    /**
     * 获取待结算的记录
     */
    List<FinanceProfitSettlement> getPendingSettlements();
    
    /**
     * 计算投资收益（根据产品类型）
     */
    java.math.BigDecimal calculateProfit(Long investmentId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);
}














