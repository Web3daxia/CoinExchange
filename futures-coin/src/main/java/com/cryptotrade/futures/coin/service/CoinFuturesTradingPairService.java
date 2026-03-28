/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service;

import com.cryptotrade.futures.coin.entity.CoinFuturesTradingPair;
import java.util.List;

public interface CoinFuturesTradingPairService {
    CoinFuturesTradingPair createTradingPair(CoinFuturesTradingPair tradingPair);
    CoinFuturesTradingPair updateTradingPair(Long id, CoinFuturesTradingPair tradingPair);
    void deleteTradingPair(Long id);
    CoinFuturesTradingPair getTradingPairById(Long id);
    CoinFuturesTradingPair getTradingPairByPairName(String pairName);
    List<CoinFuturesTradingPair> getAllTradingPairs();
    List<CoinFuturesTradingPair> getActiveTradingPairs();
    List<CoinFuturesTradingPair> getVisibleTradingPairs();
}














