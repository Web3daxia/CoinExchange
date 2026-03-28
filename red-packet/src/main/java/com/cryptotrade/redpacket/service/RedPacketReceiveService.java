/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.service;

import com.cryptotrade.redpacket.dto.request.RedPacketReceiveRequest;
import com.cryptotrade.redpacket.entity.RedPacketReceive;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 红包领取Service接口
 */
public interface RedPacketReceiveService {
    
    /**
     * 用户领取红包
     */
    RedPacketReceive receivePacket(Long userId, RedPacketReceiveRequest request);
    
    /**
     * 根据ID获取领取记录
     */
    RedPacketReceive getReceiveById(Long receiveId);
    
    /**
     * 根据红包编号获取领取记录
     */
    RedPacketReceive getReceiveByPacketNo(String packetNo);
    
    /**
     * 获取用户的所有红包
     */
    List<RedPacketReceive> getUserPackets(Long userId);
    
    /**
     * 获取用户的分页红包
     */
    Page<RedPacketReceive> getUserPackets(Long userId, Integer page, Integer size);
    
    /**
     * 获取用户的有效红包
     */
    List<RedPacketReceive> getUserValidPackets(Long userId);
    
    /**
     * 检查用户是否已领取该活动红包
     */
    boolean hasUserReceived(Long userId, Long activityId);
}














