/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import com.cryptotrade.pledgeloan.entity.PledgeLoanRepayment;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押借币还款Service接口
 */
public interface PledgeLoanRepaymentService {
    
    /**
     * 全额还款
     */
    PledgeLoanRepayment repayFull(Long orderId, Long userId);
    
    /**
     * 部分还款（本金）
     */
    PledgeLoanRepayment repayPartial(Long orderId, Long userId, BigDecimal principalAmount);
    
    /**
     * 还款利息
     */
    PledgeLoanRepayment repayInterest(Long orderId, Long userId, BigDecimal interestAmount);
    
    /**
     * 计算应还利息
     */
    BigDecimal calculateInterest(Long orderId);
    
    /**
     * 获取订单的还款记录
     */
    List<PledgeLoanRepayment> getOrderRepayments(Long orderId);
    
    /**
     * 获取用户的还款记录
     */
    List<PledgeLoanRepayment> getUserRepayments(Long userId);
}














