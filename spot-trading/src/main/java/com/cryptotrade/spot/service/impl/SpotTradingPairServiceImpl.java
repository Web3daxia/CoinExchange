/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service.impl;

import com.cryptotrade.spot.entity.SpotTradingPair;
import com.cryptotrade.spot.repository.SpotTradingPairRepository;
import com.cryptotrade.spot.service.SpotTradingPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpotTradingPairServiceImpl implements SpotTradingPairService {
    
    @Autowired
    private SpotTradingPairRepository spotTradingPairRepository;
    
    @Override
    @Transactional
    public SpotTradingPair createTradingPair(SpotTradingPair tradingPair) {
        if (spotTradingPairRepository.existsByPairName(tradingPair.getPairName())) {
            throw new RuntimeException("交易对已存在: " + tradingPair.getPairName());
        }
        return spotTradingPairRepository.save(tradingPair);
    }
    
    @Override
    @Transactional
    public SpotTradingPair updateTradingPair(Long id, SpotTradingPair tradingPair) {
        SpotTradingPair existing = spotTradingPairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("交易对不存在"));
        
        // 检查交易对名称是否重复（如果修改了名称）
        if (!existing.getPairName().equals(tradingPair.getPairName()) 
                && spotTradingPairRepository.existsByPairName(tradingPair.getPairName())) {
            throw new RuntimeException("交易对名称已存在: " + tradingPair.getPairName());
        }
        
        tradingPair.setId(id);
        tradingPair.setCreatedAt(existing.getCreatedAt());
        
        return spotTradingPairRepository.save(tradingPair);
    }
    
    @Override
    @Transactional
    public void deleteTradingPair(Long id) {
        spotTradingPairRepository.deleteById(id);
    }
    
    @Override
    public SpotTradingPair getTradingPairById(Long id) {
        return spotTradingPairRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public SpotTradingPair getTradingPairByPairName(String pairName) {
        return spotTradingPairRepository.findByPairName(pairName)
                .orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public List<SpotTradingPair> getAllTradingPairs() {
        return spotTradingPairRepository.findAll();
    }
    
    @Override
    public List<SpotTradingPair> getActiveTradingPairs() {
        return spotTradingPairRepository.findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    }
    
    @Override
    public List<SpotTradingPair> getVisibleTradingPairs() {
        return spotTradingPairRepository.findByFrontendVisibleTrueOrderBySortOrderAsc();
    }
}














