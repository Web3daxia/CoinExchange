/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.service.impl;

import com.cryptotrade.financeproduct.entity.FinanceInvestment;
import com.cryptotrade.financeproduct.entity.FinanceProduct;
import com.cryptotrade.financeproduct.entity.FinanceProfitSettlement;
import com.cryptotrade.financeproduct.repository.FinanceInvestmentRepository;
import com.cryptotrade.financeproduct.repository.FinanceProductRepository;
import com.cryptotrade.financeproduct.repository.FinanceProfitSettlementRepository;
import com.cryptotrade.financeproduct.service.FinanceSettlementService;
import com.cryptotrade.financeproduct.util.OrderNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 理财产品收益结算Service实现
 */
@Service
public class FinanceSettlementServiceImpl implements FinanceSettlementService {

    @Autowired
    private FinanceProfitSettlementRepository settlementRepository;

    @Autowired
    private FinanceInvestmentRepository investmentRepository;

    @Autowired
    private FinanceProductRepository productRepository;

    @Override
    @Transactional
    public FinanceProfitSettlement settleInvestmentProfit(Long investmentId) {
        FinanceInvestment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new RuntimeException("投资记录不存在: " + investmentId));

        FinanceProduct product = productRepository.findById(investment.getProductId())
                .orElseThrow(() -> new RuntimeException("理财产品不存在"));

        // 检查是否需要结算
        if (investment.getNextSettlementTime() == null || 
            LocalDateTime.now().isBefore(investment.getNextSettlementTime())) {
            throw new RuntimeException("尚未到达结算时间");
        }

        // 计算结算周期
        LocalDateTime periodStart = investment.getLastSettlementTime() != null ? 
                investment.getLastSettlementTime() : investment.getStartDate();
        LocalDateTime periodEnd = LocalDateTime.now();

        // 计算收益
        BigDecimal profit = calculateProfit(investment, periodStart, periodEnd);

        // 创建结算记录
        FinanceProfitSettlement settlement = new FinanceProfitSettlement();
        settlement.setInvestmentId(investmentId);
        settlement.setUserId(investment.getUserId());
        settlement.setProductId(investment.getProductId());
        settlement.setSettlementPeriodStart(periodStart);
        settlement.setSettlementPeriodEnd(periodEnd);
        settlement.setPrincipalAmount(investment.getRemainingPrincipal());
        settlement.setProfitAmount(profit);
        settlement.setCurrency(investment.getCurrency());
        settlement.setAnnualRate(product.getAnnualRate());
        settlement.setSettlementType("INTEREST");
        settlement.setSettlementStatus("SETTLED");
        settlement.setSettlementTime(LocalDateTime.now());
        settlement.setSettlementOrderNo(OrderNoGenerator.generateSettlementOrderNo());

        FinanceProfitSettlement savedSettlement = settlementRepository.save(settlement);

        // 更新投资记录
        investment.setActualProfit(investment.getActualProfit().add(profit));
        investment.setAccumulatedProfit(investment.getAccumulatedProfit().add(profit));
        investment.setLastSettlementTime(periodEnd);

        // 计算下次结算时间
        if (product.getSettlementCycle() != null) {
            switch (product.getSettlementCycle()) {
                case "DAILY":
                    investment.setNextSettlementTime(periodEnd.plusDays(1));
                    break;
                case "WEEKLY":
                    investment.setNextSettlementTime(periodEnd.plusWeeks(1));
                    break;
                case "MONTHLY":
                    investment.setNextSettlementTime(periodEnd.plusMonths(1));
                    break;
            }
        }

        investmentRepository.save(investment);

        // TODO: 将收益发放到用户账户

        return savedSettlement;
    }

    @Override
    @Transactional
    public List<FinanceProfitSettlement> batchSettleProfits(String settlementCycle) {
        List<FinanceProfitSettlement> settlements = new ArrayList<>();
        
        // 获取需要结算的投资记录
        List<FinanceInvestment> investments = investmentRepository.findAll();
        for (FinanceInvestment investment : investments) {
            FinanceProduct product = productRepository.findById(investment.getProductId()).orElse(null);
            if (product == null || !settlementCycle.equals(product.getSettlementCycle())) {
                continue;
            }

            // 检查是否需要结算
            if (investment.getNextSettlementTime() != null && 
                LocalDateTime.now().isAfter(investment.getNextSettlementTime()) &&
                "ACTIVE".equals(investment.getStatus())) {
                try {
                    FinanceProfitSettlement settlement = settleInvestmentProfit(investment.getId());
                    settlements.add(settlement);
                } catch (Exception e) {
                    // 记录错误日志
                    e.printStackTrace();
                }
            }
        }

        return settlements;
    }

    @Override
    public List<FinanceProfitSettlement> getSettlementsByInvestmentId(Long investmentId) {
        return settlementRepository.findByInvestmentId(investmentId);
    }

    @Override
    public Page<FinanceProfitSettlement> getUserSettlements(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return settlementRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<FinanceProfitSettlement> getPendingSettlements() {
        return settlementRepository.findBySettlementStatus("PENDING");
    }

    @Override
    public BigDecimal calculateProfit(Long investmentId, LocalDateTime startTime, LocalDateTime endTime) {
        FinanceInvestment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new RuntimeException("投资记录不存在: " + investmentId));

        FinanceProduct product = productRepository.findById(investment.getProductId())
                .orElseThrow(() -> new RuntimeException("理财产品不存在"));

        // 计算天数
        long days = java.time.Duration.between(startTime, endTime).toDays();
        if (days <= 0) {
            return BigDecimal.ZERO;
        }

        // 计算收益：本金 * 年化收益率 * 天数 / 365
        BigDecimal profit = investment.getRemainingPrincipal()
                .multiply(product.getAnnualRate())
                .multiply(BigDecimal.valueOf(days))
                .divide(BigDecimal.valueOf(365), 8, RoundingMode.HALF_UP);

        return profit;
    }
}














