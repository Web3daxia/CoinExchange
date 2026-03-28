/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.repository;

import com.cryptotrade.redpacket.entity.RedPacketUse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 红包使用记录Repository
 */
@Repository
public interface RedPacketUseRepository extends JpaRepository<RedPacketUse, Long>, JpaSpecificationExecutor<RedPacketUse> {
    
    List<RedPacketUse> findByPacketId(Long packetId);
    
    List<RedPacketUse> findByUserId(Long userId);
    
    List<RedPacketUse> findByActivityId(Long activityId);
    
    List<RedPacketUse> findByOrderNo(String orderNo);
    
    Page<RedPacketUse> findByUserId(Long userId, Pageable pageable);
    
    Page<RedPacketUse> findByActivityId(Long activityId, Pageable pageable);
}














