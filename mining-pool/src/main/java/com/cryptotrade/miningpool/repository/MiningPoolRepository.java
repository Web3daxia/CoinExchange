/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.repository;

import com.cryptotrade.miningpool.entity.MiningPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 矿池Repository
 */
@Repository
public interface MiningPoolRepository extends JpaRepository<MiningPool, Long> {
    
    Optional<MiningPool> findByPoolCode(String poolCode);
    
    List<MiningPool> findByStatus(String status);
    
    List<MiningPool> findByMiningCurrency(String miningCurrency);
    
    List<MiningPool> findByAlgorithm(String algorithm);
    
    List<MiningPool> findByRiskLevel(String riskLevel);
    
    List<MiningPool> findAllByOrderBySortOrderAsc();
}














