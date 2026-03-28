/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.service.impl;

import com.cryptotrade.agent.entity.*;
import com.cryptotrade.agent.repository.*;
import com.cryptotrade.agent.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代理商服务实现
 */
@Service
public class AgentServiceImpl implements AgentService {
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentUserRelationRepository agentUserRelationRepository;

    @Autowired
    private AgentRebateConfigRepository agentRebateConfigRepository;

    @Autowired
    private AgentRebateRecordRepository agentRebateRecordRepository;

    @Autowired
    private AgentPerformanceRepository agentPerformanceRepository;

    @Value("${app.agent.invite-base-url:https://example.com/register?agent=}")
    private String inviteBaseUrl;

    @Override
    @Transactional
    public Agent applyAgent(Long userId, String agentName) {
        // 检查是否已经是代理商
        Optional<Agent> existingOpt = agentRepository.findByUserId(userId);
        if (existingOpt.isPresent()) {
            throw new RuntimeException("您已经是代理商");
        }

        Agent agent = new Agent();
        agent.setUserId(userId);
        agent.setAgentCode(generateAgentCode(userId));
        agent.setAgentName(agentName);
        agent.setLevel("LEVEL1");
        agent.setStatus("PENDING");
        agent.setApplyTime(LocalDateTime.now());
        agent.setCreatedAt(LocalDateTime.now());
        agent.setUpdatedAt(LocalDateTime.now());

        return agentRepository.save(agent);
    }

    @Override
    public Map<String, String> generateInviteLink(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("代理商不存在"));

        if (!"APPROVED".equals(agent.getStatus())) {
            throw new RuntimeException("代理商状态异常，无法生成邀请链接");
        }

        String inviteCode = agent.getAgentCode();
        String inviteLink = inviteBaseUrl + inviteCode;
        String qrCodeUrl = generateQRCodeUrl(inviteCode);

        // 注意：这里只是生成链接，不需要保存关系，关系在用户注册时创建

        Map<String, String> result = new HashMap<>();
        result.put("inviteCode", inviteCode);
        result.put("inviteLink", inviteLink);
        result.put("qrCodeUrl", qrCodeUrl);
        return result;
    }

    @Override
    @Transactional
    public AgentUserRelation registerUserByInviteCode(Long userId, String inviteCode) {
        // 查找代理商
        Optional<Agent> agentOpt = agentRepository.findByAgentCode(inviteCode);
        if (!agentOpt.isPresent()) {
            throw new RuntimeException("无效的代理商邀请码");
        }

        Agent agent = agentOpt.get();
        if (!"APPROVED".equals(agent.getStatus())) {
            throw new RuntimeException("代理商状态异常");
        }

        // 检查用户是否已被其他代理商邀请
        Optional<AgentUserRelation> existingOpt = agentUserRelationRepository.findByUserId(userId);
        if (existingOpt.isPresent()) {
            throw new RuntimeException("用户已被其他代理商邀请");
        }

        // 创建用户关系
        AgentUserRelation relation = new AgentUserRelation();
        relation.setAgentId(agent.getId());
        relation.setUserId(userId);
        relation.setInviteCode(inviteCode);
        relation.setInviteLink(inviteBaseUrl + inviteCode);
        relation.setUserGroup("NORMAL");
        relation.setStatus("ACTIVE");
        relation.setCreatedAt(LocalDateTime.now());
        relation.setUpdatedAt(LocalDateTime.now());

        return agentUserRelationRepository.save(relation);
    }

    @Override
    public List<Map<String, Object>> getAgentUsers(Long agentId, String userGroup) {
        List<AgentUserRelation> relations;
        if (userGroup != null) {
            relations = agentUserRelationRepository.findByAgentIdAndUserGroup(agentId, userGroup);
        } else {
            relations = agentUserRelationRepository.findByAgentIdAndStatus(agentId, "ACTIVE");
        }

        // 这里应该查询用户详细信息、资产信息、交易信息等
        // 由于涉及其他模块，这里简化处理
        return relations.stream().map(relation -> {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", relation.getUserId());
            userInfo.put("userGroup", relation.getUserGroup());
            userInfo.put("inviteTime", relation.getCreatedAt());
            // TODO: 查询用户详细信息、资产、交易等
            return userInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getAgentCommission(Long agentId, String status, String period) {
        Map<String, Object> commission = new HashMap<>();

        // 返佣配置
        AgentRebateConfig config = agentRebateConfigRepository.findByAgentId(agentId).orElse(null);
        commission.put("rebateConfig", config);

        // 返佣记录
        List<AgentRebateRecord> records;
        if (status != null) {
            records = agentRebateRecordRepository.findByAgentIdAndStatus(agentId, status);
        } else {
            records = agentRebateRecordRepository.findByAgentId(agentId);
        }
        commission.put("rebateRecords", records);

        // 累计返佣金额
        BigDecimal totalRebate = agentRebateRecordRepository.sumRebateAmountByAgentId(agentId);
        commission.put("totalRebate", totalRebate);

        // 已结算返佣金额
        List<AgentRebateRecord> settledRecords = agentRebateRecordRepository.findByAgentIdAndStatus(agentId, "SETTLED");
        BigDecimal settledRebate = settledRecords.stream()
                .map(AgentRebateRecord::getRebateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        commission.put("settledRebate", settledRebate);

        // 待结算返佣金额
        List<AgentRebateRecord> pendingRecords = agentRebateRecordRepository.findByAgentIdAndStatus(agentId, "PENDING");
        BigDecimal pendingRebate = pendingRecords.stream()
                .map(AgentRebateRecord::getRebateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        commission.put("pendingRebate", pendingRebate);

        return commission;
    }

    @Override
    public Map<String, Object> getAgentPerformance(Long agentId, String period) {
        Map<String, Object> performance = new HashMap<>();
        
        // period参数暂时未使用，保留用于未来扩展

        // 邀请用户数
        Long inviteUserCount = agentUserRelationRepository.countByAgentId(agentId);
        performance.put("inviteUserCount", inviteUserCount);

        // 总交易量（这里需要从交易模块查询，简化处理）
        performance.put("totalTradeVolume", BigDecimal.ZERO);
        performance.put("spotTradeVolume", BigDecimal.ZERO);
        performance.put("futuresTradeVolume", BigDecimal.ZERO);
        performance.put("optionsTradeVolume", BigDecimal.ZERO);

        // 总返佣金额
        BigDecimal totalRebate = agentRebateRecordRepository.sumRebateAmountByAgentId(agentId);
        performance.put("totalRebate", totalRebate);

        // 已结算返佣金额
        BigDecimal settledRebate = agentRebateRecordRepository.findByAgentIdAndStatus(agentId, "SETTLED")
                .stream()
                .map(AgentRebateRecord::getRebateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        performance.put("settledRebate", settledRebate);

        // 待结算返佣金额
        BigDecimal pendingRebate = agentRebateRecordRepository.findByAgentIdAndStatus(agentId, "PENDING")
                .stream()
                .map(AgentRebateRecord::getRebateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        performance.put("pendingRebate", pendingRebate);

        return performance;
    }

    @Override
    @Transactional
    public AgentRebateRecord calculateAgentRebate(Long userId, String tradeType, Long orderId, BigDecimal feeAmount, String feeCurrency) {
        // 查找用户关系
        Optional<AgentUserRelation> relationOpt = agentUserRelationRepository.findByUserId(userId);
        if (!relationOpt.isPresent()) {
            return null; // 用户不是代理商邀请的
        }

        AgentUserRelation relation = relationOpt.get();
        Long agentId = relation.getAgentId();

        // 获取返佣配置
        Optional<AgentRebateConfig> configOpt = agentRebateConfigRepository.findByAgentId(agentId);
        if (!configOpt.isPresent() || !"ACTIVE".equals(configOpt.get().getStatus())) {
            return null; // 没有返佣配置或未启用
        }

        AgentRebateConfig config = configOpt.get();

        // 获取返佣比例
        BigDecimal rebateRate = getRebateRate(config, tradeType);
        if (rebateRate == null || rebateRate.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        // 计算返佣金额
        BigDecimal rebateAmount = feeAmount.multiply(rebateRate).divide(new BigDecimal("100"), 8, BigDecimal.ROUND_HALF_UP);

        // 创建返佣记录
        AgentRebateRecord record = new AgentRebateRecord();
        record.setRebateNo(generateRebateNo());
        record.setAgentId(agentId);
        record.setUserId(userId);
        record.setTradeType(tradeType);
        record.setOrderId(orderId);
        record.setFeeAmount(feeAmount);
        record.setRebateRate(rebateRate);
        record.setRebateAmount(rebateAmount);
        record.setRebateCurrency(feeCurrency);
        record.setRebatePeriod(config.getRebatePeriod());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime periodStart = calculatePeriodStart(now, config.getRebatePeriod());
        LocalDateTime periodEnd = calculatePeriodEnd(periodStart, config.getRebatePeriod());
        record.setPeriodStart(periodStart);
        record.setPeriodEnd(periodEnd);

        record.setStatus("PENDING");
        record.setCreatedAt(now);
        record.setUpdatedAt(now);

        return agentRebateRecordRepository.save(record);
    }

    @Override
    public AgentRebateConfig getAgentRebateConfig(Long agentId) {
        return agentRebateConfigRepository.findByAgentId(agentId).orElse(null);
    }

    @Override
    @Transactional
    public void groupUser(Long agentId, Long userId, String userGroup) {
        AgentUserRelation relation = agentUserRelationRepository.findByAgentId(agentId)
                .stream()
                .filter(r -> r.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("用户关系不存在"));

        relation.setUserGroup(userGroup);
        relation.setUpdatedAt(LocalDateTime.now());
        agentUserRelationRepository.save(relation);
    }

    // ==================== 私有方法 ====================

    private String generateAgentCode(Long userId) {
        return "AGT" + userId + System.currentTimeMillis() % 100000;
    }

    private String generateQRCodeUrl(String inviteCode) {
        return "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=" + inviteBaseUrl + inviteCode;
    }

    private String generateRebateNo() {
        return "ARB" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    private BigDecimal getRebateRate(AgentRebateConfig config, String tradeType) {
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

