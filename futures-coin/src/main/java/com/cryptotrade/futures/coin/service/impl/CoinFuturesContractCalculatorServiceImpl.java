/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.dto.request.CoinFuturesCalculatorRequest;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesCalculatorResponse;
import com.cryptotrade.futures.coin.service.CoinFuturesContractCalculatorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 币本位合约计算器服务实现
 */
@Service
public class CoinFuturesContractCalculatorServiceImpl implements CoinFuturesContractCalculatorService {
    
    @Override
    public CoinFuturesCalculatorResponse calculate(CoinFuturesCalculatorRequest request) {
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
        
        CoinFuturesCalculatorResponse response = new CoinFuturesCalculatorResponse();
        response.setMarkPrice(markPrice);
        
        // 计算初始保证金
        // 币本位合约 - 逐仓：初始保证金 = 面值 × |张数| × 合约乘数 / (开仓均价 × 杠杆倍数)
        BigDecimal initialMargin = faceValue
            .multiply(quantity.abs())
            .multiply(contractMultiplier)
            .divide(entryPrice.multiply(new BigDecimal(leverage)), 8, RoundingMode.HALF_UP);
        response.setInitialMargin(initialMargin);
        
        // 如果是逐仓模式且提供了保证金余额，使用提供的保证金余额
        if ("ISOLATED".equals(marginMode) && marginBalance != null) {
            // 使用提供的保证金余额
        } else {
            marginBalance = initialMargin;
        }
        
        // 计算维持保证金
        // 币本位保证金合约：维持保证金 = 面值 × |张数| × 合约乘数 × 维持保证金率 / 标记价格
        BigDecimal maintenanceMargin = faceValue
            .multiply(quantity.abs())
            .multiply(contractMultiplier)
            .multiply(maintenanceMarginRate)
            .divide(markPrice, 8, RoundingMode.HALF_UP);
        response.setMaintenanceMargin(maintenanceMargin);
        
        // 计算未实现盈亏
        BigDecimal unrealizedPnl;
        if ("LONG".equals(positionSide)) {
            // 多仓收益 = 面值 × |张数| × 合约乘数 × (1/开仓均价 - 1/标记价格)
            BigDecimal entryPriceReciprocal = BigDecimal.ONE.divide(entryPrice, 8, RoundingMode.HALF_UP);
            BigDecimal markPriceReciprocal = BigDecimal.ONE.divide(markPrice, 8, RoundingMode.HALF_UP);
            unrealizedPnl = faceValue
                .multiply(quantity.abs())
                .multiply(contractMultiplier)
                .multiply(entryPriceReciprocal.subtract(markPriceReciprocal));
        } else {
            // 空仓收益 = 面值 × |张数| × 合约乘数 × (1/标记价格 - 1/开仓均价)
            BigDecimal entryPriceReciprocal = BigDecimal.ONE.divide(entryPrice, 8, RoundingMode.HALF_UP);
            BigDecimal markPriceReciprocal = BigDecimal.ONE.divide(markPrice, 8, RoundingMode.HALF_UP);
            unrealizedPnl = faceValue
                .multiply(quantity.abs())
                .multiply(contractMultiplier)
                .multiply(markPriceReciprocal.subtract(entryPriceReciprocal));
        }
        response.setUnrealizedPnl(unrealizedPnl);
        
        // 计算收益率 = 收益 / 开仓保证金
        BigDecimal profitRate = unrealizedPnl
            .divide(initialMargin, 8, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
        response.setProfitRate(profitRate);
        
        // 计算保证金率（逐仓模式）
        if ("ISOLATED".equals(marginMode)) {
            // 保证金率 = (保证金余额 + 收益) / (面值 × |张数| / 标记价格 × (维持保证金率 + 手续费率))
            BigDecimal denominator = faceValue
                .multiply(quantity.abs())
                .divide(markPrice, 8, RoundingMode.HALF_UP)
                .multiply(maintenanceMarginRate.add(feeRate));
            BigDecimal marginRate = marginBalance.add(unrealizedPnl)
                .divide(denominator, 8, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
            response.setMarginRate(marginRate);
        }
        
        // 计算强平价格（逐仓模式）
        if ("ISOLATED".equals(marginMode)) {
            BigDecimal liquidationPrice;
            BigDecimal faceValueTimesQuantity = faceValue.multiply(quantity.abs());
            BigDecimal entryPriceReciprocal = BigDecimal.ONE.divide(entryPrice, 8, RoundingMode.HALF_UP);
            
            if ("LONG".equals(positionSide)) {
                // 多仓预估强平价 = 面值 × |张数| × (维持保证金率 + 手续费率 + 1) / (保证金余额 + 面值 × |张数| / 开仓均价)
                BigDecimal numerator = faceValueTimesQuantity
                    .multiply(maintenanceMarginRate.add(feeRate).add(BigDecimal.ONE));
                BigDecimal denominator = marginBalance.add(
                    faceValueTimesQuantity.multiply(entryPriceReciprocal)
                );
                liquidationPrice = numerator.divide(denominator, 8, RoundingMode.HALF_UP);
            } else {
                // 空仓预估强平价 = 面值 × |张数| × (维持保证金率 + 手续费率 - 1) / (保证金余额 - 面值 × |张数| / 开仓均价)
                BigDecimal numerator = faceValueTimesQuantity
                    .multiply(maintenanceMarginRate.add(feeRate).subtract(BigDecimal.ONE));
                BigDecimal denominator = marginBalance.subtract(
                    faceValueTimesQuantity.multiply(entryPriceReciprocal)
                );
                liquidationPrice = numerator.divide(denominator, 8, RoundingMode.HALF_UP);
            }
            response.setLiquidationPrice(liquidationPrice);
        }
        
        // 计算平仓价格（盈亏为0的价格）
        BigDecimal breakEvenPrice = entryPrice; // 对于币本位合约，平仓价格就是开仓均价
        response.setBreakEvenPrice(breakEvenPrice);
        
        return response;
    }
}














