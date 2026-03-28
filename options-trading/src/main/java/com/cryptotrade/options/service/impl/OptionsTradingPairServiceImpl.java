/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.options.service.impl;

import com.cryptotrade.options.entity.OptionsTradingPair;
import com.cryptotrade.options.repository.OptionsTradingPairRepository;
import com.cryptotrade.options.service.OptionsTradingPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionsTradingPairServiceImpl implements OptionsTradingPairService {
    
    @Autowired
    private OptionsTradingPairRepository repository;
    
    @Override
    @Transactional
    public OptionsTradingPair createTradingPair(OptionsTradingPair tradingPair) {
        if (repository.existsByPairName(tradingPair.getPairName())) {
            throw new RuntimeException("交易对已存在: " + tradingPair.getPairName());
        }
        return repository.save(tradingPair);
    }
    
    @Override
    @Transactional
    public OptionsTradingPair updateTradingPair(Long id, OptionsTradingPair tradingPair) {
        OptionsTradingPair existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("交易对不存在"));
        if (!existing.getPairName().equals(tradingPair.getPairName()) 
                && repository.existsByPairName(tradingPair.getPairName())) {
            throw new RuntimeException("交易对名称已存在: " + tradingPair.getPairName());
        }
        tradingPair.setId(id);
        tradingPair.setCreatedAt(existing.getCreatedAt());
        return repository.save(tradingPair);
    }
    
    @Override
    @Transactional
    public void deleteTradingPair(Long id) {
        repository.deleteById(id);
    }
    
    @Override
    public OptionsTradingPair getTradingPairById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public OptionsTradingPair getTradingPairByPairName(String pairName) {
        return repository.findByPairName(pairName).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public List<OptionsTradingPair> getAllTradingPairs() {
        return repository.findAll();
    }
    
    @Override
    public List<OptionsTradingPair> getActiveTradingPairs() {
        return repository.findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    }
    
    @Override
    public List<OptionsTradingPair> getVisibleTradingPairs() {
        return repository.findByVisibleTrueOrderBySortOrderAsc();
    }
}














