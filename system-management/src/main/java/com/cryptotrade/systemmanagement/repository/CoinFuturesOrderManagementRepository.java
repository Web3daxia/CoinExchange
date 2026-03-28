/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.CoinFuturesOrderManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 币本位永续合约委托管理Repository
 */
@Repository
public interface CoinFuturesOrderManagementRepository extends JpaRepository<CoinFuturesOrderManagement, Long>, JpaSpecificationExecutor<CoinFuturesOrderManagement> {
    
    Optional<CoinFuturesOrderManagement> findByOrderId(Long orderId);
    List<CoinFuturesOrderManagement> findByUserId(Long userId);
    Optional<CoinFuturesOrderManagement> findByOrderNo(String orderNo);
    List<CoinFuturesOrderManagement> findByStatus(String status);
}














