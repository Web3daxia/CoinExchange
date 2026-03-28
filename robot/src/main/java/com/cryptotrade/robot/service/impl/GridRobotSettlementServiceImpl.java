/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service.impl;

import com.cryptotrade.robot.entity.GridRobotSettlement;
import com.cryptotrade.robot.entity.TradingRobot;
import com.cryptotrade.robot.repository.GridRobotSettlementRepository;
import com.cryptotrade.robot.repository.TradingRobotRepository;
import com.cryptotrade.robot.service.GridRobotSettlementService;
import com.cryptotrade.wallet.dto.request.AssetTransferRequest;
import com.cryptotrade.wallet.service.AssetTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 网格机器人结算服务实现类
 */
@Service
public class GridRobotSettlementServiceImpl implements GridRobotSettlementService {

    @Autowired
    private GridRobotSettlementRepository settlementRepository;

    @Autowired
    private TradingRobotRepository robotRepository;

    @Autowired(required = false)
    private AssetTransferService assetTransferService;

    @Override
    @Transactional
    public GridRobotSettlement settleRobot(Long userId, Long robotId) {
        Optional<TradingRobot> robotOpt = robotRepository.findById(robotId);
        TradingRobot robot = robotOpt.orElseThrow(() -> new RuntimeException("机器人不存在"));

        if (!robot.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此机器人");
        }

        if (!"STOPPED".equals(robot.getStatus())) {
            throw new RuntimeException("只能结算已停止的机器人");
        }

        // 生成结算ID
        String settlementId = "GS" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 计算最终盈亏
        BigDecimal totalProfit = robot.getTotalProfit() != null ? robot.getTotalProfit() : BigDecimal.ZERO;
        BigDecimal totalLoss = robot.getTotalLoss() != null ? robot.getTotalLoss() : BigDecimal.ZERO;
        BigDecimal netProfit = totalProfit.subtract(totalLoss);

        // 计算最终资金（简化处理，实际应该考虑所有持仓和订单）
        BigDecimal finalCapital = robot.getCurrentCapital() != null ? robot.getCurrentCapital() : robot.getInitialCapital();

        // 创建结算记录
        GridRobotSettlement settlement = new GridRobotSettlement();
        settlement.setSettlementId(settlementId);
        settlement.setRobotId(robotId);
        settlement.setUserId(userId);
        settlement.setInitialCapital(robot.getInitialCapital());
        settlement.setFinalCapital(finalCapital);
        settlement.setTotalProfit(totalProfit);
        settlement.setTotalLoss(totalLoss);
        settlement.setNetProfit(netProfit);
        settlement.setTotalFees(BigDecimal.ZERO); // TODO: 计算总手续费
        settlement.setSettlementAmount(finalCapital);
        settlement.setStatus("COMPLETED");
        settlement.setSettlementTime(LocalDateTime.now());
        settlement.setCompletedAt(LocalDateTime.now());

        settlement = settlementRepository.save(settlement);

        // 调用钱包服务将资金转入用户账户
        if (assetTransferService != null) {
            try {
                // 从交易对确定币种（如BTC/USDT -> USDT）
                String currency = "USDT"; // 默认USDT
                if (robot.getPairName() != null && robot.getPairName().contains("/")) {
                    String[] parts = robot.getPairName().split("/");
                    if (parts.length > 1) {
                        currency = parts[1]; // 取计价货币
                    }
                }
                
                AssetTransferRequest transferRequest = new AssetTransferRequest();
                transferRequest.setFromAccountType("ROBOT");
                transferRequest.setToAccountType("SPOT");
                transferRequest.setCurrency(currency);
                transferRequest.setAmount(finalCapital);
                transferRequest.setRemark("网格机器人结算 - " + robot.getRobotName());
                
                assetTransferService.transfer(userId, transferRequest);
            } catch (Exception e) {
                // 记录日志，但不影响结算流程
                // 如果转账失败，结算记录仍然保存，管理员可以手动处理
                // log.error("转账失败: " + e.getMessage(), e);
            }
        }

        // 更新机器人结算状态
        robot.setSettlementStatus("COMPLETED");
        robotRepository.save(robot);

        return settlement;
    }

    @Override
    public GridRobotSettlement getSettlement(String settlementId) {
        Optional<GridRobotSettlement> settlementOpt = settlementRepository.findBySettlementId(settlementId);
        return settlementOpt.orElseThrow(() -> new RuntimeException("结算记录不存在"));
    }

    @Override
    public List<GridRobotSettlement> getUserSettlements(Long userId, Long robotId) {
        if (robotId != null) {
            return settlementRepository.findByRobotId(robotId);
        }
        return settlementRepository.findByUserId(userId);
    }
}

