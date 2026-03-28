/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service;

import com.cryptotrade.leveraged.entity.LeveragedTradingPair;
import java.util.List;

public interface LeveragedTradingPairService {
    LeveragedTradingPair createTradingPair(LeveragedTradingPair tradingPair);
    LeveragedTradingPair updateTradingPair(Long id, LeveragedTradingPair tradingPair);
    void deleteTradingPair(Long id);
    LeveragedTradingPair getTradingPairById(Long id);
    LeveragedTradingPair getTradingPairByPairName(String pairName);
    List<LeveragedTradingPair> getAllTradingPairs();
    List<LeveragedTradingPair> getActiveTradingPairs();
    List<LeveragedTradingPair> getVisibleTradingPairs();
}














