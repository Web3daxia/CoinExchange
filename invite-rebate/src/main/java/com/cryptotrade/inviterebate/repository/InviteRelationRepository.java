/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.repository;

import com.cryptotrade.inviterebate.entity.InviteRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 邀请关系Repository
 */
@Repository
public interface InviteRelationRepository extends JpaRepository<InviteRelation, Long> {
    /**
     * 根据邀请者ID查询所有邀请关系
     */
    List<InviteRelation> findByInviterId(Long inviterId);

    /**
     * 根据被邀请者ID查询邀请关系
     */
    Optional<InviteRelation> findByInviteeId(Long inviteeId);

    /**
     * 根据邀请码查询邀请关系
     */
    Optional<InviteRelation> findByInviteCode(String inviteCode);

    /**
     * 统计邀请者邀请的好友数量
     */
    @Query("SELECT COUNT(ir) FROM InviteRelation ir WHERE ir.inviterId = :inviterId AND ir.status = 'ACTIVE'")
    Long countByInviterId(@Param("inviterId") Long inviterId);

    /**
     * 查询邀请者的有效邀请关系
     */
    @Query("SELECT ir FROM InviteRelation ir WHERE ir.inviterId = :inviterId AND ir.status = 'ACTIVE'")
    List<InviteRelation> findActiveByInviterId(@Param("inviterId") Long inviterId);
}















