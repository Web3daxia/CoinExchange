/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service;

import com.cryptotrade.options.entity.OptionsTradingPair;
import java.util.List;

public interface OptionsTradingPairService {
    OptionsTradingPair createTradingPair(OptionsTradingPair tradingPair);
    OptionsTradingPair updateTradingPair(Long id, OptionsTradingPair tradingPair);
    void deleteTradingPair(Long id);
    OptionsTradingPair getTradingPairById(Long id);
    OptionsTradingPair getTradingPairByPairName(String pairName);
    List<OptionsTradingPair> getAllTradingPairs();
    List<OptionsTradingPair> getActiveTradingPairs();
    List<OptionsTradingPair> getVisibleTradingPairs();
}














