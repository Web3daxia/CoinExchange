/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.service;

import com.cryptotrade.deliverycontract.entity.DeliveryContractTradingPair;
import java.util.List;

public interface DeliveryContractTradingPairService {
    DeliveryContractTradingPair createTradingPair(DeliveryContractTradingPair tradingPair);
    DeliveryContractTradingPair updateTradingPair(Long id, DeliveryContractTradingPair tradingPair);
    void deleteTradingPair(Long id);
    DeliveryContractTradingPair getTradingPairById(Long id);
    DeliveryContractTradingPair getTradingPairByPairName(String pairName);
    List<DeliveryContractTradingPair> getAllTradingPairs();
    List<DeliveryContractTradingPair> getActiveTradingPairs();
    List<DeliveryContractTradingPair> getVisibleTradingPairs();
}














