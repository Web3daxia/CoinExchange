/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.service;

import com.cryptotrade.deliverycontract.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交割合约服务接口
 */
public interface DeliveryContractService {
    /**
     * 创建订单
     */
    DeliveryOrder createOrder(Long userId, Long contractId, DeliveryOrder order);

    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 修改订单
     */
    DeliveryOrder modifyOrder(Long userId, Long orderId, BigDecimal price);

    /**
     * 获取订单列表
     */
    List<DeliveryOrder> getOrders(Long userId, String status);

    /**
     * 获取持仓列表
     */
    List<DeliveryPosition> getPositions(Long userId);

    /**
     * 平仓
     */
    DeliveryOrder closePosition(Long userId, Long positionId, BigDecimal quantity);

    /**
     * 获取行情数据
     */
    DeliveryMarketData getMarketData(Long contractId);

    /**
     * 强制平仓检查
     */
    void checkLiquidation(Long userId, Long positionId);

    /**
     * 结算合约
     */
    DeliverySettlement settleContract(Long contractId, Long positionId);
}















