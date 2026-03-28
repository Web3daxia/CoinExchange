/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.service.impl;

import com.cryptotrade.financeproduct.dto.request.FinanceRedemptionRequest;
import com.cryptotrade.financeproduct.entity.FinanceInvestment;
import com.cryptotrade.financeproduct.entity.FinanceProduct;
import com.cryptotrade.financeproduct.entity.FinanceRedemption;
import com.cryptotrade.financeproduct.repository.FinanceInvestmentRepository;
import com.cryptotrade.financeproduct.repository.FinanceProductRepository;
import com.cryptotrade.financeproduct.repository.FinanceRedemptionRepository;
import com.cryptotrade.financeproduct.service.FinanceRedemptionService;
import com.cryptotrade.financeproduct.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 理财产品赎回管理Service实现
 */
@Service
public class FinanceRedemptionServiceImpl implements FinanceRedemptionService {

    @Autowired
    private FinanceRedemptionRepository redemptionRepository;

    @Autowired
    private FinanceInvestmentRepository investmentRepository;

    @Autowired
    private FinanceProductRepository productRepository;

    @Override
    @Transactional
    public FinanceRedemption redeem(Long userId, FinanceRedemptionRequest request) {
        FinanceInvestment investment = investmentRepository.findById(request.getInvestmentId())
                .orElseThrow(() -> new RuntimeException("投资记录不存在: " + request.getInvestmentId()));

        // 检查用户权限
        if (!investment.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此投资记录");
        }

        FinanceProduct product = productRepository.findById(investment.getProductId())
                .orElseThrow(() -> new RuntimeException("理财产品不存在"));

        // 检查投资状态
        if (!"ACTIVE".equals(investment.getStatus())) {
            throw new RuntimeException("投资记录状态不允许赎回: " + investment.getStatus());
        }

        // 确定赎回金额
        BigDecimal redemptionAmount;
        BigDecimal principalAmount;
        BigDecimal profitAmount = BigDecimal.ZERO;

        if ("FULL".equals(request.getRedemptionType())) {
            // 全额赎回
            redemptionAmount = investment.getRemainingPrincipal().add(investment.getAccumulatedProfit());
            principalAmount = investment.getRemainingPrincipal();
            profitAmount = investment.getAccumulatedProfit();
        } else {
            // 部分赎回
            if (request.getRedemptionAmount() == null) {
                throw new RuntimeException("部分赎回需要指定赎回金额");
            }
            if (request.getRedemptionAmount().compareTo(investment.getRemainingPrincipal()) > 0) {
                throw new RuntimeException("赎回金额不能大于剩余本金");
            }
            redemptionAmount = request.getRedemptionAmount();
            principalAmount = request.getRedemptionAmount();
        }

        // 检查锁仓期（定期理财）
        if ("FIXED".equals(product.getProductType()) && investment.getLockUntil() != null) {
            if (LocalDateTime.now().isBefore(investment.getLockUntil())) {
                throw new RuntimeException("投资仍在锁仓期内，无法赎回");
            }
        }

        // 计算赎回手续费（如果提前赎回）
        BigDecimal redemptionFee = calculateRedemptionFee(investment.getId(), redemptionAmount);

        // 创建赎回记录
        FinanceRedemption redemption = new FinanceRedemption();
        redemption.setInvestmentId(investment.getId());
        redemption.setUserId(userId);
        redemption.setProductId(investment.getProductId());
        redemption.setRedemptionAmount(redemptionAmount);
        redemption.setPrincipalAmount(principalAmount);
        redemption.setProfitAmount(profitAmount);
        redemption.setCurrency(investment.getCurrency());
        redemption.setRedemptionType(request.getRedemptionType());
        redemption.setRedemptionFee(redemptionFee);
        redemption.setStatus("PENDING");
        redemption.setRedemptionOrderNo(OrderNoGenerator.generateRedemptionOrderNo());

        FinanceRedemption savedRedemption = redemptionRepository.save(redemption);

        // 更新投资记录
        investment.setRedeemedAmount(investment.getRedeemedAmount().add(principalAmount));
        investment.setRemainingPrincipal(investment.getRemainingPrincipal().subtract(principalAmount));

        // 如果全额赎回，更新投资状态
        if ("FULL".equals(request.getRedemptionType())) {
            investment.setStatus("REDEEMED");
            investment.setAccumulatedProfit(BigDecimal.ZERO);
            investment.setActualProfit(BigDecimal.ZERO);
        }

        investmentRepository.save(investment);

        // 如果活期理财，立即处理赎回
        if ("FLEXIBLE".equals(product.getProductType())) {
            processRedemption(savedRedemption.getId());
        }

        return savedRedemption;
    }

    @Override
    public FinanceRedemption getRedemptionById(Long redemptionId) {
        return redemptionRepository.findById(redemptionId)
                .orElseThrow(() -> new RuntimeException("赎回记录不存在: " + redemptionId));
    }

    @Override
    public FinanceRedemption getRedemptionByOrderNo(String orderNo) {
        return redemptionRepository.findByRedemptionOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("赎回记录不存在: " + orderNo));
    }

    @Override
    public List<FinanceRedemption> getUserRedemptions(Long userId) {
        return redemptionRepository.findByUserId(userId);
    }

    @Override
    public Page<FinanceRedemption> getUserRedemptions(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return redemptionRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<FinanceRedemption> getInvestmentRedemptions(Long investmentId) {
        return redemptionRepository.findByInvestmentId(investmentId);
    }

    @Override
    public BigDecimal calculateRedemptionFee(Long investmentId, BigDecimal redemptionAmount) {
        FinanceInvestment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new RuntimeException("投资记录不存在: " + investmentId));

        FinanceProduct product = productRepository.findById(investment.getProductId())
                .orElseThrow(() -> new RuntimeException("理财产品不存在"));

        // 如果是活期理财，通常没有手续费
        if ("FLEXIBLE".equals(product.getProductType())) {
            return BigDecimal.ZERO;
        }

        // 如果是定期理财且提前赎回，计算手续费
        if ("FIXED".equals(product.getProductType()) && investment.getLockUntil() != null) {
            if (LocalDateTime.now().isBefore(investment.getLockUntil())) {
                // 提前赎回，收取一定比例的手续费（例如1%）
                return redemptionAmount.multiply(BigDecimal.valueOf(0.01));
            }
        }

        return BigDecimal.ZERO;
    }

    @Override
    @Transactional
    public void processRedemption(Long redemptionId) {
        FinanceRedemption redemption = getRedemptionById(redemptionId);

        if (!"PENDING".equals(redemption.getStatus())) {
            throw new RuntimeException("赎回记录状态不允许处理: " + redemption.getStatus());
        }

        // TODO: 解冻用户资金并发放到账户

        // 更新赎回状态
        redemption.setStatus("COMPLETED");
        redemption.setCompletedTime(LocalDateTime.now());
        redemptionRepository.save(redemption);
    }
}














