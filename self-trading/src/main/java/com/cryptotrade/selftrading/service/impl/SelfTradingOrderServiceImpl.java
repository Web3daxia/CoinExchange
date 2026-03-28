/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.service.impl;

import com.cryptotrade.selftrading.entity.SelfTradingOrder;
import com.cryptotrade.selftrading.entity.TradingAd;
import com.cryptotrade.selftrading.repository.SelfTradingOrderRepository;
import com.cryptotrade.selftrading.repository.TradingAdRepository;
import com.cryptotrade.selftrading.service.SelfTradingOrderService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 自选交易订单服务实现类
 */
@Service
public class SelfTradingOrderServiceImpl implements SelfTradingOrderService {

    @Autowired
    private SelfTradingOrderRepository selfTradingOrderRepository;

    @Autowired
    private TradingAdRepository tradingAdRepository;

    @Autowired
    private WalletService walletService;

    @Override
    @Transactional
    public SelfTradingOrder createOrder(Long userId, Long adId, BigDecimal cryptoAmount) {
        TradingAd ad = tradingAdRepository.findById(adId)
                .orElseThrow(() -> new RuntimeException("广告不存在"));

        if (!"ACTIVE".equals(ad.getStatus())) {
            throw new RuntimeException("广告不可用");
        }

        // 计算法币金额
        BigDecimal fiatAmount = cryptoAmount.multiply(ad.getPrice());

        // 检查金额限制
        if (ad.getMinAmount() != null && fiatAmount.compareTo(ad.getMinAmount()) < 0) {
            throw new RuntimeException("交易金额低于最小限额");
        }
        if (fiatAmount.compareTo(ad.getMaxAmount()) > 0) {
            throw new RuntimeException("交易金额超过最大限额");
        }
        if (fiatAmount.compareTo(ad.getAvailableAmount()) > 0) {
            throw new RuntimeException("可用金额不足");
        }

        // 生成订单号
        String orderNo = "ST" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 创建订单
        SelfTradingOrder order = new SelfTradingOrder();
        order.setOrderNo(orderNo);
        order.setMerchantId(ad.getMerchantId());
        order.setUserId(userId);
        order.setAdId(adId);
        order.setOrderType(ad.getAdType().equals("SELL") ? "BUY" : "SELL");
        order.setCryptoCurrency(ad.getCryptoCurrency());
        order.setFiatCurrency(ad.getFiatCurrency());
        order.setPaymentMethod(""); // TODO: 从请求中获取
        order.setCryptoAmount(cryptoAmount);
        order.setFiatAmount(fiatAmount);
        order.setPrice(ad.getPrice());
        order.setStatus("PENDING");
        order.setProcessingTimeout(30); // 默认30分钟

        order = selfTradingOrderRepository.save(order);

        // 更新广告可用金额
        ad.setAvailableAmount(ad.getAvailableAmount().subtract(fiatAmount));
        ad.setOrderCount(ad.getOrderCount() + 1);
        tradingAdRepository.save(ad);

        return order;
    }

    @Override
    @Transactional
    public void confirmPayment(Long userId, Long orderId, String paymentProof) {
        SelfTradingOrder order = selfTradingOrderRepository.findById(orderId)
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
        selfTradingOrderRepository.save(order);
    }

    @Override
    @Transactional
    public void releaseCrypto(Long merchantId, Long orderId) {
        SelfTradingOrder order = selfTradingOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PAID".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许放币");
        }

        // 将加密货币转入用户钱包
        walletService.addBalance(order.getUserId(), order.getCryptoCurrency(), "SPOT", order.getCryptoAmount());

        order.setStatus("RELEASED");
        order.setReleasedAt(LocalDateTime.now());
        selfTradingOrderRepository.save(order);

        // TODO: 更新商家统计数据
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId, String reason) {
        SelfTradingOrder order = selfTradingOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"PENDING".equals(order.getStatus()) && !"PAID".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }

        order.setStatus("CANCELLED");
        order.setCancelledReason(reason);
        order.setCancelledAt(LocalDateTime.now());
        selfTradingOrderRepository.save(order);

        // 恢复广告可用金额
        TradingAd ad = tradingAdRepository.findById(order.getAdId())
                .orElse(null);
        if (ad != null) {
            ad.setAvailableAmount(ad.getAvailableAmount().add(order.getFiatAmount()));
            tradingAdRepository.save(ad);
        }
    }

    @Override
    public List<SelfTradingOrder> getUserOrders(Long userId, String status) {
        if (status != null && !status.isEmpty()) {
            return selfTradingOrderRepository.findByUserIdAndStatus(userId, status);
        }
        return selfTradingOrderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<SelfTradingOrder> getMerchantOrders(Long merchantId, String status) {
        if (status != null && !status.isEmpty()) {
            return selfTradingOrderRepository.findByMerchantIdAndStatus(merchantId, status);
        }
        return selfTradingOrderRepository.findByMerchantIdOrderByCreatedAtDesc(merchantId);
    }

    @Override
    public SelfTradingOrder getOrder(Long orderId) {
        return selfTradingOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
    }
}















