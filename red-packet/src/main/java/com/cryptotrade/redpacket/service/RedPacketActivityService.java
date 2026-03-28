/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.service;

import com.cryptotrade.redpacket.dto.request.RedPacketActivityCreateRequest;
import com.cryptotrade.redpacket.entity.RedPacketActivity;

import java.util.List;

/**
 * 红包活动管理Service接口
 */
public interface RedPacketActivityService {
    
    /**
     * 创建红包活动
     */
    RedPacketActivity createActivity(RedPacketActivityCreateRequest request);
    
    /**
     * 更新红包活动
     */
    RedPacketActivity updateActivity(Long activityId, RedPacketActivityCreateRequest request);
    
    /**
     * 删除红包活动
     */
    void deleteActivity(Long activityId);
    
    /**
     * 根据ID获取活动
     */
    RedPacketActivity getActivityById(Long activityId);
    
    /**
     * 根据活动代码获取活动
     */
    RedPacketActivity getActivityByCode(String activityCode);
    
    /**
     * 获取所有活动
     */
    List<RedPacketActivity> getAllActivities();
    
    /**
     * 获取活跃的活动
     */
    List<RedPacketActivity> getActiveActivities();
    
    /**
     * 根据红包类型获取活动
     */
    List<RedPacketActivity> getActivitiesByType(String packetType);
}














