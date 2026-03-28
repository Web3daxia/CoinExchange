/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.flashexchange.service.impl;

import com.cryptotrade.flashexchange.entity.FlashExchangeOrder;
import com.cryptotrade.flashexchange.repository.FlashExchangeOrderRepository;
import com.cryptotrade.flashexchange.service.FlashExchangeService;
import com.cryptotrade.spot.service.MarketDataService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 闪兑交易服务实现类
 */
@Service
public class FlashExchangeServiceImpl implements FlashExchangeService {

    @Autowired
    private FlashExchangeOrderRepository flashExchangeOrderRepository;

    @Autowired
    private MarketDataService marketDataService;

    @Autowired
    private WalletService walletService;

    // 默认手续费率 0.1%
    @Value("${flash-exchange.default-fee-rate:0.001}")
    private BigDecimal defaultFeeRate;

    // 支持的加密货币列表
    private static final List<String> SUPPORTED_CURRENCIES = Arrays.asList(
            "USDT", "BTC", "ETH", "XRP", "ADA", "BNB", "SOL", "DOT", "DOGE", "MATIC"
    );

    @Override
    public Map<String, Object> getSupportedCurrencies(String fromCurrency, String toCurrency) {
        Map<String, Object> result = new HashMap<>();
        result.put("supportedCurrencies", SUPPORTED_CURRENCIES);

        // 如果指定了币种，返回实时汇率
        if (fromCurrency != null && toCurrency != null) {
            BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);
            result.put("exchangeRate", rate);
            result.put("fromCurrency", fromCurrency);
            result.put("toCurrency", toCurrency);
        }

        return result;
    }

    @Override
    public Map<String, Object> calculateExchange(String fromCurrency, String toCurrency, BigDecimal fromAmount) {
        // 获取汇率
        BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);
        
        // 计算目标数量（未扣除手续费）
        BigDecimal toAmountBeforeFee = fromAmount.multiply(exchangeRate);
        
        // 计算手续费
        BigDecimal fee = toAmountBeforeFee.multiply(defaultFeeRate);
        
        // 计算最终目标数量
        BigDecimal toAmount = toAmountBeforeFee.subtract(fee);

        Map<String, Object> result = new HashMap<>();
        result.put("fromCurrency", fromCurrency);
        result.put("toCurrency", toCurrency);
        result.put("fromAmount", fromAmount);
        result.put("toAmount", toAmount);
        result.put("exchangeRate", exchangeRate);
        result.put("fee", fee);
        result.put("feeRate", defaultFeeRate);
        result.put("toAmountBeforeFee", toAmountBeforeFee);

        return result;
    }

    @Override
    @Transactional
    public FlashExchangeOrder executeExchange(Long userId, String fromCurrency, String toCurrency,
                                             BigDecimal fromAmount, BigDecimal maxSlippage) {
        // 检查余额
        BigDecimal balance = walletService.getAvailableBalance(userId, "SPOT", fromCurrency);
        if (balance.compareTo(fromAmount) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 获取预期汇率
        BigDecimal expectedRate = getExchangeRate(fromCurrency, toCurrency);

        // 扣除源币种
        walletService.deductBalance(userId, fromCurrency, "SPOT", fromAmount);

        // 重新获取实时汇率（可能已变化）
        BigDecimal actualRate = getExchangeRate(fromCurrency, toCurrency);

        // 计算滑点
        BigDecimal slippage = calculateSlippage(expectedRate, actualRate);

        // 检查滑点是否超出限制
        if (maxSlippage != null && slippage.abs().compareTo(maxSlippage) > 0) {
            // 退回资金
            walletService.addBalance(userId, fromCurrency, "SPOT", fromAmount);
            throw new RuntimeException("滑点超出限制，交易已取消");
        }

        // 计算目标数量
        BigDecimal toAmountBeforeFee = fromAmount.multiply(actualRate);
        BigDecimal fee = toAmountBeforeFee.multiply(defaultFeeRate);
        BigDecimal toAmount = toAmountBeforeFee.subtract(fee);

        // 生成订单号
        String orderNo = "FE" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 创建订单
        FlashExchangeOrder order = new FlashExchangeOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setFromCurrency(fromCurrency);
        order.setToCurrency(toCurrency);
        order.setFromAmount(fromAmount);
        order.setToAmount(toAmount);
        order.setExchangeRate(actualRate);
        order.setExpectedRate(expectedRate);
        order.setSlippage(slippage);
        order.setMaxSlippage(maxSlippage);
        order.setFee(fee);
        order.setFeeRate(defaultFeeRate);
        order.setStatus("PENDING");

        order = flashExchangeOrderRepository.save(order);

        try {
            // 添加目标币种到钱包
            walletService.addBalance(userId, toCurrency, "SPOT", toAmount);

            // 更新订单状态
            order.setStatus("COMPLETED");
            order.setCompletedAt(LocalDateTime.now());
            flashExchangeOrderRepository.save(order);

            // TODO: 发送通知
        } catch (Exception e) {
            // 如果失败，退回资金
            walletService.addBalance(userId, fromCurrency, "SPOT", fromAmount);
            order.setStatus("FAILED");
            flashExchangeOrderRepository.save(order);
            throw new RuntimeException("兑换失败: " + e.getMessage());
        }

        return order;
    }

    @Override
    public List<FlashExchangeOrder> getUserExchangeHistory(Long userId) {
        return flashExchangeOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public FlashExchangeOrder getOrder(Long orderId) {
        return flashExchangeOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }

    /**
     * 获取汇率
     */
    private BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }

        // TODO: 从市场数据服务获取实时汇率
        // 这里简化处理，实际应该从市场数据服务获取
        // 可以通过交易对价格计算，例如 BTC/USDT 的价格
        
        // 临时实现：返回固定汇率（实际应该从市场数据获取）
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("BTC/USDT", new BigDecimal("50000"));
        rates.put("ETH/USDT", new BigDecimal("3000"));
        rates.put("XRP/USDT", new BigDecimal("0.5"));
        // ... 其他汇率

        // 简化处理：如果都是USDT，返回1
        if (fromCurrency.equals("USDT") && toCurrency.equals("USDT")) {
            return BigDecimal.ONE;
        }

        // 实际应该通过市场数据服务获取
        return new BigDecimal("1.0"); // 临时返回值
    }

    /**
     * 计算滑点
     */
    private BigDecimal calculateSlippage(BigDecimal expectedRate, BigDecimal actualRate) {
        if (expectedRate.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return actualRate.subtract(expectedRate)
                .divide(expectedRate, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")); // 转换为百分比
    }
}




