/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.repository;

import com.cryptotrade.miningpool.entity.MiningSettlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 挖矿收益结算记录Repository
 */
@Repository
public interface MiningSettlementRepository extends JpaRepository<MiningSettlement, Long>, JpaSpecificationExecutor<MiningSettlement> {
    
    List<MiningSettlement> findByWorkerId(Long workerId);
    
    List<MiningSettlement> findByUserId(Long userId);
    
    List<MiningSettlement> findByPoolId(Long poolId);
    
    List<MiningSettlement> findBySettlementStatus(String settlementStatus);
    
    Page<MiningSettlement> findByWorkerId(Long workerId, Pageable pageable);
    
    Page<MiningSettlement> findByUserId(Long userId, Pageable pageable);
}














