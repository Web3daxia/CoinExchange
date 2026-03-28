/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service;

import com.cryptotrade.pledgeloan.entity.PledgeLoanLiquidation;

import java.util.List;

/**
 * 质押借币平仓Service接口
 */
public interface PledgeLoanLiquidationService {
    
    /**
     * 自动平仓
     */
    PledgeLoanLiquidation autoLiquidation(Long orderId, String reason);
    
    /**
     * 手动平仓
     */
    PledgeLoanLiquidation manualLiquidation(Long orderId, Long liquidatorId, String reason);
    
    /**
     * 获取订单的平仓记录
     */
    List<PledgeLoanLiquidation> getOrderLiquidations(Long orderId);
    
    /**
     * 获取用户的平仓记录
     */
    List<PledgeLoanLiquidation> getUserLiquidations(Long userId);
}














