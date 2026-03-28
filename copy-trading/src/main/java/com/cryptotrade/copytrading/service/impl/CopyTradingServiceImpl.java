/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.service.impl;

import com.cryptotrade.copytrading.entity.CopyOrder;
import com.cryptotrade.copytrading.entity.CopyTradingRelation;
import com.cryptotrade.copytrading.entity.Trader;
import com.cryptotrade.copytrading.repository.CopyOrderRepository;
import com.cryptotrade.copytrading.repository.CopyTradingRelationRepository;
import com.cryptotrade.copytrading.repository.TraderRepository;
import com.cryptotrade.copytrading.service.CopyTradingService;
import com.cryptotrade.spot.service.SpotOrderService;
import com.cryptotrade.spot.dto.request.CreateOrderRequest;
import com.cryptotrade.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 跟单服务实现类
 */
@Service
public class CopyTradingServiceImpl implements CopyTradingService {

    @Autowired
    private CopyTradingRelationRepository copyTradingRelationRepository;

    @Autowired
    private CopyOrderRepository copyOrderRepository;

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private SpotOrderService spotOrderService;

    @Autowired
    private WalletService walletService;

    @Override
    @Transactional
    public CopyTradingRelation followTrader(Long followerId, Long traderId, String marketType, String copyType,
                                           BigDecimal allocationAmount, BigDecimal copyRatio,
                                           Map<String, Object> settings) {
        // 验证带单员
        Trader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new RuntimeException("带单员不存在"));

        if (!"APPROVED".equals(trader.getStatus())) {
            throw new RuntimeException("带单员状态异常");
        }

        // 检查跟单类型权限
        if ("PUBLIC".equals(copyType) && !Boolean.TRUE.equals(trader.getPublicEnabled())) {
            throw new RuntimeException("该带单员未开启公域跟单");
        }
        if ("PRIVATE".equals(copyType)) {
            if (!Boolean.TRUE.equals(trader.getPrivateEnabled())) {
                throw new RuntimeException("该带单员未开启私域跟单");
            }
            // 验证邀请码
            String inviteCode = settings != null && settings.containsKey("inviteCode") ?
                    settings.get("inviteCode").toString() : null;
            if (inviteCode == null || !inviteCode.equals(trader.getInviteCode())) {
                throw new RuntimeException("邀请码无效");
            }
        }

        // 检查是否已有跟单关系
        Optional<CopyTradingRelation> existing = copyTradingRelationRepository
                .findByTraderIdAndFollowerIdAndMarketTypeAndStatus(traderId, followerId, marketType, "ACTIVE");
        if (existing.isPresent()) {
            throw new RuntimeException("您已跟随该带单员");
        }

        // 检查余额
        String currency = "USDT"; // 默认USDT
        BigDecimal balance = walletService.getAvailableBalance(followerId, "SPOT", currency);
        if (balance.compareTo(allocationAmount) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 创建跟单关系
        CopyTradingRelation relation = new CopyTradingRelation();
        relation.setTraderId(traderId);
        relation.setFollowerId(followerId);
        relation.setMarketType(marketType);
        relation.setCopyType(copyType);
        relation.setAllocationAmount(allocationAmount);
        relation.setCopyRatio(copyRatio);
        relation.setStatus("ACTIVE");

        // 设置跟单参数
        if (settings != null) {
            if (settings.containsKey("leverage")) {
                relation.setLeverage(Integer.parseInt(settings.get("leverage").toString()));
            }
            if (settings.containsKey("marginMode")) {
                relation.setMarginMode(settings.get("marginMode").toString());
            }
            if (settings.containsKey("stopLossPrice")) {
                relation.setStopLossPrice(new BigDecimal(settings.get("stopLossPrice").toString()));
            }
            if (settings.containsKey("takeProfitPrice")) {
                relation.setTakeProfitPrice(new BigDecimal(settings.get("takeProfitPrice").toString()));
            }
            if (settings.containsKey("stopLossPercentage")) {
                relation.setStopLossPercentage(new BigDecimal(settings.get("stopLossPercentage").toString()));
            }
            if (settings.containsKey("takeProfitPercentage")) {
                relation.setTakeProfitPercentage(new BigDecimal(settings.get("takeProfitPercentage").toString()));
            }
        }

        relation = copyTradingRelationRepository.save(relation);

        // 更新带单员的跟单人数
        trader.setTotalFollowers(trader.getTotalFollowers() + 1);
        trader.setTotalAum(trader.getTotalAum().add(allocationAmount));
        traderRepository.save(trader);

        return relation;
    }

    @Override
    @Transactional
    public void stopCopyTrading(Long followerId, Long relationId) {
        CopyTradingRelation relation = copyTradingRelationRepository.findById(relationId)
                .orElseThrow(() -> new RuntimeException("跟单关系不存在"));

        if (!relation.getFollowerId().equals(followerId)) {
            throw new RuntimeException("无权操作此跟单关系");
        }

        relation.setStatus("STOPPED");
        copyTradingRelationRepository.save(relation);

        // 更新带单员统计
        Trader trader = traderRepository.findById(relation.getTraderId())
                .orElse(null);
        if (trader != null) {
            trader.setTotalFollowers(Math.max(0, trader.getTotalFollowers() - 1));
            trader.setTotalAum(trader.getTotalAum().subtract(relation.getAllocationAmount()));
            traderRepository.save(trader);
        }
    }

    @Override
    @Transactional
    public void pauseCopyTrading(Long followerId, Long relationId) {
        CopyTradingRelation relation = copyTradingRelationRepository.findById(relationId)
                .orElseThrow(() -> new RuntimeException("跟单关系不存在"));

        if (!relation.getFollowerId().equals(followerId)) {
            throw new RuntimeException("无权操作此跟单关系");
        }

        relation.setStatus("PAUSED");
        copyTradingRelationRepository.save(relation);
    }

    @Override
    @Transactional
    public void resumeCopyTrading(Long followerId, Long relationId) {
        CopyTradingRelation relation = copyTradingRelationRepository.findById(relationId)
                .orElseThrow(() -> new RuntimeException("跟单关系不存在"));

        if (!relation.getFollowerId().equals(followerId)) {
            throw new RuntimeException("无权操作此跟单关系");
        }

        relation.setStatus("ACTIVE");
        copyTradingRelationRepository.save(relation);
    }

    @Override
    @Transactional
    public CopyTradingRelation updateCopyTradingSettings(Long followerId, Long relationId, Map<String, Object> settings) {
        CopyTradingRelation relation = copyTradingRelationRepository.findById(relationId)
                .orElseThrow(() -> new RuntimeException("跟单关系不存在"));

        if (!relation.getFollowerId().equals(followerId)) {
            throw new RuntimeException("无权操作此跟单关系");
        }

        if (settings.containsKey("allocationAmount")) {
            BigDecimal newAmount = new BigDecimal(settings.get("allocationAmount").toString());
            relation.setAllocationAmount(newAmount);
        }
        if (settings.containsKey("stopLossPrice")) {
            relation.setStopLossPrice(new BigDecimal(settings.get("stopLossPrice").toString()));
        }
        if (settings.containsKey("takeProfitPrice")) {
            relation.setTakeProfitPrice(new BigDecimal(settings.get("takeProfitPrice").toString()));
        }
        if (settings.containsKey("copyRatio")) {
            relation.setCopyRatio(new BigDecimal(settings.get("copyRatio").toString()));
        }

        return copyTradingRelationRepository.save(relation);
    }

    @Override
    public List<CopyTradingRelation> getCopyTradingStatus(Long followerId) {
        return copyTradingRelationRepository.findByFollowerIdAndStatus(followerId, "ACTIVE");
    }

    @Override
    @Transactional
    public void copyTraderOrder(Long traderId, Long traderOrderId, String marketType) {
        // 查找所有活跃的跟单关系
        List<CopyTradingRelation> relations = copyTradingRelationRepository
                .findByTraderIdAndStatus(traderId, "ACTIVE");

        for (CopyTradingRelation relation : relations) {
            if (!marketType.equals(relation.getMarketType()) || !"ACTIVE".equals(relation.getStatus())) {
                continue;
            }

            try {
                // TODO: 根据marketType调用相应的订单服务复制订单
                // 这里需要根据实际的订单服务接口实现
                handleCopyOrder(relation, traderOrderId);
            } catch (Exception e) {
                System.err.println("复制订单失败: " + relation.getId() + ", " + e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void handleTraderOrder(Long traderId, Long orderId, String marketType, String pairName,
                                 String side, String action, BigDecimal quantity, BigDecimal price) {
        // 调用复制订单方法
        copyTraderOrder(traderId, orderId, marketType);
    }

    /**
     * 处理复制订单
     */
    private void handleCopyOrder(CopyTradingRelation relation, Long traderOrderId) {
        // TODO: 获取带单员的订单详情
        // 这里需要根据实际的订单服务获取订单信息
        // 然后根据跟单比例计算跟单员的订单数量并创建订单

        // 示例：现货跟单
        if ("SPOT".equals(relation.getMarketType())) {
            // 计算跟单数量
            // BigDecimal followerQuantity = traderQuantity.multiply(relation.getCopyRatio());
            
            // 创建跟单订单记录
            CopyOrder copyOrder = new CopyOrder();
            copyOrder.setTraderId(relation.getTraderId());
            copyOrder.setTraderOrderId(traderOrderId);
            copyOrder.setFollowerId(relation.getFollowerId());
            copyOrder.setRelationId(relation.getId());
            copyOrder.setMarketType(relation.getMarketType());
            copyOrder.setStatus("PENDING");
            copyOrderRepository.save(copyOrder);
        }
        // TODO: 合约跟单类似处理
    }
}

