/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.service.impl;

import com.cryptotrade.redpacket.dto.request.RedPacketActivityCreateRequest;
import com.cryptotrade.redpacket.entity.RedPacketActivity;
import com.cryptotrade.redpacket.repository.RedPacketActivityRepository;
import com.cryptotrade.redpacket.service.RedPacketActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 红包活动管理Service实现
 */
@Service
public class RedPacketActivityServiceImpl implements RedPacketActivityService {

    @Autowired
    private RedPacketActivityRepository activityRepository;

    @Override
    @Transactional
    public RedPacketActivity createActivity(RedPacketActivityCreateRequest request) {
        // 检查活动代码是否已存在
        if (activityRepository.findByActivityCode(request.getActivityCode()).isPresent()) {
            throw new RuntimeException("活动代码已存在: " + request.getActivityCode());
        }

        RedPacketActivity activity = new RedPacketActivity();
        activity.setActivityName(request.getActivityName());
        activity.setActivityCode(request.getActivityCode());
        activity.setPacketType(request.getPacketType());
        activity.setAmountType(request.getAmountType());
        activity.setFixedAmount(request.getFixedAmount());
        activity.setMinAmount(request.getMinAmount());
        activity.setMaxAmount(request.getMaxAmount());
        activity.setTotalAmount(request.getTotalAmount());
        activity.setTotalCount(request.getTotalCount());
        activity.setDistributionScope(request.getDistributionScope());
        activity.setReceiveCondition(request.getReceiveCondition());
        activity.setConditionValue(request.getConditionValue());
        activity.setValidDays(request.getValidDays() != null ? request.getValidDays() : 7);
        activity.setUseScope(request.getUseScope());
        activity.setUseTimeLimit(request.getUseTimeLimit());
        activity.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setDescription(request.getDescription());
        activity.setAutoIssue(request.getAutoIssue() != null ? request.getAutoIssue() : false);
        activity.setIssueCycle(request.getIssueCycle());

        activity.setIssuedCount(0);
        activity.setUsedCount(0);

        return activityRepository.save(activity);
    }

    @Override
    @Transactional
    public RedPacketActivity updateActivity(Long activityId, RedPacketActivityCreateRequest request) {
        RedPacketActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("红包活动不存在: " + activityId));

        if (request.getActivityName() != null) {
            activity.setActivityName(request.getActivityName());
        }
        if (request.getActivityCode() != null && !activity.getActivityCode().equals(request.getActivityCode())) {
            if (activityRepository.findByActivityCode(request.getActivityCode()).isPresent()) {
                throw new RuntimeException("活动代码已存在: " + request.getActivityCode());
            }
            activity.setActivityCode(request.getActivityCode());
        }
        if (request.getPacketType() != null) {
            activity.setPacketType(request.getPacketType());
        }
        if (request.getAmountType() != null) {
            activity.setAmountType(request.getAmountType());
        }
        if (request.getFixedAmount() != null) {
            activity.setFixedAmount(request.getFixedAmount());
        }
        if (request.getMinAmount() != null) {
            activity.setMinAmount(request.getMinAmount());
        }
        if (request.getMaxAmount() != null) {
            activity.setMaxAmount(request.getMaxAmount());
        }
        if (request.getTotalAmount() != null) {
            activity.setTotalAmount(request.getTotalAmount());
        }
        if (request.getTotalCount() != null) {
            activity.setTotalCount(request.getTotalCount());
        }
        if (request.getStatus() != null) {
            activity.setStatus(request.getStatus());
        }
        // ... 其他字段更新类似

        return activityRepository.save(activity);
    }

    @Override
    @Transactional
    public void deleteActivity(Long activityId) {
        if (!activityRepository.existsById(activityId)) {
            throw new RuntimeException("红包活动不存在: " + activityId);
        }
        // TODO: 检查是否有用户已领取，如果有则不允许删除
        activityRepository.deleteById(activityId);
    }

    @Override
    public RedPacketActivity getActivityById(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("红包活动不存在: " + activityId));
    }

    @Override
    public RedPacketActivity getActivityByCode(String activityCode) {
        return activityRepository.findByActivityCode(activityCode)
                .orElseThrow(() -> new RuntimeException("红包活动不存在: " + activityCode));
    }

    @Override
    public List<RedPacketActivity> getAllActivities() {
        return activityRepository.findAll();
    }

    @Override
    public List<RedPacketActivity> getActiveActivities() {
        return activityRepository.findByStatus("ACTIVE");
    }

    @Override
    public List<RedPacketActivity> getActivitiesByType(String packetType) {
        return activityRepository.findByPacketType(packetType);
    }
}














