/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.repository;

import com.cryptotrade.miningpool.entity.MiningWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户矿工记录Repository
 */
@Repository
public interface MiningWorkerRepository extends JpaRepository<MiningWorker, Long> {
    
    Optional<MiningWorker> findByUserIdAndPoolId(Long userId, Long poolId);
    
    List<MiningWorker> findByUserId(Long userId);
    
    List<MiningWorker> findByPoolId(Long poolId);
    
    List<MiningWorker> findByStatus(String status);
    
    List<MiningWorker> findByPoolIdAndStatus(Long poolId, String status);
    
    List<MiningWorker> findAllByPoolIdOrderByHashrateContributionDesc(Long poolId);
}














