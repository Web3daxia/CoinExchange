/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.service;

import com.cryptotrade.redpacket.dto.request.RedPacketUseRequest;
import com.cryptotrade.redpacket.entity.RedPacketUse;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 红包使用Service接口
 */
public interface RedPacketUseService {
    
    /**
     * 使用红包
     */
    RedPacketUse usePacket(Long userId, RedPacketUseRequest request);
    
    /**
     * 根据ID获取使用记录
     */
    RedPacketUse getUseById(Long useId);
    
    /**
     * 获取红包的使用记录
     */
    List<RedPacketUse> getPacketUses(Long packetId);
    
    /**
     * 获取用户的使用记录
     */
    List<RedPacketUse> getUserUses(Long userId);
    
    /**
     * 获取用户的分页使用记录
     */
    Page<RedPacketUse> getUserUses(Long userId, Integer page, Integer size);
}














