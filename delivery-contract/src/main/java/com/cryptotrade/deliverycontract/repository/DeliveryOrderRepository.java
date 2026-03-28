/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.repository;

import com.cryptotrade.deliverycontract.entity.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 交割合约订单Repository
 */
@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
    /**
     * 根据用户ID查询订单
     */
    List<DeliveryOrder> findByUserId(Long userId);

    /**
     * 根据订单号查询
     */
    Optional<DeliveryOrder> findByOrderNo(String orderNo);

    /**
     * 根据状态查询订单
     */
    List<DeliveryOrder> findByUserIdAndStatus(Long userId, String status);

    /**
     * 查询待成交订单（用于撮合）
     */
    List<DeliveryOrder> findByContractIdAndStatusAndPriceType(Long contractId, String status, String priceType);
}















