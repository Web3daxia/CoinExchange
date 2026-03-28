/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.service.impl;

import com.cryptotrade.inviterebate.entity.*;
import com.cryptotrade.inviterebate.repository.*;
import com.cryptotrade.inviterebate.service.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 邀请服务实现
 */
@Service
public class InviteServiceImpl implements InviteService {
    @Autowired
    private InviteRelationRepository inviteRelationRepository;

    @Autowired
    private InviteRewardRepository inviteRewardRepository;

    @Autowired
    private RebateRecordRepository rebateRecordRepository;

    @Autowired
    private RebateConfigRepository rebateConfigRepository;

    @Autowired
    private SystemRebateConfigRepository systemRebateConfigRepository;

    @Autowired
    private RewardRuleRepository rewardRuleRepository;

    @Value("${app.invite.base-url:https://example.com/register?code=}")
    private String inviteBaseUrl;

    @Override
    public Map<String, String> generateInviteLink(Long userId) {
        // 生成唯一邀请码
        String inviteCode = generateInviteCode(userId);
        
        // 检查是否已存在邀请关系（作为邀请者）
        Optional<InviteRelation> existing = inviteRelationRepository.findByInviteCode(inviteCode);
        if (existing.isPresent() && !existing.get().getInviterId().equals(userId)) {
            // 如果邀请码已存在且不属于当前用户，重新生成
            inviteCode = generateInviteCode(userId);
        }

        String inviteLink = inviteBaseUrl + inviteCode;
        String qrCodeUrl = generateQRCodeUrl(inviteCode);

        Map<String, String> result = new HashMap<>();
        result.put("inviteCode", inviteCode);
        result.put("inviteLink", inviteLink);
        result.put("qrCodeUrl", qrCodeUrl);
        return result;
    }

    @Override
    @Transactional
    public InviteRelation registerByInviteCode(Long inviteeId, String inviteCode) {
        // 查找邀请关系
        Optional<InviteRelation> relationOpt = inviteRelationRepository.findByInviteCode(inviteCode);
        if (!relationOpt.isPresent()) {
            throw new RuntimeException("无效的邀请码");
        }

        InviteRelation relation = relationOpt.get();
        if (!"ACTIVE".equals(relation.getStatus())) {
            throw new RuntimeException("邀请码已失效");
        }

        // 检查是否已被使用
        if (relation.getInviteeId() != null) {
            throw new RuntimeException("邀请码已被使用");
        }

        // 设置被邀请者
        relation.setInviteeId(inviteeId);
        relation.setIsRegistered(true);
        relation.setUpdatedAt(LocalDateTime.now());
        inviteRelationRepository.save(relation);

        // 处理注册奖励
        processReward(inviteeId, "REGISTER");

        return relation;
    }

    @Override
    public Map<String, Object> getInviteStatus(Long userId) {
        Map<String, Object> status = new HashMap<>();

        // 邀请人数
        Long inviteCount = inviteRelationRepository.countByInviterId(userId);
        status.put("inviteCount", inviteCount);

        // 累计奖励金额
        BigDecimal totalReward = inviteRewardRepository.sumRewardAmountByInviterId(userId);
        status.put("totalReward", totalReward);

        // 累计返佣金额
        BigDecimal totalRebate = rebateRecordRepository.sumRebateAmountByInviterId(userId);
        status.put("totalRebate", totalRebate);

        // 返佣配置
        RebateConfig config = rebateConfigRepository.findByUserId(userId).orElse(null);
        status.put("rebateConfig", config);

        // 待结算返佣金额
        List<RebateRecord> pendingRecords = rebateRecordRepository.findByInviterIdAndStatus(userId, "PENDING");
        BigDecimal pendingRebate = pendingRecords.stream()
                .map(RebateRecord::getRebateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        status.put("pendingRebate", pendingRebate);

        return status;
    }

    @Override
    @Transactional
    public InviteReward processReward(Long inviteeId, String rewardType) {
        // 查找邀请关系
        Optional<InviteRelation> relationOpt = inviteRelationRepository.findByInviteeId(inviteeId);
        if (!relationOpt.isPresent()) {
            throw new RuntimeException("未找到邀请关系");
        }

        InviteRelation relation = relationOpt.get();
        Long inviterId = relation.getInviterId();

        // 检查是否已发放过该类型奖励
        if (inviteRewardRepository.existsByRelationIdAndRewardType(relation.getId(), rewardType)) {
            throw new RuntimeException("该类型奖励已发放");
        }

        // 查找奖励规则
        Optional<RewardRule> ruleOpt = rewardRuleRepository.findByRewardTypeAndStatus(rewardType, "ACTIVE");
        if (!ruleOpt.isPresent()) {
            throw new RuntimeException("未找到奖励规则");
        }

        RewardRule rule = ruleOpt.get();

        // 创建奖励记录
        InviteReward reward = new InviteReward();
        reward.setInviterId(inviterId);
        reward.setInviteeId(inviteeId);
        reward.setRelationId(relation.getId());
        reward.setRewardType(rewardType);
        reward.setRewardCurrency(rule.getRewardCurrency());
        reward.setRewardAmount(rule.getRewardAmount());
        reward.setStatus("PENDING");
        reward.setCreatedAt(LocalDateTime.now());
        reward.setUpdatedAt(LocalDateTime.now());

        // 发放奖励（这里应该调用钱包服务）
        // walletService.addBalance(inviterId, rule.getRewardCurrency(), rule.getRewardAmount());
        
        reward.setStatus("COMPLETED");
        reward.setDistributedAt(LocalDateTime.now());
        reward.setUpdatedAt(LocalDateTime.now());

        return inviteRewardRepository.save(reward);
    }

    @Override
    @Transactional
    public RebateRecord calculateRebate(Long inviteeId, String tradeType, Long orderId, BigDecimal feeAmount, String feeCurrency) {
        // 查找邀请关系
        Optional<InviteRelation> relationOpt = inviteRelationRepository.findByInviteeId(inviteeId);
        if (!relationOpt.isPresent()) {
            return null; // 没有邀请关系，不计算返佣
        }

        InviteRelation relation = relationOpt.get();
        Long inviterId = relation.getInviterId();

        // 获取返佣配置
        RebateConfig config = rebateConfigRepository.findByUserId(inviterId)
                .orElseGet(() -> createDefaultRebateConfig(inviterId));

        if (!"ACTIVE".equals(config.getStatus())) {
            return null; // 返佣配置未启用
        }

        // 获取返佣比例
        BigDecimal rebateRate = getRebateRate(config, tradeType);
        if (rebateRate == null || rebateRate.compareTo(BigDecimal.ZERO) <= 0) {
            return null; // 没有返佣比例
        }

        // 计算返佣金额
        BigDecimal rebateAmount = feeAmount.multiply(rebateRate).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_UP);

        // 检查每日返佣上限
        if (config.getDailyRebateLimit() != null) {
            LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime todayEnd = todayStart.plusDays(1);
            BigDecimal todayRebate = rebateRecordRepository.sumRebateAmountByPeriod(
                    inviterId, "DAILY", todayStart, todayEnd);
            if (todayRebate.add(rebateAmount).compareTo(config.getDailyRebateLimit()) > 0) {
                rebateAmount = config.getDailyRebateLimit().subtract(todayRebate);
                if (rebateAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    return null; // 已达到每日上限
                }
            }
        }

        // 创建返佣记录
        RebateRecord record = new RebateRecord();
        record.setRebateNo(generateRebateNo());
        record.setInviterId(inviterId);
        record.setInviteeId(inviteeId);
        record.setTradeType(tradeType);
        record.setOrderId(orderId);
        record.setFeeAmount(feeAmount);
        record.setRebateRate(rebateRate);
        record.setRebateAmount(rebateAmount);
        record.setRebateCurrency(feeCurrency);
        record.setRebatePeriod(config.getRebatePeriod());
        
        // 计算周期时间
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime periodStart = calculatePeriodStart(now, config.getRebatePeriod());
        LocalDateTime periodEnd = calculatePeriodEnd(periodStart, config.getRebatePeriod());
        record.setPeriodStart(periodStart);
        record.setPeriodEnd(periodEnd);
        
        record.setStatus("PENDING");
        record.setCreatedAt(now);
        record.setUpdatedAt(now);

        return rebateRecordRepository.save(record);
    }

    @Override
    public RebateConfig getUserRebateConfig(Long userId) {
        return rebateConfigRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultRebateConfig(userId));
    }

    @Override
    @Transactional
    public RebateConfig setUserRebateConfig(Long userId, RebateConfig config) {
        Optional<RebateConfig> existingOpt = rebateConfigRepository.findByUserId(userId);
        if (existingOpt.isPresent()) {
            RebateConfig existing = existingOpt.get();
            // 更新配置
            if (config.getSpotRebateRate() != null) {
                existing.setSpotRebateRate(config.getSpotRebateRate());
            }
            if (config.getFuturesUsdtRebateRate() != null) {
                existing.setFuturesUsdtRebateRate(config.getFuturesUsdtRebateRate());
            }
            if (config.getFuturesCoinRebateRate() != null) {
                existing.setFuturesCoinRebateRate(config.getFuturesCoinRebateRate());
            }
            if (config.getCopyTradingRebateRate() != null) {
                existing.setCopyTradingRebateRate(config.getCopyTradingRebateRate());
            }
            if (config.getOptionsRebateRate() != null) {
                existing.setOptionsRebateRate(config.getOptionsRebateRate());
            }
            if (config.getDailyRebateLimit() != null) {
                existing.setDailyRebateLimit(config.getDailyRebateLimit());
            }
            if (config.getRebatePeriod() != null) {
                existing.setRebatePeriod(config.getRebatePeriod());
            }
            if (config.getRebateMethod() != null) {
                existing.setRebateMethod(config.getRebateMethod());
            }
            existing.setUpdatedAt(LocalDateTime.now());
            return rebateConfigRepository.save(existing);
        } else {
            config.setUserId(userId);
            config.setStatus("ACTIVE");
            config.setCreatedAt(LocalDateTime.now());
            config.setUpdatedAt(LocalDateTime.now());
            return rebateConfigRepository.save(config);
        }
    }

    @Override
    public List<RebateRecord> getRebateRecords(Long userId, String status, String period) {
        if (status != null && period != null) {
            return rebateRecordRepository.findByInviterIdAndStatus(userId, status)
                    .stream()
                    .filter(r -> period.equals(r.getRebatePeriod()))
                    .collect(Collectors.toList());
        } else if (status != null) {
            return rebateRecordRepository.findByInviterIdAndStatus(userId, status);
        } else {
            return rebateRecordRepository.findByInviterId(userId);
        }
    }

    @Override
    public BigDecimal getTotalRebateAmount(Long userId) {
        return rebateRecordRepository.sumRebateAmountByInviterId(userId);
    }

    @Override
    public List<InviteRelation> getInvitedUsers(Long userId) {
        return inviteRelationRepository.findByInviterId(userId);
    }

    // ==================== 私有方法 ====================

    private String generateInviteCode(Long userId) {
        // 生成邀请码：用户ID + 随机字符串
        return "INV" + userId + System.currentTimeMillis() % 100000;
    }

    private String generateQRCodeUrl(String inviteCode) {
        // 生成二维码URL（实际应该调用二维码生成服务）
        return "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + inviteBaseUrl + inviteCode;
    }

    private String generateRebateNo() {
        return "RB" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    private RebateConfig createDefaultRebateConfig(Long userId) {
        // 获取系统默认配置
        Optional<SystemRebateConfig> defaultConfigOpt = systemRebateConfigRepository.findDefaultConfig();
        SystemRebateConfig defaultConfig = defaultConfigOpt.orElse(new SystemRebateConfig());

        RebateConfig config = new RebateConfig();
        config.setUserId(userId);
        config.setSpotRebateRate(defaultConfig.getSpotRebateRate());
        config.setFuturesUsdtRebateRate(defaultConfig.getFuturesUsdtRebateRate());
        config.setFuturesCoinRebateRate(defaultConfig.getFuturesCoinRebateRate());
        config.setCopyTradingRebateRate(defaultConfig.getCopyTradingRebateRate());
        config.setOptionsRebateRate(defaultConfig.getOptionsRebateRate());
        config.setDailyRebateLimit(defaultConfig.getMaxDailyRebate());
        config.setRebatePeriod(defaultConfig.getDefaultRebatePeriod());
        config.setRebateMethod("USDT");
        config.setStatus("ACTIVE");
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());

        return rebateConfigRepository.save(config);
    }

    private BigDecimal getRebateRate(RebateConfig config, String tradeType) {
        switch (tradeType) {
            case "SPOT":
                return config.getSpotRebateRate();
            case "FUTURES_USDT":
                return config.getFuturesUsdtRebateRate();
            case "FUTURES_COIN":
                return config.getFuturesCoinRebateRate();
            case "COPY_TRADING":
                return config.getCopyTradingRebateRate();
            case "OPTIONS":
                return config.getOptionsRebateRate();
            default:
                return null;
        }
    }

    private LocalDateTime calculatePeriodStart(LocalDateTime time, String period) {
        switch (period) {
            case "DAILY":
                return time.withHour(0).withMinute(0).withSecond(0).withNano(0);
            case "WEEKLY":
                int dayOfWeek = time.getDayOfWeek().getValue();
                return time.minusDays(dayOfWeek - 1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case "MONTHLY":
                return time.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            default:
                return time;
        }
    }

    private LocalDateTime calculatePeriodEnd(LocalDateTime periodStart, String period) {
        switch (period) {
            case "DAILY":
                return periodStart.plusDays(1);
            case "WEEKLY":
                return periodStart.plusWeeks(1);
            case "MONTHLY":
                return periodStart.plusMonths(1);
            default:
                return periodStart.plusDays(1);
        }
    }
}















