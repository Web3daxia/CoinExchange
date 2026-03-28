/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.repository;

import com.cryptotrade.proofofreserves.entity.PlatformAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 平台资产Repository
 */
@Repository
public interface PlatformAssetRepository extends JpaRepository<PlatformAsset, Long> {
    /**
     * 根据资产类型查询
     */
    List<PlatformAsset> findByAssetType(String assetType);

    /**
     * 根据币种查询
     */
    List<PlatformAsset> findByCurrency(String currency);

    /**
     * 统计总资产
     */
    @Query("SELECT COALESCE(SUM(pa.balance), 0) FROM PlatformAsset pa WHERE pa.currency = :currency")
    BigDecimal sumBalanceByCurrency(@Param("currency") String currency);
}















