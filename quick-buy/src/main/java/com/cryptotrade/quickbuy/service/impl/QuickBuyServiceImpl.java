/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.quickbuy.service.impl;

import com.cryptotrade.quickbuy.entity.QuickBuyOrder;
import com.cryptotrade.quickbuy.repository.QuickBuyOrderRepository;
import com.cryptotrade.quickbuy.service.QuickBuyService;
import com.cryptotrade.spot.service.MarketDataService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 快捷买币服务实现类
 */
@Service
public class QuickBuyServiceImpl implements QuickBuyService {

    @Autowired
    private QuickBuyOrderRepository quickBuyOrderRepository;

    @Autowired
    private MarketDataService marketDataService;

    @Autowired
    private WalletService walletService;

    // 默认手续费率 2%
    private static final BigDecimal DEFAULT_FEE_RATE = new BigDecimal("0.02");

    @Override
    public Map<String, Object> getAvailableOptions() {
        Map<String, Object> options = new HashMap<>();
        
        // 支持的加密货币
        options.put("cryptoCurrencies", Arrays.asList("USDT", "BTC", "ETH", "BNB", "SOL"));
        
        // 支持的法币
        options.put("fiatCurrencies", Arrays.asList("USD", "CNY", "EUR", "GBP", "JPY"));
        
        // 支持的支付方式
        Map<String, List<String>> paymentMethods = new HashMap<>();
        paymentMethods.put("USD", Arrays.asList("PayPal", "BankTransfer", "CreditCard"));
        paymentMethods.put("CNY", Arrays.asList("WeChat", "Alipay", "BankTransfer"));
        paymentMethods.put("EUR", Arrays.asList("PayPal", "BankTransfer", "SEPA"));
        paymentMethods.put("GBP", Arrays.asList("PayPal", "BankTransfer"));
        paymentMethods.put("JPY", Arrays.asList("BankTransfer"));
        options.put("paymentMethods", paymentMethods);

        return options;
    }

    @Override
    @Transactional
    public QuickBuyOrder executeQuickBuy(Long userId, String cryptoCurrency, String fiatCurrency,
                                        String paymentMethod, BigDecimal cryptoAmount) {
        // 获取当前汇率（这里需要从市场数据服务获取）
        BigDecimal exchangeRate = getExchangeRate(cryptoCurrency, fiatCurrency);
        if (exchangeRate == null) {
            throw new RuntimeException("无法获取汇率");
        }

        // 计算法币金额
        BigDecimal fiatAmount = cryptoAmount.multiply(exchangeRate);
        
        // 计算手续费
        BigDecimal fee = fiatAmount.multiply(DEFAULT_FEE_RATE);
        BigDecimal totalFiatAmount = fiatAmount.add(fee);

        // 创建订单
        QuickBuyOrder order = new QuickBuyOrder();
        order.setUserId(userId);
        order.setCryptoCurrency(cryptoCurrency);
        order.setFiatCurrency(fiatCurrency);
        order.setPaymentMethod(paymentMethod);
        order.setCryptoAmount(cryptoAmount);
        order.setFiatAmount(totalFiatAmount);
        order.setExchangeRate(exchangeRate);
        order.setFee(fee);
        order.setStatus("PENDING");

        return quickBuyOrderRepository.save(order);
    }

    @Override
    public List<QuickBuyOrder> getUserOrders(Long userId) {
        return quickBuyOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void confirmPayment(Long userId, Long orderId, String paymentProof) {
        QuickBuyOrder order = quickBuyOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许支付");
        }

        order.setStatus("PAID");
        order.setPaymentProof(paymentProof);
        order.setPaidAt(LocalDateTime.now());
        quickBuyOrderRepository.save(order);

        // TODO: 通知管理员审核支付凭证
        // 审核通过后，将加密货币转入用户钱包
    }

    /**
     * 获取汇率（需要从市场数据服务获取）
     */
    private BigDecimal getExchangeRate(String cryptoCurrency, String fiatCurrency) {
        // TODO: 从市场数据服务获取汇率
        // 这里返回一个默认值，实际应该从市场数据服务获取
        if ("USDT".equals(cryptoCurrency) && "USD".equals(fiatCurrency)) {
            return new BigDecimal("1.0");
        }
        // 其他货币对的汇率需要从市场数据获取
        return new BigDecimal("50000"); // 临时返回值
    }
}















