/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.service;

import com.cryptotrade.deliverycontract.entity.DeliverySettlement;

import java.util.List;

/**
 * 交割合约结算服务接口
 */
public interface DeliverySettlementService {
    /**
     * 查询结算记录
     */
    List<DeliverySettlement> getSettlements(Long userId, Long contractId, String settlementType,
                                            String status, String startTime, String endTime);

    /**
     * 查询结算详情
     */
    DeliverySettlement getSettlementDetail(Long userId, Long settlementId);

    /**
     * 执行结算
     */
    DeliverySettlement executeSettlement(Long contractId, Long positionId);
}













