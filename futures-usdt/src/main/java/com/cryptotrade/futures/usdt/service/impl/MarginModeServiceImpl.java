/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.futures.usdt.dto.request.CreateSegmentRequest;
import com.cryptotrade.futures.usdt.dto.request.SetMarginModeRequest;
import com.cryptotrade.futures.usdt.dto.request.UpdateSegmentRequest;
import com.cryptotrade.futures.usdt.entity.MarginMode;
import com.cryptotrade.futures.usdt.entity.MarginSegment;
import com.cryptotrade.futures.usdt.repository.FuturesPositionRepository;
import com.cryptotrade.futures.usdt.repository.MarginModeRepository;
import com.cryptotrade.futures.usdt.repository.MarginSegmentRepository;
import com.cryptotrade.futures.usdt.service.MarginModeService;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MarginModeServiceImpl implements MarginModeService {

    @Autowired
    private MarginModeRepository marginModeRepository;

    @Autowired
    private MarginSegmentRepository marginSegmentRepository;

    @Autowired
    private FuturesPositionRepository futuresPositionRepository;

    @Autowired
    private WalletService walletService;

    private static final String ACCOUNT_TYPE = "FUTURES"; // 永续合约账户类型
    private static final String SETTLEMENT_CURRENCY = "USDT"; // USDT本位合约结算货币

    @Override
    @Transactional
    public MarginMode setMarginMode(Long userId, SetMarginModeRequest request) {
        // 检查当前是否有持仓（如果有持仓，不能切换模式）
        List<com.cryptotrade.futures.usdt.entity.FuturesPosition> openPositions = 
                futuresPositionRepository.findByUserIdAndStatus(userId, "OPEN");
        
        if (!openPositions.isEmpty()) {
            throw new RuntimeException("当前有持仓，无法切换保证金模式，请先平仓");
        }

        // 查找或创建保证金模式配置
        Optional<MarginMode> existingModeOpt = marginModeRepository.findByUserId(userId);
        MarginMode marginMode;
        
        if (existingModeOpt.isPresent()) {
            marginMode = existingModeOpt.get();
        } else {
            marginMode = new MarginMode();
            marginMode.setUserId(userId);
        }

        marginMode.setMarginMode(request.getMarginMode());
        marginMode.setDefaultLeverage(request.getDefaultLeverage());

        return marginModeRepository.save(marginMode);
    }

    @Override
    public MarginMode getMarginMode(Long userId) {
        return marginModeRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // 如果不存在，返回默认配置（全仓模式，10倍杠杆）
                    MarginMode defaultMode = new MarginMode();
                    defaultMode.setUserId(userId);
                    defaultMode.setMarginMode("CROSS");
                    defaultMode.setDefaultLeverage(10);
                    return defaultMode;
                });
    }

    @Override
    @Transactional
    public MarginSegment createSegment(Long userId, CreateSegmentRequest request) {
        // 验证保证金模式是否为分仓模式
        MarginMode marginMode = getMarginMode(userId);
        if (!"SEGMENTED".equals(marginMode.getMarginMode())) {
            throw new RuntimeException("当前不是分仓模式，无法创建分仓");
        }

        // 验证用户余额
        BigDecimal availableBalance = walletService.getAvailableBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY);
        if (availableBalance.compareTo(request.getMargin()) < 0) {
            throw new RuntimeException("余额不足，可用余额: " + availableBalance + " " + SETTLEMENT_CURRENCY);
        }

        // 创建分仓
        MarginSegment segment = new MarginSegment();
        segment.setUserId(userId);
        segment.setSegmentName(request.getSegmentName());
        segment.setMargin(request.getMargin());
        segment.setLeverage(request.getLeverage());
        segment.setIsActive(true);

        // 冻结保证金
        walletService.freezeBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY, request.getMargin());

        return marginSegmentRepository.save(segment);
    }

    @Override
    public List<MarginSegment> getSegments(Long userId) {
        return marginSegmentRepository.findByUserIdAndIsActiveTrue(userId);
    }

    @Override
    @Transactional
    public MarginSegment updateSegment(Long userId, Long segmentId, UpdateSegmentRequest request) {
        MarginSegment segment = marginSegmentRepository.findById(segmentId)
                .orElseThrow(() -> new RuntimeException("分仓不存在"));

        if (!segment.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此分仓");
        }

        // 检查分仓是否有关联的持仓
        List<com.cryptotrade.futures.usdt.entity.FuturesPosition> positions = 
                futuresPositionRepository.findByUserIdAndStatus(userId, "OPEN");
        boolean hasPositionInSegment = positions.stream()
                .anyMatch(p -> segmentId.equals(p.getSegmentId()));
        
        if (hasPositionInSegment) {
            throw new RuntimeException("分仓中有持仓，无法修改，请先平仓");
        }

        // 如果修改保证金，需要调整冻结金额
        if (request.getMargin() != null && request.getMargin().compareTo(segment.getMargin()) != 0) {
            BigDecimal marginDiff = request.getMargin().subtract(segment.getMargin());
            
            if (marginDiff.compareTo(BigDecimal.ZERO) > 0) {
                // 需要增加冻结金额
                BigDecimal availableBalance = walletService.getAvailableBalance(
                        userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY);
                if (availableBalance.compareTo(marginDiff) < 0) {
                    throw new RuntimeException("余额不足，需要增加保证金: " + marginDiff);
                }
                walletService.freezeBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY, marginDiff);
            } else {
                // 需要解冻金额
                walletService.unfreezeBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY, marginDiff.abs());
            }
            
            segment.setMargin(request.getMargin());
        }

        if (request.getSegmentName() != null) {
            segment.setSegmentName(request.getSegmentName());
        }

        if (request.getLeverage() != null) {
            segment.setLeverage(request.getLeverage());
        }

        return marginSegmentRepository.save(segment);
    }

    @Override
    @Transactional
    public void deleteSegment(Long userId, Long segmentId) {
        MarginSegment segment = marginSegmentRepository.findById(segmentId)
                .orElseThrow(() -> new RuntimeException("分仓不存在"));

        if (!segment.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此分仓");
        }

        // 检查分仓是否有关联的持仓
        List<com.cryptotrade.futures.usdt.entity.FuturesPosition> positions = 
                futuresPositionRepository.findByUserIdAndStatus(userId, "OPEN");
        boolean hasPositionInSegment = positions.stream()
                .anyMatch(p -> segmentId.equals(p.getSegmentId()));
        
        if (hasPositionInSegment) {
            throw new RuntimeException("分仓中有持仓，无法删除，请先平仓");
        }

        // 解冻保证金
        walletService.unfreezeBalance(userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY, segment.getMargin());

        // 删除分仓（软删除）
        segment.setIsActive(false);
        marginSegmentRepository.save(segment);
    }

    @Override
    public BigDecimal calculateMargin(Long userId, BigDecimal positionQuantity, BigDecimal price,
                                     Integer leverage, String marginMode, Long segmentId) {
        if (positionQuantity == null || price == null || leverage == null) {
            return BigDecimal.ZERO;
        }

        // 基础保证金 = 持仓价值 / 杠杆倍数
        BigDecimal baseMargin = positionQuantity.multiply(price)
                .divide(BigDecimal.valueOf(leverage), 8, java.math.RoundingMode.HALF_UP);

        switch (marginMode) {
            case "CROSS":
                // 全仓模式：使用账户全部USDT余额
                BigDecimal totalBalance = walletService.getAvailableBalance(
                        userId, ACCOUNT_TYPE, SETTLEMENT_CURRENCY);
                return totalBalance;
                
            case "ISOLATED":
                // 逐仓模式：使用该仓位分配的保证金（返回基础保证金）
                return baseMargin;
                
            case "SEGMENTED":
                // 分仓模式：使用分仓的保证金
                if (segmentId == null) {
                    throw new RuntimeException("分仓模式必须指定分仓ID");
                }
                MarginSegment segment = marginSegmentRepository.findById(segmentId)
                        .orElseThrow(() -> new RuntimeException("分仓不存在"));
                if (!segment.getUserId().equals(userId)) {
                    throw new RuntimeException("无权使用此分仓");
                }
                return segment.getMargin();
                
            case "COMBINED":
                // 合仓模式：使用合并的保证金池（多个分仓的保证金之和）
                List<MarginSegment> segments = marginSegmentRepository.findByUserIdAndIsActiveTrue(userId);
                BigDecimal combinedMargin = segments.stream()
                        .map(MarginSegment::getMargin)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                return combinedMargin;
                
            default:
                return baseMargin;
        }
    }
}

