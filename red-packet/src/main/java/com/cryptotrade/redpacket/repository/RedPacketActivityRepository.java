/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.repository;

import com.cryptotrade.redpacket.entity.RedPacketActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 红包活动Repository
 */
@Repository
public interface RedPacketActivityRepository extends JpaRepository<RedPacketActivity, Long> {
    
    Optional<RedPacketActivity> findByActivityCode(String activityCode);
    
    List<RedPacketActivity> findByStatus(String status);
    
    List<RedPacketActivity> findByPacketType(String packetType);
    
    List<RedPacketActivity> findByDistributionScope(String distributionScope);
}














