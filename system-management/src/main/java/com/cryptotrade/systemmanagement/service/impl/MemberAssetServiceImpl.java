/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.dto.request.LockAssetRequest;
import com.cryptotrade.systemmanagement.dto.request.MemberAssetOperationRequest;
import com.cryptotrade.systemmanagement.dto.request.ResetDepositAddressRequest;
import com.cryptotrade.systemmanagement.dto.response.MemberAssetResponse;
import com.cryptotrade.systemmanagement.entity.MemberAsset;
import com.cryptotrade.systemmanagement.repository.MemberAssetRepository;
import com.cryptotrade.systemmanagement.service.MemberAssetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会员资产管理Service实现
 */
@Service
public class MemberAssetServiceImpl implements MemberAssetService {

    @Autowired
    private MemberAssetRepository memberAssetRepository;

    @Override
    public List<MemberAssetResponse> getMemberAssets(Long userId) {
        List<MemberAsset> assets = memberAssetRepository.findByUserId(userId);
        return assets.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public MemberAssetResponse getMemberAssetByCurrency(Long userId, String currency) {
        MemberAsset asset = memberAssetRepository.findByUserIdAndCurrency(userId, currency)
                .orElseThrow(() -> new RuntimeException("资产不存在"));
        return convertToResponse(asset);
    }

    @Override
    @Transactional
    public MemberAsset deposit(Long userId, MemberAssetOperationRequest request) {
        MemberAsset asset = memberAssetRepository.findByUserIdAndCurrency(userId, request.getCurrency())
                .orElseGet(() -> {
                    MemberAsset newAsset = new MemberAsset();
                    newAsset.setUserId(userId);
                    newAsset.setCurrency(request.getCurrency());
                    newAsset.setAvailableBalance(BigDecimal.ZERO);
                    newAsset.setFrozenBalance(BigDecimal.ZERO);
                    newAsset.setPendingReleaseBalance(BigDecimal.ZERO);
                    newAsset.setIsLocked(false);
                    return newAsset;
                });

        // 增加可用余额
        asset.setAvailableBalance(asset.getAvailableBalance().add(request.getAmount()));
        return memberAssetRepository.save(asset);
    }

    @Override
    @Transactional
    public MemberAsset freeze(Long userId, MemberAssetOperationRequest request) {
        MemberAsset asset = memberAssetRepository.findByUserIdAndCurrency(userId, request.getCurrency())
                .orElseThrow(() -> new RuntimeException("资产不存在"));

        if (asset.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("可用余额不足");
        }

        // 从可用余额转移到冻结余额
        asset.setAvailableBalance(asset.getAvailableBalance().subtract(request.getAmount()));
        asset.setFrozenBalance(asset.getFrozenBalance().add(request.getAmount()));
        return memberAssetRepository.save(asset);
    }

    @Override
    @Transactional
    public MemberAsset unfreeze(Long userId, MemberAssetOperationRequest request) {
        MemberAsset asset = memberAssetRepository.findByUserIdAndCurrency(userId, request.getCurrency())
                .orElseThrow(() -> new RuntimeException("资产不存在"));

        if (asset.getFrozenBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("冻结余额不足");
        }

        // 从冻结余额转移到可用余额
        asset.setFrozenBalance(asset.getFrozenBalance().subtract(request.getAmount()));
        asset.setAvailableBalance(asset.getAvailableBalance().add(request.getAmount()));
        return memberAssetRepository.save(asset);
    }

    @Override
    @Transactional
    public MemberAsset lockAsset(Long userId, LockAssetRequest request) {
        MemberAsset asset = memberAssetRepository.findByUserIdAndCurrency(userId, request.getCurrency())
                .orElseThrow(() -> new RuntimeException("资产不存在"));
        asset.setIsLocked(request.getIsLocked());
        return memberAssetRepository.save(asset);
    }

    @Override
    @Transactional
    public MemberAsset resetDepositAddress(Long userId, ResetDepositAddressRequest request) {
        MemberAsset asset = memberAssetRepository.findByUserIdAndCurrency(userId, request.getCurrency())
                .orElseGet(() -> {
                    MemberAsset newAsset = new MemberAsset();
                    newAsset.setUserId(userId);
                    newAsset.setCurrency(request.getCurrency());
                    newAsset.setAvailableBalance(BigDecimal.ZERO);
                    newAsset.setFrozenBalance(BigDecimal.ZERO);
                    newAsset.setPendingReleaseBalance(BigDecimal.ZERO);
                    newAsset.setIsLocked(false);
                    return newAsset;
                });

        asset.setDepositAddress(request.getDepositAddress());
        asset.setDepositAddressTag(request.getDepositAddressTag());
        return memberAssetRepository.save(asset);
    }

    /**
     * 转换为响应DTO
     */
    private MemberAssetResponse convertToResponse(MemberAsset asset) {
        MemberAssetResponse response = new MemberAssetResponse();
        BeanUtils.copyProperties(asset, response);
        return response;
    }
}














