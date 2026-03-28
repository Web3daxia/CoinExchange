/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.service.impl;

import com.cryptotrade.spotbot.dto.request.BotConfigRequest;
import com.cryptotrade.spotbot.dto.response.BotSimulationResponse;
import com.cryptotrade.spotbot.dto.response.OrderBookItem;
import com.cryptotrade.spotbot.entity.SpotTradingBotConfig;
import com.cryptotrade.spotbot.repository.SpotTradingBotConfigRepository;
import com.cryptotrade.spotbot.service.SpotTradingBotConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SpotTradingBotConfigServiceImpl implements SpotTradingBotConfigService {
    
    @Autowired
    private SpotTradingBotConfigRepository configRepository;
    
    @Override
    @Transactional
    public SpotTradingBotConfig createConfig(BotConfigRequest request) {
        if (configRepository.existsByPairName(request.getPairName())) {
            throw new RuntimeException("该交易对的机器人配置已存在: " + request.getPairName());
        }
        
        SpotTradingBotConfig config = new SpotTradingBotConfig();
        BeanUtils.copyProperties(request, config);
        config.setStatus("ACTIVE");
        
        return configRepository.save(config);
    }
    
    @Override
    @Transactional
    public SpotTradingBotConfig updateConfig(Long configId, BotConfigRequest request) {
        SpotTradingBotConfig config = configRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("机器人配置不存在"));
        
        BeanUtils.copyProperties(request, config, "id", "pairName", "createdAt");
        
        return configRepository.save(config);
    }
    
    @Override
    @Transactional
    public void deleteConfig(Long configId) {
        configRepository.deleteById(configId);
    }
    
    @Override
    public SpotTradingBotConfig getConfigById(Long configId) {
        return configRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("机器人配置不存在"));
    }
    
    @Override
    public SpotTradingBotConfig getConfigByPairName(String pairName) {
        return configRepository.findByPairName(pairName)
                .orElseThrow(() -> new RuntimeException("机器人配置不存在"));
    }
    
    @Override
    public List<SpotTradingBotConfig> getAllConfigs() {
        return configRepository.findAll();
    }
    
    @Override
    public List<SpotTradingBotConfig> getActiveConfigs() {
        return configRepository.findByStatus("ACTIVE");
    }
    
    @Override
    public BotSimulationResponse simulateConfig(BotConfigRequest request) {
        return doSimulate(request);
    }
    
    @Override
    public BotSimulationResponse simulateConfigById(Long configId) {
        SpotTradingBotConfig config = getConfigById(configId);
        BotConfigRequest request = new BotConfigRequest();
        BeanUtils.copyProperties(config, request);
        return doSimulate(request);
    }
    
    private BotSimulationResponse doSimulate(BotConfigRequest request) {
        BotSimulationResponse response = new BotSimulationResponse();
        
        BigDecimal currentPrice = request.getCurrentPrice() != null ? 
            request.getCurrentPrice() : BigDecimal.ZERO;
        
        if (currentPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("当前价格无效");
        }
        
        // 计算买卖盘价格范围
        BigDecimal priceDiff = request.getMaxPriceDiff();
        if ("RATIO".equals(request.getPriceDiffType())) {
            // 比例差价
            priceDiff = currentPrice.multiply(request.getMaxPriceDiff()).divide(
                new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        }
        
        // 卖盘数据（SELL - 价格更高）
        BigDecimal sellMaxPrice = currentPrice.add(priceDiff);
        BigDecimal sellMinPrice = currentPrice;
        BigDecimal sellPriceDiff = priceDiff;
        BigDecimal sellPriceDiffPercent = priceDiff.divide(currentPrice, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        
        // 买盘数据（BUY - 价格更低）
        BigDecimal buyMaxPrice = currentPrice;
        BigDecimal buyMinPrice = currentPrice.subtract(priceDiff);
        BigDecimal buyPriceDiff = priceDiff;
        BigDecimal buyPriceDiffPercent = priceDiff.divide(currentPrice, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        
        // 生成50条盘口数据
        List<OrderBookItem> orderBookItems = generateOrderBook(request, currentPrice, 
                sellMinPrice, sellMaxPrice, buyMinPrice, buyMaxPrice);
        
        // 计算卖盘总量和金额
        BigDecimal sellCoinQuantity = orderBookItems.stream()
                .filter(item -> "SELL".equals(item.getSide()))
                .map(OrderBookItem::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal sellAmount = orderBookItems.stream()
                .filter(item -> "SELL".equals(item.getSide()))
                .map(OrderBookItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算买盘总量和金额
        BigDecimal buyCoinQuantity = orderBookItems.stream()
                .filter(item -> "BUY".equals(item.getSide()))
                .map(OrderBookItem::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal buyAmount = orderBookItems.stream()
                .filter(item -> "BUY".equals(item.getSide()))
                .map(OrderBookItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 预估数据（平均和安全值）
        BigDecimal avgTradeCoin = sellCoinQuantity.add(buyCoinQuantity).divide(new BigDecimal("2"), 8, RoundingMode.HALF_UP);
        BigDecimal safeTradeCoin = avgTradeCoin.multiply(new BigDecimal("0.9")); // 安全值取90%
        BigDecimal avgSettlementCoin = sellAmount.add(buyAmount).divide(new BigDecimal("2"), 8, RoundingMode.HALF_UP);
        BigDecimal safeSettlementCoin = avgSettlementCoin.multiply(new BigDecimal("0.9")); // 安全值取90%
        
        response.setEstimatedTradeCoinAverage(avgTradeCoin);
        response.setEstimatedTradeCoinSafe(safeTradeCoin);
        response.setEstimatedSettlementCoinAverage(avgSettlementCoin);
        response.setEstimatedSettlementCoinSafe(safeSettlementCoin);
        
        response.setSellMaxPrice(sellMaxPrice);
        response.setSellMinPrice(sellMinPrice);
        response.setSellPriceDiff(sellPriceDiff);
        response.setSellPriceDiffPercent(sellPriceDiffPercent);
        response.setSellCoinQuantity(sellCoinQuantity);
        response.setSellAmount(sellAmount);
        
        response.setBuyMaxPrice(buyMaxPrice);
        response.setBuyMinPrice(buyMinPrice);
        response.setBuyPriceDiff(buyPriceDiff);
        response.setBuyPriceDiffPercent(buyPriceDiffPercent);
        response.setBuyCoinQuantity(buyCoinQuantity);
        response.setBuyAmount(buyAmount);
        
        response.setOrderBookItems(orderBookItems);
        
        return response;
    }
    
    private List<OrderBookItem> generateOrderBook(BotConfigRequest request, BigDecimal currentPrice,
                                                   BigDecimal sellMinPrice, BigDecimal sellMaxPrice,
                                                   BigDecimal buyMinPrice, BigDecimal buyMaxPrice) {
        List<OrderBookItem> items = new ArrayList<>();
        Random random = new Random();
        
        // 生成25条卖盘数据（从最低到最高）
        BigDecimal sellStep = sellMaxPrice.subtract(sellMinPrice).divide(
                new BigDecimal("25"), request.getPricePrecision(), RoundingMode.HALF_UP);
        
        for (int i = 0; i < 25; i++) {
            OrderBookItem item = new OrderBookItem();
            item.setSide("SELL");
            BigDecimal price = sellMinPrice.add(sellStep.multiply(new BigDecimal(i + 1)));
            price = price.setScale(request.getPricePrecision(), RoundingMode.HALF_UP);
            item.setPrice(price);
            
            // 使用随机因子生成数量
            BigDecimal quantity = generateQuantity(request, random);
            item.setQuantity(quantity);
            item.setAmount(price.multiply(quantity).setScale(8, RoundingMode.HALF_UP));
            item.setIndex(i + 1);
            items.add(item);
        }
        
        // 生成25条买盘数据（从最高到最低）
        BigDecimal buyStep = buyMaxPrice.subtract(buyMinPrice).divide(
                new BigDecimal("25"), request.getPricePrecision(), RoundingMode.HALF_UP);
        
        for (int i = 0; i < 25; i++) {
            OrderBookItem item = new OrderBookItem();
            item.setSide("BUY");
            BigDecimal price = buyMaxPrice.subtract(buyStep.multiply(new BigDecimal(i + 1)));
            price = price.setScale(request.getPricePrecision(), RoundingMode.HALF_UP);
            item.setPrice(price);
            
            // 使用随机因子生成数量
            BigDecimal quantity = generateQuantity(request, random);
            item.setQuantity(quantity);
            item.setAmount(price.multiply(quantity).setScale(8, RoundingMode.HALF_UP));
            item.setIndex(i + 1);
            items.add(item);
        }
        
        return items;
    }
    
    private BigDecimal generateQuantity(BotConfigRequest request, Random random) {
        // 使用随机因子生成数量
        double rand = random.nextDouble() * 100;
        
        BigDecimal factor;
        if (rand < getFactorValue(request.getVolumeRandomFactor1())) {
            factor = request.getVolumeRandomFactor1();
        } else if (rand < getFactorValue(request.getVolumeRandomFactor1()) + getFactorValue(request.getVolumeRandomFactor2())) {
            factor = request.getVolumeRandomFactor2();
        } else if (rand < getFactorValue(request.getVolumeRandomFactor1()) + getFactorValue(request.getVolumeRandomFactor2()) + 
                   getFactorValue(request.getVolumeRandomFactor3())) {
            factor = request.getVolumeRandomFactor3();
        } else if (rand < getFactorValue(request.getVolumeRandomFactor1()) + getFactorValue(request.getVolumeRandomFactor2()) + 
                   getFactorValue(request.getVolumeRandomFactor3()) + getFactorValue(request.getVolumeRandomFactor4())) {
            factor = request.getVolumeRandomFactor4();
        } else if (rand < getFactorValue(request.getVolumeRandomFactor1()) + getFactorValue(request.getVolumeRandomFactor2()) + 
                   getFactorValue(request.getVolumeRandomFactor3()) + getFactorValue(request.getVolumeRandomFactor4()) + 
                   getFactorValue(request.getVolumeRandomFactor5())) {
            factor = request.getVolumeRandomFactor5();
        } else if (rand < getFactorValue(request.getVolumeRandomFactor1()) + getFactorValue(request.getVolumeRandomFactor2()) + 
                   getFactorValue(request.getVolumeRandomFactor3()) + getFactorValue(request.getVolumeRandomFactor4()) + 
                   getFactorValue(request.getVolumeRandomFactor5()) + getFactorValue(request.getVolumeRandomFactor6())) {
            factor = request.getVolumeRandomFactor6();
        } else {
            factor = request.getVolumeRandomFactor7();
        }
        
        // 根据因子计算数量（基于初始订单数量）
        BigDecimal quantity = request.getInitialOrderQuantity()
                .multiply(factor != null ? factor : new BigDecimal("1"))
                .divide(new BigDecimal("100"), request.getQuantityPrecision(), RoundingMode.HALF_UP);
        
        // 确保不低于最低交易量
        if (quantity.compareTo(request.getMinTradeQuantity()) < 0) {
            quantity = request.getMinTradeQuantity();
        }
        
        return quantity;
    }
    
    private double getFactorValue(BigDecimal factor) {
        return factor != null ? factor.doubleValue() : 0.0;
    }
}














