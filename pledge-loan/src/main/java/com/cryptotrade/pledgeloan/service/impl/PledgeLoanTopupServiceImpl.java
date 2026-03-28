/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import com.cryptotrade.pledgeloan.entity.PledgeLoanTopup;
import com.cryptotrade.pledgeloan.repository.PledgeLoanTopupRepository;
import com.cryptotrade.pledgeloan.service.PledgeLoanOrderService;
import com.cryptotrade.pledgeloan.repository.PledgeLoanOrderRepository;
import com.cryptotrade.pledgeloan.service.AssetService;
import com.cryptotrade.pledgeloan.service.MarketPriceService;
import com.cryptotrade.pledgeloan.service.PledgeLoanTopupService;
import com.cryptotrade.pledgeloan.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 质押借币补仓Service实现
 */
@Service
public class PledgeLoanTopupServiceImpl implements PledgeLoanTopupService {

    @Autowired
    private PledgeLoanTopupRepository topupRepository;

    @Autowired
    private PledgeLoanOrderRepository orderRepository;

    @Autowired
    private PledgeLoanOrderService orderService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private MarketPriceService marketPriceService;

    @Override
    @Transactional
    public PledgeLoanTopup topup(Long orderId, Long userId, BigDecimal topupAmount) {
        PledgeLoanOrder order = orderService.getOrderById(orderId);

        // 检查用户权限
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此订单");
        }

        if (!"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许补仓: " + order.getStatus());
        }

        if (topupAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("补仓金额必须大于0");
        }

        // 获取当前价格
        BigDecimal currentPrice = marketPriceService.getCurrentPrice(order.getPledgeCurrency());

        // 计算补仓价值（USDT）
        BigDecimal topupValue = topupAmount.multiply(currentPrice);

        // 计算补仓前健康度
        BigDecimal healthRateBefore = order.getHealthRate() != null ? order.getHealthRate() : BigDecimal.ZERO;

        // 更新订单质押金额和价值
        BigDecimal newPledgeAmount = order.getPledgeAmount().add(topupAmount);
        BigDecimal newPledgeValue = order.getPledgeValue().add(topupValue);
        order.setPledgeAmount(newPledgeAmount);
        order.setPledgeValue(newPledgeValue);

        // 计算新的健康度
        BigDecimal healthRateAfter = newPledgeValue.divide(order.getLoanValue(), 6, java.math.RoundingMode.HALF_UP);
        order.setHealthRate(healthRateAfter);
        order.setLastHealthCheckTime(LocalDateTime.now());
        orderRepository.save(order);

        // 创建补仓记录
        PledgeLoanTopup topup = new PledgeLoanTopup();
        topup.setOrderId(orderId);
        topup.setOrderNo(order.getOrderNo());
        topup.setUserId(userId);
        topup.setTopupNo(OrderNoGenerator.generateTopupNo());
        topup.setPledgeCurrency(order.getPledgeCurrency());
        topup.setTopupAmount(topupAmount);
        topup.setTopupValue(topupValue);
        topup.setHealthRateBefore(healthRateBefore);
        topup.setHealthRateAfter(healthRateAfter);
        topup.setTopupTime(LocalDateTime.now());

        topupRepository.save(topup);

        // 冻结用户的补仓资产
        boolean freezeSuccess = assetService.freezeAsset(userId, order.getPledgeCurrency(), 
                topupAmount, order.getOrderNo());
        if (!freezeSuccess) {
            throw new RuntimeException("冻结补仓资产失败");
        }

        return topup;
    }

    @Override
    public List<PledgeLoanTopup> getOrderTopups(Long orderId) {
        return topupRepository.findByOrderId(orderId);
    }

    @Override
    public List<PledgeLoanTopup> getUserTopups(Long userId) {
        return topupRepository.findByUserId(userId);
    }
}

