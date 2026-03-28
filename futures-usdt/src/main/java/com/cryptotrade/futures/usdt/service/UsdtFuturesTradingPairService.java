/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service;

import com.cryptotrade.futures.usdt.entity.UsdtFuturesTradingPair;
import java.util.List;

public interface UsdtFuturesTradingPairService {
    UsdtFuturesTradingPair createTradingPair(UsdtFuturesTradingPair tradingPair);
    UsdtFuturesTradingPair updateTradingPair(Long id, UsdtFuturesTradingPair tradingPair);
    void deleteTradingPair(Long id);
    UsdtFuturesTradingPair getTradingPairById(Long id);
    UsdtFuturesTradingPair getTradingPairByPairName(String pairName);
    List<UsdtFuturesTradingPair> getAllTradingPairs();
    List<UsdtFuturesTradingPair> getActiveTradingPairs();
    List<UsdtFuturesTradingPair> getVisibleTradingPairs();
}














