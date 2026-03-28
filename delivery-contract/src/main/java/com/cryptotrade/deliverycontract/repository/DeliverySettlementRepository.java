/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.repository;

import com.cryptotrade.deliverycontract.entity.DeliverySettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 交割合约结算Repository
 */
@Repository
public interface DeliverySettlementRepository extends JpaRepository<DeliverySettlement, Long> {
    /**
     * 根据用户ID查询结算记录
     */
    List<DeliverySettlement> findByUserId(Long userId);
}















