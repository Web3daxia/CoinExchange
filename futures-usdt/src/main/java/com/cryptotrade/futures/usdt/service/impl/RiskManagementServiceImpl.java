/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.futures.usdt.entity.FuturesOrder;
import com.cryptotrade.futures.usdt.entity.FuturesPosition;
import com.cryptotrade.futures.usdt.entity.LiquidationRecord;
import com.cryptotrade.futures.usdt.repository.FuturesOrderRepository;
import com.cryptotrade.futures.usdt.repository.FuturesPositionRepository;
import com.cryptotrade.futures.usdt.repository.LiquidationRecordRepository;
import com.cryptotrade.futures.usdt.service.PositionService;
import com.cryptotrade.futures.usdt.service.RiskManagementService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiskManagementServiceImpl implements RiskManagementService {

    @Autowired
    private FuturesPositionRepository futuresPositionRepository;

    @Autowired
    private FuturesOrderRepository futuresOrderRepository;

    @Autowired
    private LiquidationRecordRepository liquidationRecordRepository;

    @Autowired
    private PositionService positionService;

    @Autowired
    private WalletService walletService;

    private static final String ACCOUNT_TYPE = "FUTURES";
    private static final String SETTLEMENT_CURRENCY = "USDT";
    private static final BigDecimal MAINTENANCE_MARGIN_RATE = new BigDecimal("0.005"); // 维持保证金率 0.5%
    private static final BigDecimal MARGIN_CALL_RATE = new BigDecimal("0.01"); // 追加保证金警告率 1%

    @Override
    public boolean checkLiquidation(FuturesPosition position) {
        if (!"OPEN".equals(position.getStatus())) {
            return false;
        }

        // 更新标记价格和保证金率
        positionService.updateMarkPrice(position.getId());
        position = futuresPositionRepository.findById(position.getId())
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        // 计算保证金率
        BigDecimal marginRatio = positionService.calculateMarginRatio(
                position, position.getMarkPrice() != null ? position.getMarkPrice() : position.getEntryPrice());

        // 如果保证金率 < 维持保证金率，需要强平
        return marginRatio.compareTo(MAINTENANCE_MARGIN_RATE) < 0;
    }

    @Override
    @Transactional
    public void executeLiquidation(Long positionId) {
        FuturesPosition position = futuresPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("仓位不存在"));

        if (!"OPEN".equals(position.getStatus())) {
            throw new RuntimeException("仓位不是OPEN状态，无法强平");
        }

        // 1. 取消所有相关订单
        List<FuturesOrder> pendingOrders = futuresOrderRepository
                .findByUserIdAndStatus(position.getUserId(), "PENDING")
                .stream()
                .filter(order -> order.getPairName().equals(position.getPairName()) &&
                        order.getPositionSide().equals(position.getPositionSide()))
                .collect(Collectors.toList());

        for (FuturesOrder order : pendingOrders) {
            try {
                // 解冻保证金
                BigDecimal frozenMargin = order.getMargin();
                walletService.unfreezeBalance(order.getUserId(), ACCOUNT_TYPE, SETTLEMENT_CURRENCY, frozenMargin);
                order.setStatus("CANCELLED");
                futuresOrderRepository.save(order);
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
        BigDecimal unrealizedPnl = positionService.calculateUnrealizedPnl(position, liquidationPrice);

        // 4. 计算亏损（保证金 + 未实现盈亏，如果为负数则是亏损）
        BigDecimal margin = position.getMargin();
        BigDecimal totalValue = margin.add(unrealizedPnl);
        BigDecimal loss = totalValue.compareTo(BigDecimal.ZERO) < 0 ? totalValue.abs() : BigDecimal.ZERO;

        // 5. 扣除保证金（如果还有剩余）
        if (margin.compareTo(BigDecimal.ZERO) > 0) {
            walletService.deductBalance(position.getUserId(), ACCOUNT_TYPE, SETTLEMENT_CURRENCY, margin);
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
        futuresPositionRepository.save(position);
    }

    @Override
    @Transactional
    public void monitorPositions() {
        // 查询所有OPEN状态的仓位
        List<FuturesPosition> openPositions = futuresPositionRepository.findAll()
                .stream()
                .filter(p -> "OPEN".equals(p.getStatus()))
                .collect(Collectors.toList());

        for (FuturesPosition position : openPositions) {
            try {
                // 更新标记价格
                positionService.updateMarkPrice(position.getId());
                
                // 重新查询以获取最新数据
                position = futuresPositionRepository.findById(position.getId())
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
    public boolean checkMarginCall(FuturesPosition position) {
        if (!"OPEN".equals(position.getStatus())) {
            return false;
        }

        // 计算保证金率
        BigDecimal currentPrice = position.getMarkPrice() != null ? 
                position.getMarkPrice() : position.getEntryPrice();
        BigDecimal marginRatio = positionService.calculateMarginRatio(position, currentPrice);

        // 如果保证金率 < 追加保证金警告率，需要警告
        return marginRatio.compareTo(MARGIN_CALL_RATE) < 0;
    }

    @Override
    public List<FuturesPosition> getHighRiskPositions() {
        // 查询所有OPEN状态的仓位
        List<FuturesPosition> openPositions = futuresPositionRepository.findAll()
                .stream()
                .filter(p -> "OPEN".equals(p.getStatus()))
                .collect(Collectors.toList());

        return openPositions.stream()
                .filter(this::checkMarginCall)
                .collect(Collectors.toList());
    }
}

