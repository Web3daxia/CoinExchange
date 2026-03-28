/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.entity.CoinFuturesPosition;
import com.cryptotrade.futures.coin.repository.CoinFuturesPositionRepository;
import com.cryptotrade.futures.coin.service.CoinFuturesMarginModeService;
import com.cryptotrade.futures.usdt.dto.request.CreateSegmentRequest;
import com.cryptotrade.futures.usdt.dto.request.SetMarginModeRequest;
import com.cryptotrade.futures.usdt.dto.request.UpdateSegmentRequest;
import com.cryptotrade.futures.usdt.entity.MarginMode;
import com.cryptotrade.futures.usdt.entity.MarginSegment;
import com.cryptotrade.futures.usdt.repository.MarginModeRepository;
import com.cryptotrade.futures.usdt.repository.MarginSegmentRepository;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CoinFuturesMarginModeServiceImpl implements CoinFuturesMarginModeService {

    @Autowired
    private MarginModeRepository marginModeRepository;

    @Autowired
    private MarginSegmentRepository marginSegmentRepository;

    @Autowired
    private CoinFuturesPositionRepository coinFuturesPositionRepository;

    @Autowired
    private WalletService walletService;

    private static final String ACCOUNT_TYPE = "FUTURES"; // 永续合约账户类型

    @Override
    @Transactional
    public MarginMode setMarginMode(Long userId, SetMarginModeRequest request) {
        // 检查当前是否有持仓（如果有持仓，不能切换模式）
        List<CoinFuturesPosition> openPositions = 
                coinFuturesPositionRepository.findByUserIdAndStatus(userId, "OPEN");
        
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

        // 获取基础资产（从交易对名称中提取，简化处理）
        // 实际应该从合约信息中获取
        String baseCurrency = "BTC"; // 简化处理，实际应该从合约信息获取

        // 验证用户余额（币本位：使用基础资产）
        BigDecimal availableBalance = walletService.getAvailableBalance(userId, ACCOUNT_TYPE, baseCurrency);
        if (availableBalance.compareTo(request.getMargin()) < 0) {
            throw new RuntimeException("余额不足，可用余额: " + availableBalance + " " + baseCurrency);
        }

        // 创建分仓
        MarginSegment segment = new MarginSegment();
        segment.setUserId(userId);
        segment.setSegmentName(request.getSegmentName());
        segment.setMargin(request.getMargin());
        segment.setLeverage(request.getLeverage());
        segment.setIsActive(true);

        // 冻结保证金
        walletService.freezeBalance(userId, ACCOUNT_TYPE, baseCurrency, request.getMargin());

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

        // 检查是否有持仓使用此分仓
        List<CoinFuturesPosition> positions = coinFuturesPositionRepository.findByUserIdAndStatus(userId, "OPEN");
        boolean hasPosition = positions.stream()
                .anyMatch(p -> segmentId.equals(p.getSegmentId()));
        
        if (hasPosition) {
            throw new RuntimeException("该分仓有持仓，无法修改");
        }

        // 获取基础资产
        String baseCurrency = "BTC"; // 简化处理

        // 如果修改了保证金，需要调整冻结金额
        if (request.getMargin() != null && request.getMargin().compareTo(segment.getMargin()) != 0) {
            BigDecimal oldMargin = segment.getMargin();
            BigDecimal newMargin = request.getMargin();
            BigDecimal diff = newMargin.subtract(oldMargin);

            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                // 需要增加冻结
                if (!walletService.checkBalance(userId, ACCOUNT_TYPE, baseCurrency, diff)) {
                    throw new RuntimeException("余额不足");
                }
                walletService.freezeBalance(userId, ACCOUNT_TYPE, baseCurrency, diff);
            } else {
                // 需要解冻
                walletService.unfreezeBalance(userId, ACCOUNT_TYPE, baseCurrency, diff.abs());
            }
            segment.setMargin(newMargin);
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

        // 检查是否有持仓使用此分仓
        List<CoinFuturesPosition> positions = coinFuturesPositionRepository.findByUserIdAndStatus(userId, "OPEN");
        boolean hasPosition = positions.stream()
                .anyMatch(p -> segmentId.equals(p.getSegmentId()));
        
        if (hasPosition) {
            throw new RuntimeException("该分仓有持仓，无法删除");
        }

        // 获取基础资产
        String baseCurrency = "BTC"; // 简化处理

        // 解冻保证金
        walletService.unfreezeBalance(userId, ACCOUNT_TYPE, baseCurrency, segment.getMargin());

        // 软删除
        segment.setIsActive(false);
        marginSegmentRepository.save(segment);
    }

    @Override
    public BigDecimal calculateMargin(Long userId, String pairName, BigDecimal positionQuantity, 
                                     BigDecimal price, Integer leverage, String marginMode, Long segmentId) {
        // 计算基础保证金 = 持仓价值 / 杠杆倍数
        BigDecimal positionValue = positionQuantity.multiply(price);
        BigDecimal baseMargin = positionValue.divide(new BigDecimal(leverage), 8, java.math.RoundingMode.UP);

        // 根据保证金模式计算可用保证金
        switch (marginMode) {
            case "CROSS":
                // 全仓模式：使用账户全部可用余额
                // 获取基础资产
                String baseCurrency = pairName.split("/")[0];
                BigDecimal availableBalance = walletService.getAvailableBalance(userId, ACCOUNT_TYPE, baseCurrency);
                return availableBalance;
            
            case "ISOLATED":
                // 逐仓模式：只使用订单指定的保证金
                return baseMargin;
            
            case "SEGMENTED":
                // 分仓模式：使用分仓的保证金
                if (segmentId == null) {
                    throw new RuntimeException("分仓模式必须指定分仓ID");
                }
                MarginSegment segment = marginSegmentRepository.findById(segmentId)
                        .orElseThrow(() -> new RuntimeException("分仓不存在"));
                return segment.getMargin();
            
            case "COMBINED":
                // 组合模式：全仓 + 逐仓的组合
                baseCurrency = pairName.split("/")[0];
                availableBalance = walletService.getAvailableBalance(userId, ACCOUNT_TYPE, baseCurrency);
                return availableBalance.add(baseMargin);
            
            default:
                return baseMargin;
        }
    }
}















