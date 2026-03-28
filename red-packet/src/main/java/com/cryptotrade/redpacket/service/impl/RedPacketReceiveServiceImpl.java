/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.service.impl;

import com.cryptotrade.redpacket.dto.request.RedPacketReceiveRequest;
import com.cryptotrade.redpacket.entity.RedPacketActivity;
import com.cryptotrade.redpacket.entity.RedPacketReceive;
import com.cryptotrade.redpacket.repository.RedPacketActivityRepository;
import com.cryptotrade.redpacket.repository.RedPacketReceiveRepository;
import com.cryptotrade.redpacket.service.RedPacketReceiveService;
import com.cryptotrade.redpacket.util.PacketNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * 红包领取Service实现
 */
@Service
public class RedPacketReceiveServiceImpl implements RedPacketReceiveService {

    @Autowired
    private RedPacketReceiveRepository receiveRepository;

    @Autowired
    private RedPacketActivityRepository activityRepository;

    @Override
    @Transactional
    public RedPacketReceive receivePacket(Long userId, RedPacketReceiveRequest request) {
        // 获取红包活动
        RedPacketActivity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new RuntimeException("红包活动不存在: " + request.getActivityId()));

        // 检查活动状态
        if (!"ACTIVE".equals(activity.getStatus())) {
            throw new RuntimeException("红包活动不可用，当前状态: " + activity.getStatus());
        }

        // 检查活动时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime()) || now.isAfter(activity.getEndTime())) {
            throw new RuntimeException("红包活动不在有效期内");
        }

        // 检查是否已领取（防重复领取）
        if (hasUserReceived(userId, request.getActivityId())) {
            throw new RuntimeException("您已经领取过该活动的红包");
        }

        // 检查是否还有剩余红包
        if (activity.getIssuedCount() >= activity.getTotalCount()) {
            throw new RuntimeException("红包已发放完毕");
        }

        // TODO: 检查领取条件（交易金额、签到、邀请等）

        // 生成红包金额
        BigDecimal amount;
        if ("FIXED".equals(activity.getAmountType())) {
            amount = activity.getFixedAmount();
        } else {
            // 随机金额
            BigDecimal min = activity.getMinAmount();
            BigDecimal max = activity.getMaxAmount();
            Random random = new Random();
            double randomValue = min.doubleValue() + (max.doubleValue() - min.doubleValue()) * random.nextDouble();
            amount = BigDecimal.valueOf(randomValue).setScale(8, RoundingMode.HALF_UP);
        }

        // 创建红包领取记录
        RedPacketReceive receive = new RedPacketReceive();
        receive.setUserId(userId);
        receive.setActivityId(activity.getId());
        receive.setPacketNo(PacketNoGenerator.generatePacketNo());
        receive.setPacketType(activity.getPacketType());
        receive.setAmount(amount);
        receive.setRemainingAmount(amount);
        receive.setCurrency("USDT"); // 默认币种
        receive.setReceiveTime(now);
        receive.setExpireTime(now.plusDays(activity.getValidDays()));
        receive.setStatus("VALID");
        receive.setUseCount(0);
        receive.setMaxUseCount(1); // 默认只能使用一次

        RedPacketReceive savedReceive = receiveRepository.save(receive);

        // 更新活动的已发放数量
        activity.setIssuedCount(activity.getIssuedCount() + 1);
        activityRepository.save(activity);

        return savedReceive;
    }

    @Override
    public RedPacketReceive getReceiveById(Long receiveId) {
        return receiveRepository.findById(receiveId)
                .orElseThrow(() -> new RuntimeException("红包领取记录不存在: " + receiveId));
    }

    @Override
    public RedPacketReceive getReceiveByPacketNo(String packetNo) {
        return receiveRepository.findByPacketNo(packetNo)
                .orElseThrow(() -> new RuntimeException("红包领取记录不存在: " + packetNo));
    }

    @Override
    public List<RedPacketReceive> getUserPackets(Long userId) {
        return receiveRepository.findByUserId(userId);
    }

    @Override
    public Page<RedPacketReceive> getUserPackets(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return receiveRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<RedPacketReceive> getUserValidPackets(Long userId) {
        return receiveRepository.findByUserIdAndStatus(userId, "VALID");
    }

    @Override
    public boolean hasUserReceived(Long userId, Long activityId) {
        Long count = receiveRepository.countByUserIdAndActivityId(userId, activityId);
        return count > 0;
    }
}














