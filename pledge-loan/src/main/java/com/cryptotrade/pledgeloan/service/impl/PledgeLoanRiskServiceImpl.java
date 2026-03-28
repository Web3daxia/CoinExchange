/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.service.impl;

import com.cryptotrade.pledgeloan.entity.PledgeLoanOrder;
import com.cryptotrade.pledgeloan.entity.PledgeLoanRiskRecord;
import com.cryptotrade.pledgeloan.repository.PledgeLoanOrderRepository;
import com.cryptotrade.pledgeloan.repository.PledgeLoanRiskRecordRepository;
import com.cryptotrade.pledgeloan.service.PledgeLoanOrderService;
import com.cryptotrade.pledgeloan.service.PledgeLoanRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 质押借币风险监控Service实现
 */
@Service
public class PledgeLoanRiskServiceImpl implements PledgeLoanRiskService {

    @Autowired
    private PledgeLoanRiskRecordRepository riskRecordRepository;

    @Autowired
    private PledgeLoanOrderService orderService;

    @Autowired
    private PledgeLoanOrderRepository orderRepository;

    @Override
    @Transactional
    public PledgeLoanRiskRecord createRiskRecord(Long orderId, String riskLevel, String riskMessage) {
        PledgeLoanOrder order = orderService.getOrderById(orderId);

        // 检查是否已存在未处理的风险记录
        List<PledgeLoanRiskRecord> existingRecords = riskRecordRepository.findByOrderId(orderId);
        boolean hasUnprocessed = existingRecords.stream()
                .anyMatch(r -> !r.getIsProcessed() && r.getRiskLevel().equals(riskLevel));

        if (hasUnprocessed) {
            // 如果已存在相同风险等级且未处理的记录，更新现有记录
            PledgeLoanRiskRecord existing = existingRecords.stream()
                    .filter(r -> !r.getIsProcessed() && r.getRiskLevel().equals(riskLevel))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existing.setHealthRate(order.getHealthRate() != null ? order.getHealthRate() : BigDecimal.ZERO);
                existing.setPledgeValue(order.getPledgeValue());
                existing.setLoanValue(order.getLoanValue());
                existing.setRiskMessage(riskMessage);
                existing.setCreatedAt(LocalDateTime.now());
                return riskRecordRepository.save(existing);
            }
        }

        // 创建新的风险记录
        PledgeLoanRiskRecord riskRecord = new PledgeLoanRiskRecord();
        riskRecord.setOrderId(orderId);
        riskRecord.setOrderNo(order.getOrderNo());
        riskRecord.setUserId(order.getUserId());
        riskRecord.setRiskLevel(riskLevel);
        riskRecord.setHealthRate(order.getHealthRate() != null ? order.getHealthRate() : BigDecimal.ZERO);
        riskRecord.setPledgeValue(order.getPledgeValue());
        riskRecord.setLoanValue(order.getLoanValue());
        riskRecord.setRiskMessage(riskMessage);
        riskRecord.setIsNotified(false);
        riskRecord.setIsProcessed(false);

        return riskRecordRepository.save(riskRecord);
    }

    @Override
    public List<PledgeLoanRiskRecord> getOrderRiskRecords(Long orderId) {
        return riskRecordRepository.findByOrderId(orderId);
    }

    @Override
    public List<PledgeLoanRiskRecord> getUserRiskRecords(Long userId) {
        return riskRecordRepository.findByUserId(userId);
    }

    @Override
    public List<PledgeLoanRiskRecord> getUnprocessedRiskRecords() {
        return riskRecordRepository.findByIsProcessedFalse();
    }

    @Override
    @Transactional
    public void markAsProcessed(Long riskRecordId, Long processorId) {
        PledgeLoanRiskRecord riskRecord = riskRecordRepository.findById(riskRecordId)
                .orElseThrow(() -> new RuntimeException("风险记录不存在: " + riskRecordId));

        riskRecord.setIsProcessed(true);
        riskRecord.setProcessTime(LocalDateTime.now());
        riskRecord.setProcessorId(processorId);
        riskRecordRepository.save(riskRecord);
    }

    @Override
    @Transactional
    public void notifyUserRisk(Long riskRecordId) {
        PledgeLoanRiskRecord riskRecord = riskRecordRepository.findById(riskRecordId)
                .orElseThrow(() -> new RuntimeException("风险记录不存在: " + riskRecordId));

        // TODO: 实现通知逻辑（短信、邮件、站内信等）
        System.out.println(String.format("通知用户风险: userId=%d, orderNo=%s, riskLevel=%s, message=%s",
                riskRecord.getUserId(), riskRecord.getOrderNo(), riskRecord.getRiskLevel(), riskRecord.getRiskMessage()));

        riskRecord.setIsNotified(true);
        riskRecord.setNotificationTime(LocalDateTime.now());
        riskRecordRepository.save(riskRecord);
    }
}














