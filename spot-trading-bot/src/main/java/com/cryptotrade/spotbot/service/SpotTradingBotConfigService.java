/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.service;

import com.cryptotrade.spotbot.dto.request.BotConfigRequest;
import com.cryptotrade.spotbot.dto.response.BotSimulationResponse;
import com.cryptotrade.spotbot.entity.SpotTradingBotConfig;

import java.util.List;

public interface SpotTradingBotConfigService {
    SpotTradingBotConfig createConfig(BotConfigRequest request);
    
    SpotTradingBotConfig updateConfig(Long configId, BotConfigRequest request);
    
    void deleteConfig(Long configId);
    
    SpotTradingBotConfig getConfigById(Long configId);
    
    SpotTradingBotConfig getConfigByPairName(String pairName);
    
    List<SpotTradingBotConfig> getAllConfigs();
    
    List<SpotTradingBotConfig> getActiveConfigs();
    
    BotSimulationResponse simulateConfig(BotConfigRequest request);
    
    BotSimulationResponse simulateConfigById(Long configId);
}














