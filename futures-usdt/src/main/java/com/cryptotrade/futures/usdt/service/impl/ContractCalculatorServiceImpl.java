/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.futures.usdt.dto.request.ContractCalculatorRequest;
import com.cryptotrade.futures.usdt.dto.response.ContractCalculatorResponse;
import com.cryptotrade.futures.usdt.service.ContractCalculatorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * USDT本位合约计算器服务实现
 */
@Service
public class ContractCalculatorServiceImpl implements ContractCalculatorService {
    
    @Override
    public ContractCalculatorResponse calculate(ContractCalculatorRequest request) {
        BigDecimal entryPrice = request.getEntryPrice();
        BigDecimal quantity = request.getQuantity();
        Integer leverage = request.getLeverage();
        String positionSide = request.getPositionSide();
        String marginMode = request.getMarginMode();
        BigDecimal markPrice = request.getMarkPrice() != null ? request.getMarkPrice() : entryPrice;
        BigDecimal faceValue = request.getFaceValue();
        BigDecimal contractMultiplier = request.getContractMultiplier();
        BigDecimal maintenanceMarginRate = request.getMaintenanceMarginRate();
        BigDecimal feeRate = request.getFeeRate();
        BigDecimal marginBalance = request.getMarginBalance();
        
        ContractCalculatorResponse response = new ContractCalculatorResponse();
        response.setMarkPrice(markPrice);
        
        // 计算初始保证金
        // USDT本位合约：初始保证金 = 面值 × |张数| × 合约乘数 × 开仓均价 / 杠杆倍数
        BigDecimal initialMargin = faceValue
            .multiply(quantity.abs())
            .multiply(contractMultiplier)
            .multiply(entryPrice)
            .divide(new BigDecimal(leverage), 8, RoundingMode.HALF_UP);
        response.setInitialMargin(initialMargin);
        
        // 如果是逐仓模式且提供了保证金余额，使用提供的保证金余额
        if ("ISOLATED".equals(marginMode) && marginBalance != null) {
            // 使用提供的保证金余额
        } else {
            marginBalance = initialMargin;
        }
        
        // 计算维持保证金
        // USDT保证金合约：维持保证金 = 面值 × |张数| × 合约乘数 × 维持保证金率 × 标记价格
        BigDecimal maintenanceMargin = faceValue
            .multiply(quantity.abs())
            .multiply(contractMultiplier)
            .multiply(maintenanceMarginRate)
            .multiply(markPrice);
        response.setMaintenanceMargin(maintenanceMargin);
        
        // 计算未实现盈亏
        BigDecimal unrealizedPnl;
        if ("LONG".equals(positionSide)) {
            // 多仓收益 = 面值 × |张数| × 合约乘数 × (标记价格 - 开仓均价)
            unrealizedPnl = faceValue
                .multiply(quantity.abs())
                .multiply(contractMultiplier)
                .multiply(markPrice.subtract(entryPrice));
        } else {
            // 空仓收益 = 面值 × |张数| × 合约乘数 × (开仓均价 - 标记价格)
            unrealizedPnl = faceValue
                .multiply(quantity.abs())
                .multiply(contractMultiplier)
                .multiply(entryPrice.subtract(markPrice));
        }
        response.setUnrealizedPnl(unrealizedPnl);
        
        // 计算收益率 = 收益 / 开仓保证金
        BigDecimal profitRate = unrealizedPnl
            .divide(initialMargin, 8, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
        response.setProfitRate(profitRate);
        
        // 计算保证金率（逐仓模式）
        if ("ISOLATED".equals(marginMode)) {
            // 保证金率 = (保证金余额 + 收益) / (面值 × |张数| × 标记价格 × (维持保证金率 + 手续费率))
            BigDecimal denominator = faceValue
                .multiply(quantity.abs())
                .multiply(markPrice)
                .multiply(maintenanceMarginRate.add(feeRate));
            BigDecimal marginRate = marginBalance.add(unrealizedPnl)
                .divide(denominator, 8, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
            response.setMarginRate(marginRate);
        }
        
        // 计算强平价格（逐仓模式）
        if ("ISOLATED".equals(marginMode)) {
            BigDecimal liquidationPrice;
            if ("LONG".equals(positionSide)) {
                // 多仓预估强平价 = (保证金余额 - 面值 × |张数| × 开仓均价) / (面值 × |张数| × (维持保证金率 + 手续费率 - 1))
                BigDecimal numerator = marginBalance.subtract(
                    faceValue.multiply(quantity.abs()).multiply(entryPrice)
                );
                BigDecimal denominator = faceValue
                    .multiply(quantity.abs())
                    .multiply(maintenanceMarginRate.add(feeRate).subtract(BigDecimal.ONE));
                liquidationPrice = numerator.divide(denominator, 8, RoundingMode.HALF_UP);
            } else {
                // 空仓预估强平价 = (保证金余额 + 面值 × |张数| × 开仓均价) / (面值 × |张数| × (维持保证金率 + 手续费率 + 1))
                BigDecimal numerator = marginBalance.add(
                    faceValue.multiply(quantity.abs()).multiply(entryPrice)
                );
                BigDecimal denominator = faceValue
                    .multiply(quantity.abs())
                    .multiply(maintenanceMarginRate.add(feeRate).add(BigDecimal.ONE));
                liquidationPrice = numerator.divide(denominator, 8, RoundingMode.HALF_UP);
            }
            response.setLiquidationPrice(liquidationPrice);
        }
        
        // 计算平仓价格（盈亏为0的价格）
        BigDecimal breakEvenPrice = entryPrice; // 对于USDT本位合约，平仓价格就是开仓均价
        response.setBreakEvenPrice(breakEvenPrice);
        
        return response;
    }
}














