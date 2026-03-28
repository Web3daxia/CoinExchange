/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.service;

import com.cryptotrade.financeproduct.dto.request.FinanceRedemptionRequest;
import com.cryptotrade.financeproduct.entity.FinanceRedemption;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 理财产品赎回管理Service接口
 */
public interface FinanceRedemptionService {
    
    /**
     * 用户赎回投资
     */
    FinanceRedemption redeem(Long userId, FinanceRedemptionRequest request);
    
    /**
     * 根据ID获取赎回记录
     */
    FinanceRedemption getRedemptionById(Long redemptionId);
    
    /**
     * 根据订单号获取赎回记录
     */
    FinanceRedemption getRedemptionByOrderNo(String orderNo);
    
    /**
     * 获取用户的赎回记录
     */
    List<FinanceRedemption> getUserRedemptions(Long userId);
    
    /**
     * 获取用户的分页赎回记录
     */
    Page<FinanceRedemption> getUserRedemptions(Long userId, Integer page, Integer size);
    
    /**
     * 获取投资的赎回记录
     */
    List<FinanceRedemption> getInvestmentRedemptions(Long investmentId);
    
    /**
     * 计算赎回手续费
     */
    java.math.BigDecimal calculateRedemptionFee(Long investmentId, java.math.BigDecimal redemptionAmount);
    
    /**
     * 处理赎回（后台管理）
     */
    void processRedemption(Long redemptionId);
}














