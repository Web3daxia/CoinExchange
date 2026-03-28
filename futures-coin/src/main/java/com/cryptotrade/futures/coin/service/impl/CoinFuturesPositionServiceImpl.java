/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.dto.response.CoinFuturesPositionResponse;
import com.cryptotrade.futures.coin.entity.CoinFuturesContract;
import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;
import com.cryptotrade.futures.coin.repository.CoinFuturesContractRepository;
import com.cryptotrade.futures.coin.repository.CoinFuturesPositionRepository;
import com.cryptotrade.futures.coin.service.CoinFuturesPositionService;
import com.cryptotrade.futures.usdt.entity.GradientRule;
import com.cryptotrade.futures.usdt.repository.GradientRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoinFuturesPositionServiceImpl implements CoinFuturesPositionService {

    @Autowired
    private CoinFuturesPositionRepository coinFuturesPositionRepository;

    @Autowired
    private CoinFuturesContractRepository coinFuturesContractRepository;

    @Autowired
    private GradientRuleRepository gradientRuleRepository;

    private static final BigDecimal DEFAULT_MAINTENANCE_MARGIN_RATE = new BigDecimal("0.005"); // 默认维持保证金率 0.5%

    @Override
    public List<CoinFuturesPositionResponse> getPositions(Long userId) {
        List<CoinFuturesPosition> positions = coinFuturesPositionRepository.findByUserIdAndStatus(userId, "OPEN");
        return positions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CoinFuturesPositionResponse getPosition(Long userId, Long positionId) {
        CoinFuturesPosition position = coinFuturesPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        if (!position.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此仓位");
        }

        // 更新标记价格和未实现盈亏
        updateMarkPrice(positionId);

        // 重新查询以获取最新数据
        position = coinFuturesPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        return convertToResponse(position);
    }

    @Override
    public BigDecimal calculateUnrealizedPnl(CoinFuturesPosition position, BigDecimal currentPrice) {
        if (currentPrice == null || position.getEntryPrice() == null || position.getQuantity() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal priceDiff;
        if ("LONG".equals(position.getPositionSide())) {
            // 多头：Pnl = (当前价格 - 开仓价格) × 持仓数量
            priceDiff = currentPrice.subtract(position.getEntryPrice());
        } else {
            // 空头：Pnl = (开仓价格 - 当前价格) × 持仓数量
            priceDiff = position.getEntryPrice().subtract(currentPrice);
        }

        return priceDiff.multiply(position.getQuantity());
    }

    @Override
    public BigDecimal calculateMarginRatio(CoinFuturesPosition position, BigDecimal currentPrice) {
        if (currentPrice == null || position.getQuantity() == null || position.getMargin() == null) {
            return BigDecimal.ZERO;
        }

        // 持仓价值 = 持仓数量 × 当前价格
        BigDecimal positionValue = position.getQuantity().multiply(currentPrice);

        // 获取维持保证金率
        BigDecimal maintenanceMarginRate = getMaintenanceMarginRate(position);

        // 维持保证金 = 持仓价值 × 维持保证金率
        BigDecimal maintenanceMargin = positionValue.multiply(maintenanceMarginRate);

        // 保证金率 = (保证金 + 未实现盈亏) / 持仓价值
        BigDecimal unrealizedPnl = calculateUnrealizedPnl(position, currentPrice);
        BigDecimal availableMargin = position.getMargin().add(unrealizedPnl);

        if (positionValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return availableMargin.divide(positionValue, 4, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public void updateMarkPrice(Long positionId) {
        CoinFuturesPosition position = coinFuturesPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        // 获取合约信息以获取标记价格
        CoinFuturesContract contract = coinFuturesContractRepository
                .findByPairNameAndContractType(position.getPairName(), "COIN_MARGINED")
                .orElseThrow(() -> new RuntimeException("合约不存在"));

        BigDecimal markPrice = contract.getMarkPrice();
        if (markPrice == null) {
            markPrice = contract.getCurrentPrice(); // 如果没有标记价格，使用当前价格
        }

        if (markPrice == null) {
            throw new RuntimeException("无法获取标记价格");
        }

        // 更新标记价格
        position.setMarkPrice(markPrice);

        // 计算未实现盈亏
        BigDecimal unrealizedPnl = calculateUnrealizedPnl(position, markPrice);
        position.setUnrealizedPnl(unrealizedPnl);

        // 计算保证金率
        BigDecimal marginRatio = calculateMarginRatio(position, markPrice);
        position.setMarginRatio(marginRatio);

        // 计算强平价格
        BigDecimal liquidationPrice = calculateLiquidationPrice(position);
        position.setLiquidationPrice(liquidationPrice);

        coinFuturesPositionRepository.save(position);
    }

    @Override
    @Transactional
    public void updateAllMarkPrices() {
        // 查询所有OPEN状态的仓位
        List<CoinFuturesPosition> openPositions = coinFuturesPositionRepository.findAll()
                .stream()
                .filter(p -> "OPEN".equals(p.getStatus()))
                .collect(Collectors.toList());
        for (CoinFuturesPosition position : openPositions) {
            try {
                updateMarkPrice(position.getId());
            } catch (Exception e) {
                // 记录错误，继续处理下一个仓位
                System.err.println("更新仓位标记价格失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public CoinFuturesPosition updatePosition(CoinFuturesPosition position, BigDecimal quantity, BigDecimal price, boolean isOpenPosition) {
        if (isOpenPosition) {
            // 开仓或加仓
            BigDecimal oldQuantity = position.getQuantity();
            BigDecimal oldEntryPrice = position.getEntryPrice();
            BigDecimal oldMargin = position.getMargin();

            // 新持仓数量
            BigDecimal newQuantity = oldQuantity.add(quantity);

            // 新开仓价格（加权平均）
            // 新开仓价格 = (原持仓数量 × 原开仓价格 + 新增数量 × 成交价格) / 新持仓数量
            BigDecimal newEntryPrice = oldEntryPrice.multiply(oldQuantity)
                    .add(price.multiply(quantity))
                    .divide(newQuantity, 8, RoundingMode.HALF_UP);

            // 新增保证金（简化处理，实际应该根据新订单的保证金计算）
            BigDecimal additionalMargin = quantity.multiply(price).divide(
                    BigDecimal.valueOf(position.getLeverage()), 8, RoundingMode.HALF_UP);

            position.setQuantity(newQuantity);
            position.setEntryPrice(newEntryPrice);
            position.setMargin(oldMargin.add(additionalMargin));

        } else {
            // 减仓或平仓
            BigDecimal remainingQuantity = position.getQuantity().subtract(quantity);

            if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
                // 完全平仓
                position.setStatus("CLOSED");
                position.setQuantity(BigDecimal.ZERO);
            } else {
                // 部分平仓，开仓价格不变
                position.setQuantity(remainingQuantity);
                // 按比例减少保证金
                BigDecimal marginRatio = remainingQuantity.divide(
                        position.getQuantity().add(quantity), 8, RoundingMode.HALF_UP);
                position.setMargin(position.getMargin().multiply(marginRatio));
            }
        }

        // 重新计算强平价格
        BigDecimal liquidationPrice = calculateLiquidationPrice(position);
        position.setLiquidationPrice(liquidationPrice);

        return coinFuturesPositionRepository.save(position);
    }

    @Override
    public BigDecimal calculateLiquidationPrice(CoinFuturesPosition position) {
        if (position.getQuantity() == null || position.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // 获取维持保证金率
        BigDecimal maintenanceMarginRate = getMaintenanceMarginRate(position);

        if ("CROSS".equals(position.getMarginMode())) {
            // 全仓模式：强平价格需要考虑账户全部保证金
            // 简化处理：使用当前仓位的保证金
            // 强平价格 = 开仓价格 - (保证金 - 维持保证金) / 持仓数量
            BigDecimal maintenanceMargin = position.getQuantity()
                    .multiply(position.getEntryPrice())
                    .multiply(maintenanceMarginRate);

            BigDecimal marginAvailable = position.getMargin().subtract(maintenanceMargin);

            if ("LONG".equals(position.getPositionSide())) {
                // 多头：价格下跌时强平
                return position.getEntryPrice().subtract(
                        marginAvailable.divide(position.getQuantity(), 8, RoundingMode.HALF_UP));
            } else {
                // 空头：价格上涨时强平
                return position.getEntryPrice().add(
                        marginAvailable.divide(position.getQuantity(), 8, RoundingMode.HALF_UP));
            }
        } else {
            // 逐仓模式：强平价格 = (保证金 - 维持保证金) / 持仓数量
            BigDecimal maintenanceMargin = position.getQuantity()
                    .multiply(position.getEntryPrice())
                    .multiply(maintenanceMarginRate);

            BigDecimal marginAvailable = position.getMargin().subtract(maintenanceMargin);

            if ("LONG".equals(position.getPositionSide())) {
                // 多头：价格下跌时强平
                // 强平价格 = 开仓价格 - (保证金 - 维持保证金) / 持仓数量
                return position.getEntryPrice().subtract(
                        marginAvailable.divide(position.getQuantity(), 8, RoundingMode.HALF_UP));
            } else {
                // 空头：价格上涨时强平
                // 强平价格 = 开仓价格 + (保证金 - 维持保证金) / 持仓数量
                return position.getEntryPrice().add(
                        marginAvailable.divide(position.getQuantity(), 8, RoundingMode.HALF_UP));
            }
        }
    }

    /**
     * 获取维持保证金率
     */
    private BigDecimal getMaintenanceMarginRate(CoinFuturesPosition position) {
        // 尝试从梯度规则中获取（根据持仓数量匹配）
        Optional<GradientRule> ruleOpt = gradientRuleRepository.findApplicableRule(
                position.getPairName(), position.getQuantity());
        
        if (ruleOpt.isPresent() && ruleOpt.get().getMaintenanceMarginRate() != null) {
            return ruleOpt.get().getMaintenanceMarginRate();
        }
        
        // 如果没有找到，使用默认值
        return DEFAULT_MAINTENANCE_MARGIN_RATE;
    }

    /**
     * 转换为响应DTO
     */
    private CoinFuturesPositionResponse convertToResponse(CoinFuturesPosition position) {
        CoinFuturesPositionResponse response = new CoinFuturesPositionResponse();
        response.setId(position.getId());
        response.setPairName(position.getPairName());
        response.setPositionSide(position.getPositionSide());
        response.setMarginMode(position.getMarginMode());
        response.setLeverage(position.getLeverage());
        response.setQuantity(position.getQuantity());
        response.setEntryPrice(position.getEntryPrice());
        response.setMarkPrice(position.getMarkPrice());
        response.setLiquidationPrice(position.getLiquidationPrice());
        response.setMargin(position.getMargin());
        response.setUnrealizedPnl(position.getUnrealizedPnl());
        response.setRealizedPnl(position.getRealizedPnl());
        response.setFundingFee(position.getFundingFee());
        response.setMarginRatio(position.getMarginRatio());
        response.setMaintenanceMargin(position.getMaintenanceMargin());
        response.setStatus(position.getStatus());
        response.setCreatedAt(position.getCreatedAt());
        return response;
    }
}















