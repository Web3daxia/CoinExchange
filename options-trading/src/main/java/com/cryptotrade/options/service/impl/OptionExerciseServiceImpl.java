/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service.impl;

import com.cryptotrade.options.entity.OptionContract;
import com.cryptotrade.options.entity.OptionExercise;
import com.cryptotrade.options.entity.OptionPosition;
import com.cryptotrade.options.repository.OptionContractRepository;
import com.cryptotrade.options.repository.OptionExerciseRepository;
import com.cryptotrade.options.repository.OptionPositionRepository;
import com.cryptotrade.options.service.OptionExerciseService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 期权行使服务实现类
 */
@Service
public class OptionExerciseServiceImpl implements OptionExerciseService {

    @Autowired
    private OptionExerciseRepository optionExerciseRepository;

    @Autowired
    private OptionPositionRepository optionPositionRepository;

    @Autowired
    private OptionContractRepository optionContractRepository;

    @Autowired
    private WalletService walletService;

    @Override
    @Transactional
    public OptionExercise exerciseOption(Long userId, Long positionId, BigDecimal quantity) {
        OptionPosition position = optionPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("持仓不存在"));

        if (!position.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此持仓");
        }

        if (!"ACTIVE".equals(position.getStatus())) {
            throw new RuntimeException("持仓状态不允许行使");
        }

        // 检查是否到期
        if (position.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("期权已到期，无法行使");
        }

        // 检查行使数量
        if (quantity.compareTo(position.getQuantity()) > 0) {
            throw new RuntimeException("行使数量超过持仓数量");
        }

        OptionContract contract = optionContractRepository.findById(position.getContractId())
                .orElseThrow(() -> new RuntimeException("期权合约不存在"));

        // 检查是否实值
        if (!isInTheMoney(position)) {
            throw new RuntimeException("期权未处于实值状态，行使将产生亏损");
        }

        // 创建行使记录
        OptionExercise exercise = new OptionExercise();
        exercise.setUserId(userId);
        exercise.setPositionId(positionId);
        exercise.setContractId(position.getContractId());
        exercise.setOptionType(position.getOptionType());
        exercise.setExerciseType("MANUAL");
        exercise.setQuantity(quantity);
        exercise.setStrikePrice(position.getStrikePrice());
        exercise.setUnderlyingPrice(position.getUnderlyingPrice());
        exercise.setExercisePrice(position.getStrikePrice());
        exercise.setStatus("PENDING");

        // 计算盈亏
        BigDecimal profitLoss = calculateProfitLoss(position, contract);
        exercise.setProfitLoss(profitLoss);

        // 计算行使手续费
        BigDecimal exerciseFee = quantity.multiply(position.getStrikePrice()).multiply(new BigDecimal("0.001"));
        exercise.setExerciseFee(exerciseFee);

        // 执行行使
        executeExercise(exercise, position, contract);

        return optionExerciseRepository.save(exercise);
    }

    @Override
    @Transactional
    public OptionExercise autoExerciseOption(Long positionId) {
        OptionPosition position = optionPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("持仓不存在"));

        if (!"ACTIVE".equals(position.getStatus())) {
            return null;
        }

        // 检查是否到期
        if (position.getExpiryDate().isAfter(LocalDateTime.now())) {
            return null;
        }

        // 检查是否实值
        if (!isInTheMoney(position)) {
            // 未实值，标记为已到期
            position.setStatus("EXPIRED");
            optionPositionRepository.save(position);
            return null;
        }

        OptionContract contract = optionContractRepository.findById(position.getContractId())
                .orElseThrow(() -> new RuntimeException("期权合约不存在"));

        // 创建自动行使记录
        OptionExercise exercise = new OptionExercise();
        exercise.setUserId(position.getUserId());
        exercise.setPositionId(positionId);
        exercise.setContractId(position.getContractId());
        exercise.setOptionType(position.getOptionType());
        exercise.setExerciseType("AUTO");
        exercise.setQuantity(position.getQuantity());
        exercise.setStrikePrice(position.getStrikePrice());
        exercise.setUnderlyingPrice(position.getUnderlyingPrice());
        exercise.setExercisePrice(position.getStrikePrice());
        exercise.setStatus("PENDING");

        // 计算盈亏
        BigDecimal profitLoss = calculateProfitLoss(position, contract);
        exercise.setProfitLoss(profitLoss);

        // 计算行使手续费
        BigDecimal exerciseFee = position.getQuantity().multiply(position.getStrikePrice())
                .multiply(new BigDecimal("0.001"));
        exercise.setExerciseFee(exerciseFee);

        // 执行行使
        executeExercise(exercise, position, contract);

        return optionExerciseRepository.save(exercise);
    }

    @Override
    public List<OptionExercise> getExerciseStatus(Long userId, Long positionId) {
        return optionExerciseRepository.findByPositionId(positionId);
    }

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void checkAndAutoExerciseExpiredOptions() {
        LocalDateTime now = LocalDateTime.now();
        List<OptionPosition> expiredPositions = optionPositionRepository.findByStatusAndExpiryDateBefore("ACTIVE", now);

        for (OptionPosition position : expiredPositions) {
            try {
                autoExerciseOption(position.getId());
            } catch (Exception e) {
                // 记录错误，继续处理下一个
                System.err.println("自动行使期权失败: " + position.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    public boolean isInTheMoney(OptionPosition position) {
        OptionContract contract = optionContractRepository.findById(position.getContractId())
                .orElseThrow(() -> new RuntimeException("期权合约不存在"));

        BigDecimal underlyingPrice = contract.getUnderlyingPrice();
        BigDecimal strikePrice = position.getStrikePrice();

        if ("CALL".equals(position.getOptionType())) {
            // 看涨期权：标的资产价格 > 执行价格
            return underlyingPrice.compareTo(strikePrice) > 0;
        } else {
            // 看跌期权：标的资产价格 < 执行价格
            return underlyingPrice.compareTo(strikePrice) < 0;
        }
    }

    /**
     * 执行行使
     */
    private void executeExercise(OptionExercise exercise, OptionPosition position, OptionContract contract) {
        // 计算盈亏
        BigDecimal profitLoss = calculateProfitLoss(position, contract);

        // 处理资金
        if ("CALL".equals(position.getOptionType())) {
            // 看涨期权：以执行价格买入标的资产
            // 需要扣除执行价格 * 数量 + 手续费
            BigDecimal requiredAmount = position.getStrikePrice().multiply(exercise.getQuantity())
                    .add(exercise.getExerciseFee());
            walletService.deductBalance(position.getUserId(), contract.getQuoteCurrency(), "SPOT", requiredAmount);
            // 增加标的资产
            walletService.addBalance(position.getUserId(), contract.getBaseCurrency(), "SPOT", exercise.getQuantity());
        } else {
            // 看跌期权：以执行价格卖出标的资产
            // 需要扣除标的资产数量
            walletService.deductBalance(position.getUserId(), contract.getBaseCurrency(), "SPOT", exercise.getQuantity());
            // 增加执行价格 * 数量 - 手续费
            BigDecimal receivedAmount = position.getStrikePrice().multiply(exercise.getQuantity())
                    .subtract(exercise.getExerciseFee());
            walletService.addBalance(position.getUserId(), contract.getQuoteCurrency(), "SPOT", receivedAmount);
        }

        // 更新持仓
        BigDecimal remainingQuantity = position.getQuantity().subtract(exercise.getQuantity());
        if (remainingQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            position.setStatus("EXERCISED");
            position.setQuantity(BigDecimal.ZERO);
        } else {
            position.setQuantity(remainingQuantity);
        }

        position.setRealizedPnl(position.getRealizedPnl().add(profitLoss));
        optionPositionRepository.save(position);

        // 更新行使记录
        exercise.setStatus("EXECUTED");
        exercise.setExecutedAt(LocalDateTime.now());
    }

    /**
     * 计算盈亏
     */
    private BigDecimal calculateProfitLoss(OptionPosition position, OptionContract contract) {
        BigDecimal underlyingPrice = contract.getUnderlyingPrice();
        BigDecimal strikePrice = position.getStrikePrice();

        if ("CALL".equals(position.getOptionType())) {
            // 看涨期权：盈亏 = (标的资产价格 - 执行价格) * 数量 - 期权费
            BigDecimal intrinsicValue = underlyingPrice.subtract(strikePrice).max(BigDecimal.ZERO);
            BigDecimal totalIntrinsicValue = intrinsicValue.multiply(position.getQuantity());
            BigDecimal totalPremium = position.getAveragePrice().multiply(position.getQuantity());
            return totalIntrinsicValue.subtract(totalPremium);
        } else {
            // 看跌期权：盈亏 = (执行价格 - 标的资产价格) * 数量 - 期权费
            BigDecimal intrinsicValue = strikePrice.subtract(underlyingPrice).max(BigDecimal.ZERO);
            BigDecimal totalIntrinsicValue = intrinsicValue.multiply(position.getQuantity());
            BigDecimal totalPremium = position.getAveragePrice().multiply(position.getQuantity());
            return totalIntrinsicValue.subtract(totalPremium);
        }
    }
}















