/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service;

import com.cryptotrade.spot.entity.SpotTradingPair;
import java.util.List;

public interface SpotTradingPairService {
    SpotTradingPair createTradingPair(SpotTradingPair tradingPair);
    
    SpotTradingPair updateTradingPair(Long id, SpotTradingPair tradingPair);
    
    void deleteTradingPair(Long id);
    
    SpotTradingPair getTradingPairById(Long id);
    
    SpotTradingPair getTradingPairByPairName(String pairName);
    
    List<SpotTradingPair> getAllTradingPairs();
    
    List<SpotTradingPair> getActiveTradingPairs();
    
    List<SpotTradingPair> getVisibleTradingPairs();
}














