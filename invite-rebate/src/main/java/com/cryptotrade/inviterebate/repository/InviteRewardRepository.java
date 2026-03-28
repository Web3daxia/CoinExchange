/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.repository;

import com.cryptotrade.inviterebate.entity.InviteReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 邀请奖励Repository
 */
@Repository
public interface InviteRewardRepository extends JpaRepository<InviteReward, Long> {
    /**
     * 根据邀请者ID查询所有奖励
     */
    List<InviteReward> findByInviterId(Long inviterId);

    /**
     * 根据邀请者ID和奖励类型查询
     */
    List<InviteReward> findByInviterIdAndRewardType(Long inviterId, String rewardType);

    /**
     * 统计邀请者的累计奖励金额
     */
    @Query("SELECT COALESCE(SUM(ir.rewardAmount), 0) FROM InviteReward ir WHERE ir.inviterId = :inviterId AND ir.status = 'COMPLETED'")
    BigDecimal sumRewardAmountByInviterId(@Param("inviterId") Long inviterId);

    /**
     * 根据关系ID和奖励类型查询是否已存在
     */
    boolean existsByRelationIdAndRewardType(Long relationId, String rewardType);
}















