/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.schedule;

import com.cryptotrade.options.entity.OptionContract;
import com.cryptotrade.options.entity.OptionPosition;
import com.cryptotrade.options.repository.OptionContractRepository;
import com.cryptotrade.options.repository.OptionPositionRepository;
import com.cryptotrade.options.service.OptionExerciseService;
import com.cryptotrade.options.service.OptionPricingService;
import com.cryptotrade.options.service.OptionRiskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 期权交易定时任务
 */
@Component
public class OptionsScheduleTask {

    @Autowired
    private OptionContractRepository optionContractRepository;

    @Autowired
    private OptionPositionRepository optionPositionRepository;

    @Autowired
    private OptionExerciseService optionExerciseService;

    @Autowired
    private OptionPricingService optionPricingService;

    @Autowired
    private OptionRiskManagementService optionRiskManagementService;

    /**
     * 检查并更新到期期权合约状态
     * 每5分钟执行一次
     */
    @Scheduled(fixedRate = 300000)
    public void checkExpiredContracts() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<OptionContract> expiredContracts = optionContractRepository
                    .findByExpiryDateBeforeAndStatus(now, "ACTIVE");

            for (OptionContract contract : expiredContracts) {
                try {
                    contract.setStatus("EXPIRED");
                    optionContractRepository.save(contract);
                } catch (Exception e) {
                    System.err.println("更新到期合约状态失败: " + contract.getId() + ", " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("检查到期合约任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 自动行使到期期权
     * 每分钟执行一次（已在OptionExerciseService中实现）
     */
    @Scheduled(fixedRate = 60000)
    public void autoExerciseExpiredOptions() {
        try {
            optionExerciseService.checkAndAutoExerciseExpiredOptions();
        } catch (Exception e) {
            System.err.println("自动行使期权任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 更新期权价格和持仓盈亏
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void updateOptionPrices() {
        try {
            List<OptionContract> activeContracts = optionContractRepository.findByStatus("ACTIVE");

            for (OptionContract contract : activeContracts) {
                try {
                    // TODO: 从市场数据服务获取最新价格
                    // 这里需要实现市场数据同步逻辑

                    // 更新理论价格
                    if (contract.getUnderlyingPrice() != null && contract.getStrikePrice() != null) {
                        double timeToExpiry = calculateTimeToExpiry(contract.getExpiryDate());
                        if (timeToExpiry > 0) {
                            double riskFreeRate = 0.05;
                            double volatility = contract.getImpliedVolatility() != null ?
                                    contract.getImpliedVolatility().doubleValue() : 0.5;

                            java.math.BigDecimal theoreticalPrice;
                            if ("EUROPEAN".equals(contract.getExerciseType())) {
                                theoreticalPrice = optionPricingService.calculateBlackScholesPrice(
                                        contract.getUnderlyingPrice(),
                                        contract.getStrikePrice(),
                                        timeToExpiry,
                                        riskFreeRate,
                                        volatility,
                                        contract.getOptionType()
                                );
                            } else {
                                theoreticalPrice = optionPricingService.calculateBinomialPrice(
                                        contract.getUnderlyingPrice(),
                                        contract.getStrikePrice(),
                                        timeToExpiry,
                                        riskFreeRate,
                                        volatility,
                                        contract.getOptionType(),
                                        100
                                );
                            }
                            contract.setTheoreticalPrice(theoreticalPrice);
                            optionContractRepository.save(contract);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("更新期权价格失败: " + contract.getId() + ", " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("更新期权价格任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 更新持仓盈亏
     * 每30秒执行一次
     */
    @Scheduled(fixedRate = 30000)
    public void updatePositionPnl() {
        try {
            List<OptionPosition> activePositions = optionPositionRepository.findByStatus("ACTIVE");

            for (OptionPosition position : activePositions) {
                try {
                    // 更新未实现盈亏
                    optionRiskManagementService.monitorPositions();
                } catch (Exception e) {
                    System.err.println("更新持仓盈亏失败: " + position.getId() + ", " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("更新持仓盈亏任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 检查并触发风险警报
     * 每30秒执行一次（已在OptionRiskManagementService中实现）
     */
    @Scheduled(fixedRate = 30000)
    public void checkRiskAlerts() {
        try {
            optionRiskManagementService.checkAndTriggerRiskAlerts();
        } catch (Exception e) {
            System.err.println("检查风险警报任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 检查并执行强平
     * 每分钟执行一次（已在OptionRiskManagementService中实现）
     */
    @Scheduled(fixedRate = 60000)
    public void checkLiquidation() {
        try {
            optionRiskManagementService.checkAndLiquidate();
        } catch (Exception e) {
            System.err.println("检查强平任务执行失败: " + e.getMessage());
        }
    }

    /**
     * 计算到期时间（年）
     */
    private double calculateTimeToExpiry(LocalDateTime expiryDate) {
        LocalDateTime now = LocalDateTime.now();
        if (expiryDate.isBefore(now)) {
            return 0;
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(now, expiryDate);
        return days / 365.0;
    }
}

