/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.repository;

import com.cryptotrade.deliverycontract.entity.DeliveryContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 交割合约Repository
 */
@Repository
public interface DeliveryContractRepository extends JpaRepository<DeliveryContract, Long> {
    /**
     * 根据合约交易对查询
     */
    Optional<DeliveryContract> findByContractSymbol(String contractSymbol);

    /**
     * 根据状态查询
     */
    List<DeliveryContract> findByStatus(String status);

    /**
     * 根据合约类型查询
     */
    List<DeliveryContract> findByContractType(String contractType);
}















