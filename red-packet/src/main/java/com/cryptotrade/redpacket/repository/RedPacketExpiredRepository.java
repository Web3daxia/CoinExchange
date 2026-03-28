/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.repository;

import com.cryptotrade.redpacket.entity.RedPacketExpired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 红包过期记录Repository
 */
@Repository
public interface RedPacketExpiredRepository extends JpaRepository<RedPacketExpired, Long>, JpaSpecificationExecutor<RedPacketExpired> {
    
    List<RedPacketExpired> findByUserId(Long userId);
    
    List<RedPacketExpired> findByActivityId(Long activityId);
    
    List<RedPacketExpired> findByProcessStatus(String processStatus);
}














