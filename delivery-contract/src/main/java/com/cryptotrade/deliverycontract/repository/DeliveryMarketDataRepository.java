/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.repository;

import com.cryptotrade.deliverycontract.entity.DeliveryMarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 交割合约行情Repository
 */
@Repository
public interface DeliveryMarketDataRepository extends JpaRepository<DeliveryMarketData, Long> {
    /**
     * 查询最新的行情数据
     */
    Optional<DeliveryMarketData> findFirstByContractIdOrderByCreatedAtDesc(Long contractId);
}















