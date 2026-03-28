/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;
import com.cryptotrade.futures.coin.repository.CoinFuturesPositionRepository;
import com.cryptotrade.futures.coin.service.CoinFuturesGradientService;
import com.cryptotrade.futures.coin.service.CoinFuturesPositionService;
import com.cryptotrade.futures.usdt.entity.GradientRule;
import com.cryptotrade.futures.usdt.repository.GradientRuleRepository;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoinFuturesGradientServiceImpl implements CoinFuturesGradientService {

    @Autowired
    private GradientRuleRepository gradientRuleRepository;

    @Autowired
    private CoinFuturesPositionRepository coinFuturesPositionRepository;

    @Autowired
    private CoinFuturesPositionService coinFuturesPositionService;

    @Autowired
    private WalletService walletService;

    private static final String ACCOUNT_TYPE = "FUTURES";

    @Override
    public Optional<GradientRule> getGradientRule(String pairName, BigDecimal positionQuantity) {
        return gradientRuleRepository.findApplicableRule(pairName, positionQuantity);
    }

    @Override
    public Integer calculateLeverage(CoinFuturesPosition position, BigDecimal volatilityFactor) {
        Optional<GradientRule> ruleOpt = getGradientRule(position.getPairName(), position.getQuantity());

        if (ruleOpt.isPresent()) {
            GradientRule rule = ruleOpt.get();
            // 基础杠杆 = 最大杠杆 - (波动率因子 * 某个系数)
            // 简化处理：直接使用规则中的最大杠杆，不考虑波动率因子
            // 实际应用中，波动率因子会动态调整杠杆
            return rule.getMaxLeverage();
        }
        return position.getLeverage(); // 如果没有匹配规则，返回当前杠杆
    }

    @Override
    @Transactional
    public CoinFuturesPosition adjustLeverage(Long userId, Long positionId, Integer newLeverage) {
        CoinFuturesPosition position = coinFuturesPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        if (!position.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此仓位");
        }

        // 验证新杠杆是否在允许范围内
        if (newLeverage <= 0) {
            throw new RuntimeException("杠杆倍数必须大于0");
        }

        // 获取基础资产
        String baseCurrency = position.getPairName().split("/")[0];

        // 计算新的保证金
        BigDecimal oldLeverage = new BigDecimal(position.getLeverage());
        BigDecimal newLeverageBd = new BigDecimal(newLeverage);

        BigDecimal currentPositionValue = position.getQuantity().multiply(
                position.getMarkPrice() != null ? position.getMarkPrice() : position.getEntryPrice());

        BigDecimal oldMargin = currentPositionValue.divide(oldLeverage, 8, RoundingMode.UP);
        BigDecimal newMargin = currentPositionValue.divide(newLeverageBd, 8, RoundingMode.UP);

        // 调整保证金
        if (newMargin.compareTo(oldMargin) > 0) {
            // 需要增加保证金
            BigDecimal marginIncrease = newMargin.subtract(oldMargin);
            if (!walletService.checkBalance(userId, ACCOUNT_TYPE, baseCurrency, marginIncrease)) {
                throw new RuntimeException("可用余额不足以增加保证金");
            }
            walletService.freezeBalance(userId, ACCOUNT_TYPE, baseCurrency, marginIncrease);
            position.setMargin(position.getMargin().add(marginIncrease));
        } else if (newMargin.compareTo(oldMargin) < 0) {
            // 可以释放保证金
            BigDecimal marginDecrease = oldMargin.subtract(newMargin);
            walletService.unfreezeBalance(userId, ACCOUNT_TYPE, baseCurrency, marginDecrease);
            position.setMargin(position.getMargin().subtract(marginDecrease));
        }

        position.setLeverage(newLeverage);
        // 重新计算强平价格
        position.setLiquidationPrice(coinFuturesPositionService.calculateLiquidationPrice(position));

        return coinFuturesPositionRepository.save(position);
    }

    @Override
    @Transactional
    public void autoAdjustLeverage() {
        // 遍历所有OPEN状态的仓位
        List<CoinFuturesPosition> openPositions = coinFuturesPositionRepository.findAll()
                .stream()
                .filter(p -> "OPEN".equals(p.getStatus()))
                .collect(Collectors.toList());

        for (CoinFuturesPosition position : openPositions) {
            try {
                // 简化处理：假设波动率因子为1
                Integer newLeverage = calculateLeverage(position, BigDecimal.ONE);
                if (!newLeverage.equals(position.getLeverage())) {
                    adjustLeverage(position.getUserId(), position.getId(), newLeverage);
                    System.out.println("自动调整仓位 " + position.getId() + " 杠杆至 " + newLeverage);
                }
            } catch (Exception e) {
                System.err.println("自动调整杠杆失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }
}















