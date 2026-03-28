/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.repository;

import com.cryptotrade.deliverycontract.entity.DeliveryTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 交割合约成交Repository
 */
@Repository
public interface DeliveryTradeRepository extends JpaRepository<DeliveryTrade, Long> {
    /**
     * 根据成交单号查询
     */
    Optional<DeliveryTrade> findByTradeNo(String tradeNo);
}















