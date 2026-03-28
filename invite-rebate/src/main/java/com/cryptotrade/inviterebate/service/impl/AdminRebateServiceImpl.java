/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.service.impl;

import com.cryptotrade.inviterebate.entity.RebateRecord;
import com.cryptotrade.inviterebate.entity.RebateSettlement;
import com.cryptotrade.inviterebate.entity.SystemRebateConfig;
import com.cryptotrade.inviterebate.repository.*;
import com.cryptotrade.inviterebate.service.AdminRebateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台返佣管理服务实现
 */
@Service
public class AdminRebateServiceImpl implements AdminRebateService {
    @Autowired
    private SystemRebateConfigRepository systemRebateConfigRepository;

    @Autowired
    private RebateRecordRepository rebateRecordRepository;

    @Autowired
    private RebateSettlementRepository rebateSettlementRepository;

    @Autowired
    private InviteRelationRepository inviteRelationRepository;

    @Override
    @Transactional
    public SystemRebateConfig setSystemRebateConfig(SystemRebateConfig config) {
        if (config.getId() != null) {
            SystemRebateConfig existing = systemRebateConfigRepository.findById(config.getId())
                    .orElseThrow(() -> new RuntimeException("配置不存在"));
            // 更新配置
            if (config.getConfigName() != null) {
                existing.setConfigName(config.getConfigName());
            }
            if (config.getUserLevel() != null) {
                existing.setUserLevel(config.getUserLevel());
            }
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
            if (config.getMaxDailyRebate() != null) {
                existing.setMaxDailyRebate(config.getMaxDailyRebate());
            }
            if (config.getDefaultRebatePeriod() != null) {
                existing.setDefaultRebatePeriod(config.getDefaultRebatePeriod());
            }
            if (config.getStatus() != null) {
                existing.setStatus(config.getStatus());
            }
            if (config.getRemark() != null) {
                existing.setRemark(config.getRemark());
            }
            existing.setUpdatedAt(LocalDateTime.now());
            return systemRebateConfigRepository.save(existing);
        } else {
            config.setCreatedAt(LocalDateTime.now());
            config.setUpdatedAt(LocalDateTime.now());
            return systemRebateConfigRepository.save(config);
        }
    }

    @Override
    public List<SystemRebateConfig> getSystemRebateConfigs() {
        return systemRebateConfigRepository.findAll();
    }

    @Override
    @Transactional
    public RebateSettlement auditSettlement(Long settlementId, Long auditUserId, String status, String remark) {
        RebateSettlement settlement = rebateSettlementRepository.findById(settlementId)
                .orElseThrow(() -> new RuntimeException("结算记录不存在"));

        settlement.setStatus(status);
        settlement.setAuditUserId(auditUserId);
        settlement.setAuditRemark(remark);
        settlement.setUpdatedAt(LocalDateTime.now());

        if ("SETTLED".equals(status)) {
            settlement.setSettledAt(LocalDateTime.now());
            // 这里应该调用钱包服务发放返佣
            // walletService.addBalance(settlement.getInviterId(), settlement.getRebateCurrency(), settlement.getTotalRebateAmount());
        }

        return rebateSettlementRepository.save(settlement);
    }

    @Override
    public Map<String, Object> getRebateStatistics(String startDate, String endDate) {
        Map<String, Object> statistics = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(startDate + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " 23:59:59", formatter);

        // 总邀请人数
        Long totalInvites = inviteRelationRepository.count();
        statistics.put("totalInvites", totalInvites);

        // 总返佣金额
        List<RebateRecord> allRecords = rebateRecordRepository.findAll();
        BigDecimal totalRebate = allRecords.stream()
                .filter(r -> r.getCreatedAt().isAfter(start) && r.getCreatedAt().isBefore(end))
                .map(RebateRecord::getRebateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("totalRebate", totalRebate);

        // 已结算返佣金额
        BigDecimal settledRebate = allRecords.stream()
                .filter(r -> "SETTLED".equals(r.getStatus()) && r.getCreatedAt().isAfter(start) && r.getCreatedAt().isBefore(end))
                .map(RebateRecord::getRebateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("settledRebate", settledRebate);

        // 待结算返佣金额
        BigDecimal pendingRebate = allRecords.stream()
                .filter(r -> "PENDING".equals(r.getStatus()) && r.getCreatedAt().isAfter(start) && r.getCreatedAt().isBefore(end))
                .map(RebateRecord::getRebateAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("pendingRebate", pendingRebate);

        // 按交易类型统计
        Map<String, BigDecimal> rebateByType = allRecords.stream()
                .filter(r -> r.getCreatedAt().isAfter(start) && r.getCreatedAt().isBefore(end))
                .collect(Collectors.groupingBy(
                        RebateRecord::getTradeType,
                        Collectors.reducing(BigDecimal.ZERO, RebateRecord::getRebateAmount, BigDecimal::add)
                ));
        statistics.put("rebateByType", rebateByType);

        return statistics;
    }

    @Override
    public List<RebateRecord> getLargeRebateRecords(BigDecimal minAmount) {
        return rebateRecordRepository.findAll().stream()
                .filter(r -> r.getRebateAmount().compareTo(minAmount) >= 0)
                .collect(Collectors.toList());
    }
}















