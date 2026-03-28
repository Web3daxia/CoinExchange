/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.repository;

import com.cryptotrade.redpacket.entity.RedPacketReceive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户红包领取记录Repository
 */
@Repository
public interface RedPacketReceiveRepository extends JpaRepository<RedPacketReceive, Long>, JpaSpecificationExecutor<RedPacketReceive> {
    
    Optional<RedPacketReceive> findByPacketNo(String packetNo);
    
    List<RedPacketReceive> findByUserId(Long userId);
    
    List<RedPacketReceive> findByActivityId(Long activityId);
    
    List<RedPacketReceive> findByUserIdAndStatus(Long userId, String status);
    
    List<RedPacketReceive> findByActivityIdAndStatus(Long activityId, String status);
    
    Page<RedPacketReceive> findByUserId(Long userId, Pageable pageable);
    
    List<RedPacketReceive> findByUserIdAndActivityId(Long userId, Long activityId);
    
    Long countByUserIdAndActivityId(Long userId, Long activityId);
}














