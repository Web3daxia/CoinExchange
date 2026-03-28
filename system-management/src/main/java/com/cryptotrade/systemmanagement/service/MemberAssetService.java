/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.LockAssetRequest;
import com.cryptotrade.systemmanagement.dto.request.MemberAssetOperationRequest;
import com.cryptotrade.systemmanagement.dto.request.ResetDepositAddressRequest;
import com.cryptotrade.systemmanagement.dto.response.MemberAssetResponse;
import com.cryptotrade.systemmanagement.entity.MemberAsset;

import java.util.List;

/**
 * 会员资产管理Service接口
 */
public interface MemberAssetService {
    
    /**
     * 获取会员的所有资产列表
     */
    List<MemberAssetResponse> getMemberAssets(Long userId);

    /**
     * 根据币种获取会员资产
     */
    MemberAssetResponse getMemberAssetByCurrency(Long userId, String currency);

    /**
     * 充币操作
     */
    MemberAsset deposit(Long userId, MemberAssetOperationRequest request);

    /**
     * 冻结资产
     */
    MemberAsset freeze(Long userId, MemberAssetOperationRequest request);

    /**
     * 解冻资产
     */
    MemberAsset unfreeze(Long userId, MemberAssetOperationRequest request);

    /**
     * 锁定/解锁资产
     */
    MemberAsset lockAsset(Long userId, LockAssetRequest request);

    /**
     * 重置充值地址
     */
    MemberAsset resetDepositAddress(Long userId, ResetDepositAddressRequest request);
}














