/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.service;

import com.cryptotrade.financeproduct.dto.request.FinanceInvestmentRequest;
import com.cryptotrade.financeproduct.entity.FinanceInvestment;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 用户投资管理Service接口
 */
public interface FinanceInvestmentService {
    
    /**
     * 用户投资理财产品
     */
    FinanceInvestment invest(Long userId, FinanceInvestmentRequest request);
    
    /**
     * 根据ID获取投资记录
     */
    FinanceInvestment getInvestmentById(Long investmentId);
    
    /**
     * 根据订单号获取投资记录
     */
    FinanceInvestment getInvestmentByOrderNo(String orderNo);
    
    /**
     * 获取用户的所有投资记录
     */
    List<FinanceInvestment> getUserInvestments(Long userId);
    
    /**
     * 获取用户的分页投资记录
     */
    Page<FinanceInvestment> getUserInvestments(Long userId, Integer page, Integer size);
    
    /**
     * 根据产品ID获取投资记录
     */
    List<FinanceInvestment> getProductInvestments(Long productId);
    
    /**
     * 获取用户的活跃投资记录
     */
    List<FinanceInvestment> getActiveInvestments(Long userId);
    
    /**
     * 计算投资收益（根据产品类型和周期）
     */
    BigDecimal calculateExpectedProfit(Long investmentId);
}














