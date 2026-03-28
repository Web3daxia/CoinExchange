/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.service.impl;

import com.cryptotrade.leveraged.entity.LeveragedTradingPair;
import com.cryptotrade.leveraged.repository.LeveragedTradingPairRepository;
import com.cryptotrade.leveraged.service.LeveragedTradingPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeveragedTradingPairServiceImpl implements LeveragedTradingPairService {
    
    @Autowired
    private LeveragedTradingPairRepository repository;
    
    @Override
    @Transactional
    public LeveragedTradingPair createTradingPair(LeveragedTradingPair tradingPair) {
        if (repository.existsByPairName(tradingPair.getPairName())) {
            throw new RuntimeException("交易对已存在: " + tradingPair.getPairName());
        }
        return repository.save(tradingPair);
    }
    
    @Override
    @Transactional
    public LeveragedTradingPair updateTradingPair(Long id, LeveragedTradingPair tradingPair) {
        LeveragedTradingPair existing = repository.findById(id)
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
    public LeveragedTradingPair getTradingPairById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public LeveragedTradingPair getTradingPairByPairName(String pairName) {
        return repository.findByPairName(pairName).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public List<LeveragedTradingPair> getAllTradingPairs() {
        return repository.findAll();
    }
    
    @Override
    public List<LeveragedTradingPair> getActiveTradingPairs() {
        return repository.findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    }
    
    @Override
    public List<LeveragedTradingPair> getVisibleTradingPairs() {
        return repository.findByVisibleTrueOrderBySortOrderAsc();
    }
}














