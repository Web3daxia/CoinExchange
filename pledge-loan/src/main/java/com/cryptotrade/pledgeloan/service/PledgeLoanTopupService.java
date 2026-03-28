/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import com.cryptotrade.pledgeloan.entity.PledgeLoanTopup;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押借币补仓Service接口
 */
public interface PledgeLoanTopupService {
    
    /**
     * 补仓
     */
    PledgeLoanTopup topup(Long orderId, Long userId, BigDecimal topupAmount);
    
    /**
     * 获取订单的补仓记录
     */
    List<PledgeLoanTopup> getOrderTopups(Long orderId);
    
    /**
     * 获取用户的补仓记录
     */
    List<PledgeLoanTopup> getUserTopups(Long userId);
}














