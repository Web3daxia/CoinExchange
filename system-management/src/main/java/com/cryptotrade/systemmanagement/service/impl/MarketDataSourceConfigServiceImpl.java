/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.MarketDataSourceConfig;
import com.cryptotrade.systemmanagement.repository.MarketDataSourceConfigRepository;
import com.cryptotrade.systemmanagement.service.MarketDataSourceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MarketDataSourceConfigServiceImpl implements MarketDataSourceConfigService {
    
    @Autowired
    private MarketDataSourceConfigRepository repository;
    
    @Override
    @Transactional
    public MarketDataSourceConfig createConfig(MarketDataSourceConfig config) {
        return repository.save(config);
    }
    
    @Override
    @Transactional
    public MarketDataSourceConfig updateConfig(Long id, MarketDataSourceConfig config) {
        MarketDataSourceConfig existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("配置不存在"));
        config.setId(id);
        config.setCreatedAt(existing.getCreatedAt());
        return repository.save(config);
    }
    
    @Override
    @Transactional
    public void deleteConfig(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public MarketDataSourceConfig getConfigById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("配置不存在"));
    }
    
    @Override
    public MarketDataSourceConfig getDefaultConfigByTradingArea(String tradingArea) {
        return repository.findByTradingAreaAndEnabledTrueAndIsDefaultTrue(tradingArea)
                .orElseThrow(() -> new RuntimeException("未找到该交易区域的默认行情源配置"));
    }
    
    @Override
    public List<MarketDataSourceConfig> getConfigsByTradingArea(String tradingArea) {
        return repository.findByTradingAreaAndEnabledTrue(tradingArea);
    }
    
    @Override
    public List<MarketDataSourceConfig> getAllConfigs() {
        return repository.findAll();
    }
    
    @Override
    @Transactional
    public void setDefault(Long id) {
        MarketDataSourceConfig config = getConfigById(id);
        // 取消该交易区域的其他默认配置
        repository.findByTradingArea(config.getTradingArea()).forEach(c -> {
            c.setIsDefault(false);
            repository.save(c);
        });
        // 设置新的默认
        config.setIsDefault(true);
        config.setEnabled(true);
        repository.save(config);
    }
}














