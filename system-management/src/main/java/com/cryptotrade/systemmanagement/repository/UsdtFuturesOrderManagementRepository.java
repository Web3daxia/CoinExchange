/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.UsdtFuturesOrderManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * U本位永续合约委托管理Repository
 */
@Repository
public interface UsdtFuturesOrderManagementRepository extends JpaRepository<UsdtFuturesOrderManagement, Long>, JpaSpecificationExecutor<UsdtFuturesOrderManagement> {
    
    Optional<UsdtFuturesOrderManagement> findByOrderId(Long orderId);
    List<UsdtFuturesOrderManagement> findByUserId(Long userId);
    Optional<UsdtFuturesOrderManagement> findByOrderNo(String orderNo);
    List<UsdtFuturesOrderManagement> findByStatus(String status);
}














