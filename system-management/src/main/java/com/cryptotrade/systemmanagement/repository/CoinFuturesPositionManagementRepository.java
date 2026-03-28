/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.CoinFuturesPositionManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 币本位永续合约仓位管理Repository
 */
@Repository
public interface CoinFuturesPositionManagementRepository extends JpaRepository<CoinFuturesPositionManagement, Long>, JpaSpecificationExecutor<CoinFuturesPositionManagement> {
    
    Optional<CoinFuturesPositionManagement> findByPositionId(Long positionId);
    List<CoinFuturesPositionManagement> findByUserId(Long userId);
    List<CoinFuturesPositionManagement> findByPairName(String pairName);
    List<CoinFuturesPositionManagement> findByStatus(String status);
}














