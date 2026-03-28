/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.service.impl;

import com.cryptotrade.deliverycontract.entity.DeliveryContractTradingPair;
import com.cryptotrade.deliverycontract.repository.DeliveryContractTradingPairRepository;
import com.cryptotrade.deliverycontract.service.DeliveryContractTradingPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeliveryContractTradingPairServiceImpl implements DeliveryContractTradingPairService {
    
    @Autowired
    private DeliveryContractTradingPairRepository repository;
    
    @Override
    @Transactional
    public DeliveryContractTradingPair createTradingPair(DeliveryContractTradingPair tradingPair) {
        if (repository.existsByPairName(tradingPair.getPairName())) {
            throw new RuntimeException("交易对已存在: " + tradingPair.getPairName());
        }
        return repository.save(tradingPair);
    }
    
    @Override
    @Transactional
    public DeliveryContractTradingPair updateTradingPair(Long id, DeliveryContractTradingPair tradingPair) {
        DeliveryContractTradingPair existing = repository.findById(id)
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
    public DeliveryContractTradingPair getTradingPairById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public DeliveryContractTradingPair getTradingPairByPairName(String pairName) {
        return repository.findByPairName(pairName).orElseThrow(() -> new RuntimeException("交易对不存在"));
    }
    
    @Override
    public List<DeliveryContractTradingPair> getAllTradingPairs() {
        return repository.findAll();
    }
    
    @Override
    public List<DeliveryContractTradingPair> getActiveTradingPairs() {
        return repository.findByEnabledTrueAndTradeableTrueOrderBySortOrderAsc();
    }
    
    @Override
    public List<DeliveryContractTradingPair> getVisibleTradingPairs() {
        return repository.findByVisibleTrueOrderBySortOrderAsc();
    }
}














