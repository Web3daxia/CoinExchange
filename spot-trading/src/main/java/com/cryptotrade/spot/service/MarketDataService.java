/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service;

import com.cryptotrade.spot.dto.response.KlineDataResponse;
import com.cryptotrade.spot.dto.response.MarketDataResponse;

import java.util.List;
import java.util.Map;

public interface MarketDataService {
    /**
     * 获取市场数据
     */
    MarketDataResponse getMarketData(String pairName);

    /**
     * 获取K线数据
     */
    KlineDataResponse getKlineData(String pairName, String interval, Integer limit);

    /**
     * 获取市场深度
     */
    Map<String, Object> getMarketDepth(String pairName, Integer limit);
    
    /**
     * 获取市场深度（增强版 - 支持买卖盘切换和价格精度）
     */
    com.cryptotrade.common.dto.MarketDepthResponse getMarketDepthEnhanced(com.cryptotrade.common.dto.MarketDepthRequest request);

    /**
     * 切换市场数据源
     */
    void switchDataSource(String dataSource);

    /**
     * 获取当前数据源
     */
    String getCurrentDataSource();

    /**
     * 同步市场数据（从外部API）
     */
    void syncMarketData(String pairName);
}


