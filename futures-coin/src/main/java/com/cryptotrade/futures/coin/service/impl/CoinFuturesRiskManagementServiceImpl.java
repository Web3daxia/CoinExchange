/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.entity.CoinFuturesOrder;
import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;
import com.cryptotrade.futures.coin.repository.CoinFuturesOrderRepository;
import com.cryptotrade.futures.coin.repository.CoinFuturesPositionRepository;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesAccountRiskResponse;
import com.cryptotrade.futures.coin.dto.response.CoinFuturesPositionResponse;
import com.cryptotrade.futures.coin.service.CoinFuturesPositionService;
import com.cryptotrade.futures.coin.service.CoinFuturesRiskManagementService;
import com.cryptotrade.futures.usdt.entity.LiquidationRecord;
import com.cryptotrade.futures.usdt.repository.LiquidationRecordRepository;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinFuturesRiskManagementServiceImpl implements CoinFuturesRiskManagementService {

    @Autowired
    private CoinFuturesPositionRepository coinFuturesPositionRepository;

    @Autowired
    private CoinFuturesOrderRepository coinFuturesOrderRepository;

    @Autowired
    private LiquidationRecordRepository liquidationRecordRepository;

    @Autowired
    private CoinFuturesPositionService coinFuturesPositionService;

    @Autowired
    private WalletService walletService;

    private static final String ACCOUNT_TYPE = "FUTURES";
    private static final BigDecimal MAINTENANCE_MARGIN_RATE = new BigDecimal("0.005"); // 维持保证金率 0.5%
    private static final BigDecimal MARGIN_CALL_RATE = new BigDecimal("0.01"); // 追加保证金警告率 1%

    @Override
    public boolean checkLiquidation(CoinFuturesPosition position) {
        if (!"OPEN".equals(position.getStatus())) {
            return false;
        }

        // 更新标记价格和保证金率
        coinFuturesPositionService.updateMarkPrice(position.getId());
        position = coinFuturesPositionRepository.findById(position.getId())
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        // 计算保证金率
        BigDecimal marginRatio = coinFuturesPositionService.calculateMarginRatio(
                position, position.getMarkPrice() != null ? position.getMarkPrice() : position.getEntryPrice());

        // 如果保证金率 < 维持保证金率，需要强平
        return marginRatio.compareTo(MAINTENANCE_MARGIN_RATE) < 0;
    }

    @Override
    @Transactional
    public void executeLiquidation(Long positionId) {
        CoinFuturesPosition position = coinFuturesPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        if (!"OPEN".equals(position.getStatus())) {
            throw new RuntimeException("仓位不是OPEN状态，无法强平");
        }

        // 获取基础资产
        String baseCurrency = position.getPairName().split("/")[0];

        // 1. 取消所有相关订单
        List<CoinFuturesOrder> pendingOrders = coinFuturesOrderRepository
                .findByUserIdAndStatus(position.getUserId(), "PENDING")
                .stream()
                .filter(order -> order.getPairName().equals(position.getPairName()) &&
                        order.getPositionSide().equals(position.getPositionSide()))
                .collect(Collectors.toList());

        for (CoinFuturesOrder order : pendingOrders) {
            try {
                // 解冻保证金
                BigDecimal frozenMargin = order.getMargin();
                walletService.unfreezeBalance(order.getUserId(), ACCOUNT_TYPE, baseCurrency, frozenMargin);
                order.setStatus("CANCELLED");
                coinFuturesOrderRepository.save(order);
            } catch (Exception e) {
                System.err.println("取消订单失败: " + order.getId() + ", " + e.getMessage());
            }
        }

        // 2. 按标记价格强制平仓
        BigDecimal liquidationPrice = position.getMarkPrice();
        if (liquidationPrice == null) {
            liquidationPrice = position.getEntryPrice(); // 如果没有标记价格，使用开仓价格
        }

        // 3. 计算未实现盈亏
        BigDecimal unrealizedPnl = coinFuturesPositionService.calculateUnrealizedPnl(position, liquidationPrice);

        // 4. 计算亏损（保证金 + 未实现盈亏，如果为负数则是亏损）
        BigDecimal margin = position.getMargin();
        BigDecimal totalValue = margin.add(unrealizedPnl);
        BigDecimal loss = totalValue.compareTo(BigDecimal.ZERO) < 0 ? totalValue.abs() : BigDecimal.ZERO;

        // 5. 扣除保证金（如果还有剩余，币本位使用基础资产）
        if (margin.compareTo(BigDecimal.ZERO) > 0) {
            walletService.deductBalance(position.getUserId(), ACCOUNT_TYPE, baseCurrency, margin);
        }

        // 6. 记录清算记录
        LiquidationRecord record = new LiquidationRecord();
        record.setUserId(position.getUserId());
        record.setPositionId(position.getId());
        record.setPairName(position.getPairName());
        record.setLiquidationPrice(liquidationPrice);
        record.setLiquidationQuantity(position.getQuantity());
        record.setLiquidationType("AUTO");
        record.setLossAmount(loss);
        record.setMarginUsed(margin);
        record.setReason("保证金率低于维持保证金率，自动强平");
        record.setLiquidationAt(LocalDateTime.now());
        liquidationRecordRepository.save(record);

        // 7. 更新仓位状态
        position.setStatus("LIQUIDATED");
        position.setQuantity(BigDecimal.ZERO);
        position.setMargin(BigDecimal.ZERO);
        position.setUnrealizedPnl(BigDecimal.ZERO);
        position.setRealizedPnl(position.getRealizedPnl().add(unrealizedPnl).subtract(loss));
        coinFuturesPositionRepository.save(position);
    }

    @Override
    @Transactional
    public void monitorPositions() {
        // 查询所有OPEN状态的仓位
        List<CoinFuturesPosition> openPositions = coinFuturesPositionRepository.findAll()
                .stream()
                .filter(p -> "OPEN".equals(p.getStatus()))
                .collect(Collectors.toList());

        for (CoinFuturesPosition position : openPositions) {
            try {
                // 更新标记价格
                coinFuturesPositionService.updateMarkPrice(position.getId());
                
                // 重新查询以获取最新数据
                position = coinFuturesPositionRepository.findById(position.getId())
                        .orElseThrow(() -> new RuntimeException("仓位不存在"));

                // 检查是否需要强平
                if (checkLiquidation(position)) {
                    executeLiquidation(position.getId());
                    continue; // 已强平，跳过后续检查
                }

                // 检查是否需要追加保证金警告
                if (checkMarginCall(position)) {
                    // 可以在这里发送警告通知（邮件、短信、站内信等）
                    System.out.println("仓位 " + position.getId() + " 需要追加保证金");
                }
            } catch (Exception e) {
                // 记录错误，继续处理下一个仓位
                System.err.println("监控仓位风险失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    public boolean checkMarginCall(CoinFuturesPosition position) {
        if (!"OPEN".equals(position.getStatus())) {
            return false;
        }

        // 计算保证金率
        BigDecimal currentPrice = position.getMarkPrice() != null ? 
                position.getMarkPrice() : position.getEntryPrice();
        BigDecimal marginRatio = coinFuturesPositionService.calculateMarginRatio(position, currentPrice);

        // 如果保证金率 < 追加保证金警告率，需要警告
        return marginRatio.compareTo(MARGIN_CALL_RATE) < 0;
    }

    @Override
    public List<CoinFuturesPosition> getHighRiskPositions() {
        // 查询所有OPEN状态的仓位
        List<CoinFuturesPosition> openPositions = coinFuturesPositionRepository.findAll()
                .stream()
                .filter(p -> "OPEN".equals(p.getStatus()))
                .collect(Collectors.toList());

        return openPositions.stream()
                .filter(this::checkMarginCall)
                .collect(Collectors.toList());
    }

    @Override
    public CoinFuturesAccountRiskResponse getAccountRisk(Long userId) {
        CoinFuturesAccountRiskResponse response = new CoinFuturesAccountRiskResponse();

        // 获取所有OPEN状态的仓位
        List<CoinFuturesPosition> positions = coinFuturesPositionRepository.findByUserIdAndStatus(userId, "OPEN");

        // 获取基础资产（简化处理，假设所有仓位使用相同的基础资产）
        String baseCurrency = positions.isEmpty() ? "BTC" : positions.get(0).getPairName().split("/")[0];

        // 计算账户总资产
        BigDecimal availableBalance = walletService.getAvailableBalance(userId, ACCOUNT_TYPE, baseCurrency);
        BigDecimal usedMargin = positions.stream()
                .map(CoinFuturesPosition::getMargin)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAssets = availableBalance.add(usedMargin);

        // 计算未实现盈亏
        BigDecimal unrealizedPnl = positions.stream()
                .map(p -> p.getUnrealizedPnl() != null ? p.getUnrealizedPnl() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 账户权益 = 总资产 + 未实现盈亏
        BigDecimal accountEquity = totalAssets.add(unrealizedPnl);

        // 计算维持保证金
        BigDecimal maintenanceMargin = positions.stream()
                .map(p -> p.getMaintenanceMargin() != null ? p.getMaintenanceMargin() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 计算持仓价值
        BigDecimal positionValue = positions.stream()
                .map(p -> {
                    BigDecimal price = p.getMarkPrice() != null ? p.getMarkPrice() : p.getEntryPrice();
                    return p.getQuantity().multiply(price);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 计算保证金率
        BigDecimal marginRatio = BigDecimal.ZERO;
        if (positionValue.compareTo(BigDecimal.ZERO) > 0) {
            marginRatio = accountEquity.divide(positionValue, 4, java.math.RoundingMode.HALF_UP);
        }

        // 判断风险等级
        String riskLevel = "LOW";
        if (marginRatio.compareTo(MAINTENANCE_MARGIN_RATE) < 0) {
            riskLevel = "CRITICAL";
        } else if (marginRatio.compareTo(MARGIN_CALL_RATE) < 0) {
            riskLevel = "HIGH";
        } else if (marginRatio.compareTo(new BigDecimal("0.05")) < 0) {
            riskLevel = "MEDIUM";
        }

        // 获取高风险仓位
        List<CoinFuturesPosition> highRiskPositions = getHighRiskPositions().stream()
                .filter(p -> p.getUserId().equals(userId))
                .collect(Collectors.toList());

        // 转换为响应DTO
        List<CoinFuturesPositionResponse> highRiskPositionResponses = highRiskPositions.stream()
                .map(p -> {
                    CoinFuturesPositionResponse posResponse = new CoinFuturesPositionResponse();
                    posResponse.setId(p.getId());
                    posResponse.setPairName(p.getPairName());
                    posResponse.setPositionSide(p.getPositionSide());
                    posResponse.setMarginRatio(p.getMarginRatio());
                    posResponse.setUnrealizedPnl(p.getUnrealizedPnl());
                    return posResponse;
                })
                .collect(Collectors.toList());

        response.setTotalAssets(totalAssets);
        response.setAvailableBalance(availableBalance);
        response.setUsedMargin(usedMargin);
        response.setUnrealizedPnl(unrealizedPnl);
        response.setAccountEquity(accountEquity);
        response.setMaintenanceMargin(maintenanceMargin);
        response.setMarginRatio(marginRatio);
        response.setRiskLevel(riskLevel);
        response.setHighRiskPositions(highRiskPositionResponses);
        response.setNeedMarginCall(marginRatio.compareTo(MARGIN_CALL_RATE) < 0);
        response.setNearLiquidation(marginRatio.compareTo(MAINTENANCE_MARGIN_RATE) < 0);

        return response;
    }
}

