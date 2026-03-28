/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.futures.usdt.entity.FuturesContract;
import com.cryptotrade.futures.usdt.entity.FuturesPosition;
import com.cryptotrade.futures.usdt.entity.GradientRule;
import com.cryptotrade.futures.usdt.repository.FuturesContractRepository;
import com.cryptotrade.futures.usdt.repository.FuturesPositionRepository;
import com.cryptotrade.futures.usdt.repository.GradientRuleRepository;
import com.cryptotrade.futures.usdt.service.GradientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class GradientServiceImpl implements GradientService {

    @Autowired
    private GradientRuleRepository gradientRuleRepository;

    @Autowired
    private FuturesPositionRepository futuresPositionRepository;

    @Autowired
    private FuturesContractRepository futuresContractRepository;

    @Autowired
    private com.cryptotrade.futures.usdt.service.PositionService positionService;

    @Override
    public Optional<GradientRule> getGradientRule(String pairName, BigDecimal positionSize) {
        return gradientRuleRepository.findApplicableRule(pairName, positionSize);
    }

    @Override
    public Integer calculateLeverage(FuturesPosition position, BigDecimal volatility) {
        // 获取梯度规则
        Optional<GradientRule> ruleOpt = gradientRuleRepository.findApplicableRule(
                position.getPairName(), position.getQuantity());

        Integer maxLeverage;
        if (ruleOpt.isPresent()) {
            maxLeverage = ruleOpt.get().getMaxLeverage();
        } else {
            // 如果没有梯度规则，从合约获取最大杠杆
            Optional<FuturesContract> contractOpt = futuresContractRepository
                    .findByPairNameAndContractType(position.getPairName(), "USDT_MARGINED");
            if (contractOpt.isPresent()) {
                maxLeverage = contractOpt.get().getMaxLeverage() != null ? 
                        contractOpt.get().getMaxLeverage() : 125; // 默认125倍
            } else {
                maxLeverage = 125; // 默认值
            }
        }

        // 当前杠杆
        Integer currentLeverage = position.getLeverage();

        // 根据波动率调整杠杆
        // 波动率越大，杠杆应该越小
        // 简化算法：newLeverage = maxLeverage * (1 - volatility)
        // 但也要考虑当前杠杆，不能调整太激进
        if (volatility == null) {
            volatility = BigDecimal.ZERO; // 默认无波动
        }

        // 波动率调整因子：波动率越高，杠杆越低
        BigDecimal adjustmentFactor = BigDecimal.ONE.subtract(volatility.min(new BigDecimal("0.5"))); // 波动率最多影响50%

        // 计算调整后的杠杆 = 最大杠杆 × 调整因子
        BigDecimal adjustedLeverage = new BigDecimal(maxLeverage).multiply(adjustmentFactor);

        // 四舍五入取整
        int newLeverage = adjustedLeverage.setScale(0, RoundingMode.HALF_UP).intValue();

        // 确保在合理范围内（最小1倍，不超过最大杠杆）
        newLeverage = Math.max(1, Math.min(newLeverage, maxLeverage));

        // 如果新杠杆与当前杠杆差异小于5倍，不调整（避免频繁调整）
        if (Math.abs(newLeverage - currentLeverage) < 5) {
            return currentLeverage;
        }

        return newLeverage;
    }

    @Override
    @Transactional
    public void adjustLeverage(Long positionId, Integer newLeverage) {
        FuturesPosition position = futuresPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        // 获取合约信息以验证杠杆范围
        FuturesContract contract = futuresContractRepository
                .findByPairNameAndContractType(position.getPairName(), "USDT_MARGINED")
                .orElseThrow(() -> new RuntimeException("合约不存在"));

        // 验证杠杆是否在允许范围内
        Integer minLeverage = contract.getMinLeverage() != null ? contract.getMinLeverage() : 1;
        Integer maxLeverage = contract.getMaxLeverage() != null ? contract.getMaxLeverage() : 125;

        if (newLeverage < minLeverage || newLeverage > maxLeverage) {
            throw new RuntimeException("杠杆倍数不在允许范围内，最小: " + minLeverage + 
                    ", 最大: " + maxLeverage);
        }

        // 如果新杠杆和当前杠杆相同，不需要调整
        if (newLeverage.equals(position.getLeverage())) {
            return;
        }

        // 重新计算保证金
        // 持仓价值 = 持仓数量 × 当前价格（使用标记价格）
        BigDecimal positionValue;
        if (position.getMarkPrice() != null) {
            positionValue = position.getQuantity().multiply(position.getMarkPrice());
        } else {
            positionValue = position.getQuantity().multiply(position.getEntryPrice());
        }

        // 新保证金 = 持仓价值 / 新杠杆
        BigDecimal newMargin = positionValue.divide(
                new BigDecimal(newLeverage), 8, RoundingMode.HALF_UP);

        // 更新杠杆和保证金
        position.setLeverage(newLeverage);
        position.setMargin(newMargin);

        // 重新计算强平价格（通过PositionService）
        BigDecimal liquidationPrice = positionService.calculateLiquidationPrice(position);
        position.setLiquidationPrice(liquidationPrice);

        futuresPositionRepository.save(position);
    }

    @Override
    @Transactional
    public void autoAdjustLeverage() {
        // 查询所有OPEN状态的仓位
        List<FuturesPosition> openPositions = futuresPositionRepository.findAll()
                .stream()
                .filter(p -> "OPEN".equals(p.getStatus()))
                .collect(java.util.stream.Collectors.toList());

        for (FuturesPosition position : openPositions) {
            try {
                // 计算市场波动率（简化处理：可以从合约的24小时涨跌幅获取）
                BigDecimal volatility = calculateVolatility(position);
                
                // 计算新杠杆
                Integer newLeverage = calculateLeverage(position, volatility);
                
                // 如果新杠杆与当前杠杆不同，进行调整
                if (!newLeverage.equals(position.getLeverage())) {
                    adjustLeverage(position.getId(), newLeverage);
                }
            } catch (Exception e) {
                // 记录错误，继续处理下一个仓位
                System.err.println("自动调整杠杆失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    /**
     * 计算市场波动率（简化实现）
     */
    private BigDecimal calculateVolatility(FuturesPosition position) {
        // 从合约获取24小时涨跌幅作为波动率指标
        Optional<FuturesContract> contractOpt = futuresContractRepository
                .findByPairNameAndContractType(position.getPairName(), "USDT_MARGINED");
        
        if (contractOpt.isPresent() && contractOpt.get().getPriceChange24h() != null) {
            // 将涨跌幅转换为0-1之间的波动率
            // 例如：5%涨跌幅 = 0.05波动率
            BigDecimal priceChange = contractOpt.get().getPriceChange24h().abs();
            return priceChange.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)
                    .min(new BigDecimal("0.5")); // 最大波动率限制为0.5
        }
        
        return BigDecimal.ZERO; // 默认无波动
    }
}















