/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.MemberManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 会员管理Repository
 */
@Repository
public interface MemberManagementRepository extends JpaRepository<MemberManagement, Long>, JpaSpecificationExecutor<MemberManagement> {
    
    /**
     * 根据用户ID查找
     */
    Optional<MemberManagement> findByUserId(Long userId);

    /**
     * 根据会员UID查找
     */
    Optional<MemberManagement> findByMemberUid(String memberUid);

    /**
     * 根据邮箱查找
     */
    Optional<MemberManagement> findByEmail(String email);

    /**
     * 根据手机号查找
     */
    Optional<MemberManagement> findByPhone(String phone);

    /**
     * 根据代理商ID查找所有会员
     */
    List<MemberManagement> findByAgentId(Long agentId);

    /**
     * 根据邀请者ID查找所有被邀请的会员
     */
    List<MemberManagement> findByInviterId(Long inviterId);

    /**
     * 根据交易状态查找
     */
    List<MemberManagement> findByTradingStatus(String tradingStatus);

    /**
     * 根据用户状态查找
     */
    List<MemberManagement> findByUserStatus(String userStatus);
}














