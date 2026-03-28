/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.UpdateMemberInfoRequest;
import com.cryptotrade.systemmanagement.entity.MemberManagement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 会员管理Service接口
 */
public interface MemberManagementService {
    
    /**
     * 获取所有会员列表（分页）
     */
    Page<MemberManagement> getAllMembers(Pageable pageable);

    /**
     * 根据ID获取会员信息
     */
    Optional<MemberManagement> getMemberById(Long id);

    /**
     * 根据用户ID获取会员信息
     */
    Optional<MemberManagement> getMemberByUserId(Long userId);

    /**
     * 根据条件搜索会员
     */
    Page<MemberManagement> searchMembers(String keyword, String tradingStatus, String userStatus, Pageable pageable);

    /**
     * 更新会员基本信息（邮箱、手机、国家）
     */
    MemberManagement updateMemberInfo(Long userId, UpdateMemberInfoRequest request);

    /**
     * 修改登录密码
     */
    void updateLoginPassword(Long userId, String newPassword);

    /**
     * 修改资产管理密码
     */
    void updateAssetPassword(Long userId, String newPassword);

    /**
     * 删除谷歌验证码
     */
    void deleteGoogleAuth(Long userId);

    /**
     * 禁用/启用设备
     */
    void disableDevice(Long userId, String deviceFingerprint);

    /**
     * 禁用/启用IP
     */
    void disableIp(Long userId, String ip);

    /**
     * 冻结/解冻交易
     */
    MemberManagement updateTradingStatus(Long userId, String tradingStatus);

    /**
     * 禁用/启用用户
     */
    MemberManagement updateUserStatus(Long userId, String userStatus);

    /**
     * 允许/拒绝提现
     */
    MemberManagement updateWithdrawStatus(Long userId, String withdrawStatus);
}














