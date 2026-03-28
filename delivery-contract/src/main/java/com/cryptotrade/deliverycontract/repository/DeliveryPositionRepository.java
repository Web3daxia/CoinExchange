/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.repository;

import com.cryptotrade.deliverycontract.entity.DeliveryPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 交割合约持仓Repository
 */
@Repository
public interface DeliveryPositionRepository extends JpaRepository<DeliveryPosition, Long> {
    /**
     * 根据用户ID查询持仓
     */
    List<DeliveryPosition> findByUserId(Long userId);

    /**
     * 根据用户ID和合约ID查询持仓
     */
    Optional<DeliveryPosition> findByUserIdAndContractIdAndSideAndStatus(
            Long userId, Long contractId, String side, String status);

    /**
     * 查询活跃持仓
     */
    List<DeliveryPosition> findByUserIdAndStatus(Long userId, String status);

    /**
     * 查询需要强平的持仓
     */
    List<DeliveryPosition> findByStatusAndLiquidationPriceIsNotNull(String status);
}















