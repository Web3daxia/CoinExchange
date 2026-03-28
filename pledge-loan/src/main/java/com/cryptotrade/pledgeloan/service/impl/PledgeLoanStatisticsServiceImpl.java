/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import com.cryptotrade.pledgeloan.repository.PledgeLoanLiquidationRepository;
import com.cryptotrade.pledgeloan.repository.PledgeLoanOrderRepository;
import com.cryptotrade.pledgeloan.repository.PledgeLoanRepaymentRepository;
import com.cryptotrade.pledgeloan.repository.PledgeLoanRiskRecordRepository;
import com.cryptotrade.pledgeloan.service.PledgeLoanStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 质押借币统计Service实现
 */
@Service
public class PledgeLoanStatisticsServiceImpl implements PledgeLoanStatisticsService {

    @Autowired
    private PledgeLoanOrderRepository orderRepository;

    @Autowired
    private PledgeLoanRepaymentRepository repaymentRepository;

    @Autowired
    private PledgeLoanRiskRecordRepository riskRecordRepository;

    @Autowired
    private PledgeLoanLiquidationRepository liquidationRepository;

    @Override
    public Map<String, Object> getUserPledgeStatistics(Long userId) {
        Map<String, Object> statistics = new HashMap<>();

        // 获取用户的所有订单
        List<PledgeLoanOrder> userOrders = orderRepository.findByUserId(userId);

        // 统计质押总金额（USDT）
        BigDecimal totalPledgeValue = userOrders.stream()
                .map(PledgeLoanOrder::getPledgeValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 统计借款总金额（USDT）
        BigDecimal totalLoanValue = userOrders.stream()
                .filter(o -> "ACTIVE".equals(o.getStatus()) || "REPAID".equals(o.getStatus()))
                .map(PledgeLoanOrder::getLoanValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 统计已付利息（USDT）
        BigDecimal totalPaidInterest = userOrders.stream()
                .map(PledgeLoanOrder::getPaidInterest)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 统计订单数量
        long totalOrders = userOrders.size();
        long activeOrders = userOrders.stream().filter(o -> "ACTIVE".equals(o.getStatus())).count();
        long repaidOrders = userOrders.stream().filter(o -> "REPAID".equals(o.getStatus())).count();
        long liquidatedOrders = userOrders.stream().filter(o -> "LIQUIDATED".equals(o.getStatus())).count();

        statistics.put("totalPledgeValue", totalPledgeValue);
        statistics.put("totalLoanValue", totalLoanValue);
        statistics.put("totalPaidInterest", totalPaidInterest);
        statistics.put("totalOrders", totalOrders);
        statistics.put("activeOrders", activeOrders);
        statistics.put("repaidOrders", repaidOrders);
        statistics.put("liquidatedOrders", liquidatedOrders);

        return statistics;
    }

    @Override
    public Map<String, Object> getPlatformPledgeStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        List<PledgeLoanOrder> allOrders = orderRepository.findAll();

        // 统计质押总金额
        BigDecimal totalPledgeValue = allOrders.stream()
                .map(PledgeLoanOrder::getPledgeValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 统计借款总金额
        BigDecimal totalLoanValue = allOrders.stream()
                .filter(o -> "ACTIVE".equals(o.getStatus()) || "REPAID".equals(o.getStatus()))
                .map(PledgeLoanOrder::getLoanValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 统计总利息收入
        BigDecimal totalInterestIncome = allOrders.stream()
                .map(PledgeLoanOrder::getPaidInterest)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 统计订单数量
        long totalOrders = allOrders.size();
        long activeOrders = allOrders.stream().filter(o -> "ACTIVE".equals(o.getStatus())).count();
        long pendingOrders = allOrders.stream().filter(o -> "PENDING".equals(o.getStatus())).count();

        statistics.put("totalPledgeValue", totalPledgeValue);
        statistics.put("totalLoanValue", totalLoanValue);
        statistics.put("totalInterestIncome", totalInterestIncome);
        statistics.put("totalOrders", totalOrders);
        statistics.put("activeOrders", activeOrders);
        statistics.put("pendingOrders", pendingOrders);

        return statistics;
    }

    @Override
    public Map<String, Object> getRiskStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 统计风险记录数量
        long totalRiskRecords = riskRecordRepository.count();
        long unprocessedRiskRecords = riskRecordRepository.findByIsProcessedFalse().size();
        long criticalRiskRecords = riskRecordRepository.findByRiskLevel("CRITICAL").size();
        long highRiskRecords = riskRecordRepository.findByRiskLevel("HIGH").size();
        long mediumRiskRecords = riskRecordRepository.findByRiskLevel("MEDIUM").size();
        long lowRiskRecords = riskRecordRepository.findByRiskLevel("LOW").size();

        statistics.put("totalRiskRecords", totalRiskRecords);
        statistics.put("unprocessedRiskRecords", unprocessedRiskRecords);
        statistics.put("criticalRiskRecords", criticalRiskRecords);
        statistics.put("highRiskRecords", highRiskRecords);
        statistics.put("mediumRiskRecords", mediumRiskRecords);
        statistics.put("lowRiskRecords", lowRiskRecords);

        return statistics;
    }

    @Override
    public Map<String, Object> getInterestIncomeStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> statistics = new HashMap<>();

        // TODO: 根据时间范围查询还款记录，统计利息收入
        // 这里简化处理
        List<PledgeLoanOrder> orders = orderRepository.findAll();
        BigDecimal totalInterest = orders.stream()
                .filter(o -> o.getStartTime() != null && 
                        o.getStartTime().isAfter(startTime) && 
                        o.getStartTime().isBefore(endTime))
                .map(PledgeLoanOrder::getPaidInterest)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        statistics.put("totalInterestIncome", totalInterest);
        statistics.put("startTime", startTime);
        statistics.put("endTime", endTime);

        return statistics;
    }

    @Override
    public Map<String, Object> getLiquidationStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> statistics = new HashMap<>();

        // TODO: 根据时间范围查询平仓记录
        // 这里简化处理
        long totalLiquidations = liquidationRepository.count();
        long autoLiquidations = liquidationRepository.findByLiquidationType("AUTO").size();
        long manualLiquidations = liquidationRepository.findByLiquidationType("MANUAL").size();

        statistics.put("totalLiquidations", totalLiquidations);
        statistics.put("autoLiquidations", autoLiquidations);
        statistics.put("manualLiquidations", manualLiquidations);
        statistics.put("startTime", startTime);
        statistics.put("endTime", endTime);

        return statistics;
    }
}














