/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.MarketDataSourceConfig;
import java.util.List;

public interface MarketDataSourceConfigService {
    MarketDataSourceConfig createConfig(MarketDataSourceConfig config);
    MarketDataSourceConfig updateConfig(Long id, MarketDataSourceConfig config);
    void deleteConfig(Long id);
    MarketDataSourceConfig getConfigById(Long id);
    MarketDataSourceConfig getDefaultConfigByTradingArea(String tradingArea);
    List<MarketDataSourceConfig> getConfigsByTradingArea(String tradingArea);
    List<MarketDataSourceConfig> getAllConfigs();
    void setDefault(Long id);
}














