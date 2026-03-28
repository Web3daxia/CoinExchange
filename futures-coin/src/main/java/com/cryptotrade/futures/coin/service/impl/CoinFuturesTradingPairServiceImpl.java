/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.service.impl;

import com.cryptotrade.futures.coin.entity.CoinFuturesTradingPair;
import com.cryptotrade.futures.coin.repository.CoinFuturesTradingPairRepository;
import com.cryptotrade.futures.coin.service.CoinFuturesTradingPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoinFuturesTradingPairServiceImpl implements CoinFuturesTradingPairService {
    
    @Autowired
    private CoinFuturesTradingPairRepository repository;
    
    @Override
    @Transactional
    public CoinFuturesTradingPair createTradingPair(CoinFuturesTradingPair tradingPair) {
        if (repository.existsByPairName(tradingPair.getPairName())) {
            throw new RuntimeException("交易对已存在: " + tradingPair.getPairName());
        }
        return repository.save(tradingPair);
    }
    
    @Override
    @Transactional
    public CoinFuturesTradingPair updateTradingPair(Long id, CoinFuturesTradingPair tradingPair) {
        CoinFuturesTradingPair existing = repository.findById(id)
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
    public CoinFuturesTradingPair getTradingPairById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public CoinFuturesTradingPair getTradingPairByPairName(String pairName) {
        return repository.findByPairName(pairName).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public List<CoinFuturesTradingPair> getAllTradingPairs() {
        return repository.findAll();
    }
    
    @Override
    public List<CoinFuturesTradingPair> getActiveTradingPairs() {
        return repository.findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    }
    
    @Override
    public List<CoinFuturesTradingPair> getVisibleTradingPairs() {
        return repository.findByVisibleTrueOrderBySortOrderAsc();
    }
}














