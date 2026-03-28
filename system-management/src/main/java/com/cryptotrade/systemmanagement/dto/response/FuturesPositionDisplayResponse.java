/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.response;

import com.cryptotrade.systemmanagement.entity.UsdtFuturesPositionManagement;
import com.cryptotrade.systemmanagement.util.OrderDisplayUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * U本位永续合约仓位显示响应DTO（包含格式化后的显示字段）
 */
@Data
public class FuturesPositionDisplayResponse {
    private Long userId;
    private String memberName;
    private String contractAccountDisplay; // 合约账户显示：USDT合约账户
    private String contractAccount; // 合约账户原始值
    private BigDecimal availableBalance;
    private BigDecimal frozenBalance;
    private String marginModeDisplay; // 仓位模式显示：全仓、逐仓等
    private String marginMode; // 仓位模式原始值
    private BigDecimal longPosition; // 多仓仓位
    private BigDecimal longFrozen; // 冻结多仓
    private BigDecimal longAvgPrice; // 多仓均买价
    private String longPnlDisplay; // 多仓当前盈亏显示：0.00 | 0.00%
    private BigDecimal longUnrealizedPnl; // 多仓当前盈亏（金额）
    private BigDecimal longUnrealizedPnlPercent; // 多仓当前盈亏（百分比）
    private String longLeverageDisplay; // 多仓杠杆显示：10X
    private Integer longLeverage; // 多仓杠杆原始值
    private BigDecimal longMargin; // 多仓保证金
    private BigDecimal shortPosition; // 空仓仓位
    private BigDecimal shortFrozen; // 冻结空仓
    private BigDecimal shortAvgPrice; // 空仓卖均价
    private String shortPnlDisplay; // 空仓盈亏显示：0.00 | 0.00%
    private BigDecimal shortUnrealizedPnl; // 空仓盈亏（金额）
    private BigDecimal shortUnrealizedPnlPercent; // 空仓盈亏（百分比）
    private String shortLeverageDisplay; // 空仓杠杆显示：10X
    private Integer shortLeverage; // 空仓杠杆原始值
    private BigDecimal shortMargin; // 空仓保证金

    /**
     * 从实体转换
     */
    public static FuturesPositionDisplayResponse from(UsdtFuturesPositionManagement entity) {
        FuturesPositionDisplayResponse response = new FuturesPositionDisplayResponse();
        response.setUserId(entity.getUserId());
        response.setMemberName(entity.getMemberName());
        // 格式化合约账户显示
        response.setContractAccountDisplay(OrderDisplayUtil.formatAccountType(entity.getAccountType()));
        response.setContractAccount(entity.getAccountType());
        response.setAvailableBalance(entity.getAvailableBalance());
        response.setFrozenBalance(entity.getFrozenBalance());
        // 格式化仓位模式显示
        response.setMarginModeDisplay(OrderDisplayUtil.formatMarginMode(entity.getMarginMode()));
        response.setMarginMode(entity.getMarginMode());
        response.setLongPosition(entity.getLongPosition());
        response.setLongFrozen(entity.getLongFrozen());
        response.setLongAvgPrice(entity.getLongAvgPrice());
        // 格式化多仓盈亏显示：0.00 | 0.00%
        String longPnlDisplay = (entity.getLongUnrealizedPnl() != null ? entity.getLongUnrealizedPnl().toString() : "0.00")
                + " | " 
                + (entity.getLongUnrealizedPnlPercent() != null ? entity.getLongUnrealizedPnlPercent().toString() + "%" : "0.00%");
        response.setLongPnlDisplay(longPnlDisplay);
        response.setLongUnrealizedPnl(entity.getLongUnrealizedPnl());
        response.setLongUnrealizedPnlPercent(entity.getLongUnrealizedPnlPercent());
        // 格式化多仓杠杆显示：10X
        response.setLongLeverageDisplay(OrderDisplayUtil.formatLeverage(entity.getLongLeverage()));
        response.setLongLeverage(entity.getLongLeverage());
        response.setLongMargin(entity.getLongMargin());
        response.setShortPosition(entity.getShortPosition());
        response.setShortFrozen(entity.getShortFrozen());
        response.setShortAvgPrice(entity.getShortAvgPrice());
        // 格式化空仓盈亏显示：0.00 | 0.00%
        String shortPnlDisplay = (entity.getShortUnrealizedPnl() != null ? entity.getShortUnrealizedPnl().toString() : "0.00")
                + " | " 
                + (entity.getShortUnrealizedPnlPercent() != null ? entity.getShortUnrealizedPnlPercent().toString() + "%" : "0.00%");
        response.setShortPnlDisplay(shortPnlDisplay);
        response.setShortUnrealizedPnl(entity.getShortUnrealizedPnl());
        response.setShortUnrealizedPnlPercent(entity.getShortUnrealizedPnlPercent());
        // 格式化空仓杠杆显示：10X
        response.setShortLeverageDisplay(OrderDisplayUtil.formatLeverage(entity.getShortLeverage()));
        response.setShortLeverage(entity.getShortLeverage());
        response.setShortMargin(entity.getShortMargin());
        return response;
    }
}














