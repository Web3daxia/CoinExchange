/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.PledgeLoanLiquidation;
import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import com.cryptotrade.pledgeloan.repository.PledgeLoanLiquidationRepository;
import com.cryptotrade.pledgeloan.repository.PledgeLoanOrderRepository;
import com.cryptotrade.pledgeloan.service.AssetService;
import com.cryptotrade.pledgeloan.service.MarketPriceService;
import com.cryptotrade.pledgeloan.service.PledgeLoanLiquidationService;
import com.cryptotrade.pledgeloan.service.PledgeLoanOrderService;
import com.cryptotrade.pledgeloan.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 质押借币平仓Service实现
 */
@Service
public class PledgeLoanLiquidationServiceImpl implements PledgeLoanLiquidationService {

    @Autowired
    private PledgeLoanLiquidationRepository liquidationRepository;

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
    public PledgeLoanLiquidation autoLiquidation(Long orderId, String reason) {
        PledgeLoanOrder order = orderService.getOrderById(orderId);

        if (!"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许平仓: " + order.getStatus());
        }

        // 创建平仓记录
        PledgeLoanLiquidation liquidation = new PledgeLoanLiquidation();
        liquidation.setOrderId(orderId);
        liquidation.setOrderNo(order.getOrderNo());
        liquidation.setUserId(order.getUserId());
        liquidation.setLiquidationNo(OrderNoGenerator.generateLiquidationNo());
        liquidation.setLiquidationType("AUTO");
        liquidation.setLiquidationReason(reason);
        liquidation.setPledgeCurrency(order.getPledgeCurrency());
        liquidation.setPledgeAmount(order.getPledgeAmount());
        liquidation.setPledgeValueBefore(order.getPledgeValue());
        liquidation.setLoanCurrency(order.getLoanCurrency());
        liquidation.setLoanAmount(order.getLoanAmount());
        liquidation.setLoanValue(order.getLoanValue());
        liquidation.setHealthRateBefore(order.getHealthRate() != null ? order.getHealthRate() : BigDecimal.ZERO);
        liquidation.setLiquidationPrice(order.getLiquidationPrice());
        liquidation.setLiquidationTime(LocalDateTime.now());

        liquidationRepository.save(liquidation);

        // 更新订单状态
        order.setStatus("LIQUIDATED");
        orderRepository.save(order);

        // 执行平仓操作
        executeLiquidation(order, liquidation);

        return liquidation;
    }

    /**
     * 执行平仓操作
     */
    private void executeLiquidation(PledgeLoanOrder order, PledgeLoanLiquidation liquidation) {
        // 获取当前质押资产价格
        BigDecimal currentPrice = marketPriceService.getCurrentPrice(order.getPledgeCurrency());
        
        // 计算卖出质押资产所得（USDT）
        BigDecimal sellAmount = order.getPledgeAmount().multiply(currentPrice);
        
        // 计算需要偿还的金额（借款本金 + 未付利息）
        BigDecimal totalDebt = order.getRemainingPrincipal().add(
                order.getTotalInterest().subtract(order.getPaidInterest())
        );
        
        // 转换借款币种数量为USDT
        BigDecimal loanPrice = marketPriceService.getCurrentPrice(order.getLoanCurrency());
        BigDecimal debtInUsdt = totalDebt.multiply(loanPrice);
        
        // 解冻并扣除质押资产
        assetService.unfreezeAsset(order.getUserId(), order.getPledgeCurrency(), 
                order.getPledgeAmount(), order.getOrderNo());
        assetService.deductAsset(order.getUserId(), order.getPledgeCurrency(), 
                order.getPledgeAmount(), order.getOrderNo());
        
        // 用卖出所得偿还借款
        if (sellAmount.compareTo(debtInUsdt) >= 0) {
            // 卖出所得足够偿还借款
            // 扣除借款（按USDT等值计算）
            BigDecimal deductAmount = debtInUsdt.divide(loanPrice, 8, java.math.RoundingMode.HALF_UP);
            assetService.deductAsset(order.getUserId(), order.getLoanCurrency(), 
                    deductAmount, order.getOrderNo());
            
            // 如果有剩余，返还给用户（转换为质押币种）
            BigDecimal remaining = sellAmount.subtract(debtInUsdt);
            BigDecimal remainingInPledgeCurrency = remaining.divide(currentPrice, 8, java.math.RoundingMode.HALF_UP);
            assetService.addAsset(order.getUserId(), order.getPledgeCurrency(), 
                    remainingInPledgeCurrency, order.getOrderNo());
            
            liquidation.setPledgeValueAfter(remaining);
            liquidation.setHasDebt(false);
            liquidation.setDebtAmount(BigDecimal.ZERO);
            liquidation.setDebtAmountUsdt(BigDecimal.ZERO);
            
            // 更新订单状态为已还清
            order.setRemainingPrincipal(BigDecimal.ZERO);
            order.setStatus("LIQUIDATED");
            
            liquidationRepository.save(liquidation);
            orderRepository.save(order);
        } else {
            // 卖出所得不足以偿还借款，全部用于偿还
            BigDecimal deductAmount = sellAmount.divide(loanPrice, 8, java.math.RoundingMode.HALF_UP);
            assetService.deductAsset(order.getUserId(), order.getLoanCurrency(), 
                    deductAmount, order.getOrderNo());
            
            liquidation.setPledgeValueAfter(BigDecimal.ZERO);
            
            // 记录用户欠款（卖出所得不足的部分）
            BigDecimal unpaidDebt = debtInUsdt.subtract(sellAmount); // 未偿还的债务（USDT）
            BigDecimal unpaidDebtInLoanCurrency = unpaidDebt.divide(loanPrice, 8, java.math.RoundingMode.HALF_UP);
            
            liquidation.setHasDebt(true);
            liquidation.setDebtAmount(unpaidDebtInLoanCurrency);
            liquidation.setDebtAmountUsdt(unpaidDebt);
            
            // 更新订单的剩余本金为欠款金额
            order.setRemainingPrincipal(unpaidDebtInLoanCurrency);
            order.setStatus("LIQUIDATED_WITH_DEBT"); // 平仓但仍有欠款
            
            // 记录欠款到备注中
            liquidation.setRemark(String.format("平仓后仍有欠款: %s %s (等值 %s USDT)", 
                    unpaidDebtInLoanCurrency.toPlainString(), 
                    order.getLoanCurrency(), 
                    unpaidDebt.toPlainString()));
            
            liquidationRepository.save(liquidation);
            orderRepository.save(order);
        }
    }

    @Override
    @Transactional
    public PledgeLoanLiquidation manualLiquidation(Long orderId, Long liquidatorId, String reason) {
        PledgeLoanOrder order = orderService.getOrderById(orderId);

        if (!"ACTIVE".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不允许平仓: " + order.getStatus());
        }

        // 创建平仓记录
        PledgeLoanLiquidation liquidation = new PledgeLoanLiquidation();
        liquidation.setOrderId(orderId);
        liquidation.setOrderNo(order.getOrderNo());
        liquidation.setUserId(order.getUserId());
        liquidation.setLiquidationNo(OrderNoGenerator.generateLiquidationNo());
        liquidation.setLiquidationType("MANUAL");
        liquidation.setLiquidationReason(reason);
        liquidation.setPledgeCurrency(order.getPledgeCurrency());
        liquidation.setPledgeAmount(order.getPledgeAmount());
        liquidation.setPledgeValueBefore(order.getPledgeValue());
        liquidation.setLoanCurrency(order.getLoanCurrency());
        liquidation.setLoanAmount(order.getLoanAmount());
        liquidation.setLoanValue(order.getLoanValue());
        liquidation.setHealthRateBefore(order.getHealthRate() != null ? order.getHealthRate() : BigDecimal.ZERO);
        liquidation.setLiquidationPrice(order.getLiquidationPrice());
        liquidation.setLiquidationTime(LocalDateTime.now());
        liquidation.setLiquidatorId(liquidatorId);

        liquidationRepository.save(liquidation);

        // 更新订单状态
        order.setStatus("LIQUIDATED");
        orderRepository.save(order);

        return liquidation;
    }

    @Override
    public List<PledgeLoanLiquidation> getOrderLiquidations(Long orderId) {
        return liquidationRepository.findByOrderId(orderId);
    }

    @Override
    public List<PledgeLoanLiquidation> getUserLiquidations(Long userId) {
        return liquidationRepository.findByUserId(userId);
    }
}

