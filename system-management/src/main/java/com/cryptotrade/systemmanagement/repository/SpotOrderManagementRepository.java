/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.SpotOrderManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 现货订单管理Repository
 */
@Repository
public interface SpotOrderManagementRepository extends JpaRepository<SpotOrderManagement, Long>, JpaSpecificationExecutor<SpotOrderManagement> {
    
    /**
     * 根据订单ID查找
     */
    Optional<SpotOrderManagement> findByOrderId(Long orderId);

    /**
     * 根据用户ID查找
     */
    List<SpotOrderManagement> findByUserId(Long userId);

    /**
     * 根据订单号查找
     */
    Optional<SpotOrderManagement> findByOrderNo(String orderNo);

    /**
     * 根据状态查找
     */
    List<SpotOrderManagement> findByStatus(String status);

    /**
     * 根据订单来源查找
     */
    List<SpotOrderManagement> findByOrderSource(String orderSource);
}














