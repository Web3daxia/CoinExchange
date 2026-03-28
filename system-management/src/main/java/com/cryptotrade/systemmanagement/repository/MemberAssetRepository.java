/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.MemberAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 会员资产Repository
 */
@Repository
public interface MemberAssetRepository extends JpaRepository<MemberAsset, Long> {
    
    /**
     * 根据用户ID和币种查找
     */
    Optional<MemberAsset> findByUserIdAndCurrency(Long userId, String currency);

    /**
     * 根据用户ID查找所有资产
     */
    List<MemberAsset> findByUserId(Long userId);

    /**
     * 根据币种查找所有用户的资产
     */
    List<MemberAsset> findByCurrency(String currency);

    /**
     * 根据用户ID和是否锁定查找
     */
    List<MemberAsset> findByUserIdAndIsLocked(Long userId, Boolean isLocked);
}














