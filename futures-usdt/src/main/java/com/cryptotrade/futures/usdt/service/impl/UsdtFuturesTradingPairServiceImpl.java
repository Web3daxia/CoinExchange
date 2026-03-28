/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.service.impl;

import com.cryptotrade.futures.usdt.entity.UsdtFuturesTradingPair;
import com.cryptotrade.futures.usdt.repository.UsdtFuturesTradingPairRepository;
import com.cryptotrade.futures.usdt.service.UsdtFuturesTradingPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsdtFuturesTradingPairServiceImpl implements UsdtFuturesTradingPairService {
    
    @Autowired
    private UsdtFuturesTradingPairRepository repository;
    
    @Override
    @Transactional
    public UsdtFuturesTradingPair createTradingPair(UsdtFuturesTradingPair tradingPair) {
        if (repository.existsByPairName(tradingPair.getPairName())) {
            throw new RuntimeException("交易对已存在: " + tradingPair.getPairName());
        }
        return repository.save(tradingPair);
    }
    
    @Override
    @Transactional
    public UsdtFuturesTradingPair updateTradingPair(Long id, UsdtFuturesTradingPair tradingPair) {
        UsdtFuturesTradingPair existing = repository.findById(id)
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
    public UsdtFuturesTradingPair getTradingPairById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public UsdtFuturesTradingPair getTradingPairByPairName(String pairName) {
        return repository.findByPairName(pairName).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public List<UsdtFuturesTradingPair> getAllTradingPairs() {
        return repository.findAll();
    }
    
    @Override
    public List<UsdtFuturesTradingPair> getActiveTradingPairs() {
        return repository.findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    }
    
    @Override
    public List<UsdtFuturesTradingPair> getVisibleTradingPairs() {
        return repository.findByVisibleTrueOrderBySortOrderAsc();
    }
}














