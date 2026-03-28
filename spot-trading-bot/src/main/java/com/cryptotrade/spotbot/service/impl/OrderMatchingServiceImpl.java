/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.service.impl;

import com.cryptotrade.spotbot.dto.request.MatchOrderRequest;
import com.cryptotrade.spotbot.dto.response.MatchOrderResponse;
import com.cryptotrade.spotbot.entity.SpotTradingBotConfig;
import com.cryptotrade.spotbot.repository.BotTradeRecordRepository;
import com.cryptotrade.spotbot.repository.SpotTradingBotConfigRepository;
import com.cryptotrade.spotbot.service.OrderMatchingService;
import com.cryptotrade.spotbot.document.BotTradeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderMatchingServiceImpl implements OrderMatchingService {
    
    @Autowired
    private SpotTradingBotConfigRepository configRepository;
    
    @Autowired
    private BotTradeRecordRepository botTradeRecordRepository;
    
    // TODO: 注入现货订单服务
    // @Autowired
    // private SpotOrderService spotOrderService;
    
    @Override
    @Transactional
    public MatchOrderResponse matchUserOrder(MatchOrderRequest request) {
        MatchOrderResponse response = new MatchOrderResponse();
        
        // 获取机器人配置
        SpotTradingBotConfig config = configRepository.findByPairName(request.getPairName())
                .orElseThrow(() -> new RuntimeException("该交易对的机器人配置不存在"));
        
        if (!"ACTIVE".equals(config.getStatus())) {
            throw new RuntimeException("机器人未激活");
        }
        
        // 根据订单类型进行撮合
        if ("MARKET".equals(request.getOrderType())) {
            // 市价单 - 立即撮合
            return matchMarketOrder(request, config);
        } else if ("LIMIT".equals(request.getOrderType())) {
            // 限价单 - 检查价格是否匹配
            return matchLimitOrder(request, config);
        } else {
            throw new RuntimeException("不支持的订单类型: " + request.getOrderType());
        }
    }
    
    private MatchOrderResponse matchMarketOrder(MatchOrderRequest request, SpotTradingBotConfig config) {
        MatchOrderResponse response = new MatchOrderResponse();
        
        BigDecimal matchedPrice = config.getCurrentPrice();
        BigDecimal matchedQuantity = request.getQuantity();
        BigDecimal matchedAmount = matchedPrice.multiply(matchedQuantity)
                .setScale(8, RoundingMode.HALF_UP);
        
        // 创建机器人交易记录
        BotTradeRecord record = new BotTradeRecord();
        record.setId(UUID.randomUUID().toString());
        record.setPairName(request.getPairName());
        record.setBaseCurrency(config.getBaseCurrency());
        record.setQuoteCurrency(config.getQuoteCurrency());
        record.setSide("BUY".equals(request.getSide()) ? "SELL" : "BUY"); // 机器人方向与用户相反
        record.setPrice(matchedPrice);
        record.setQuantity(matchedQuantity);
        record.setAmount(matchedAmount);
        record.setOrderType("MARKET");
        record.setBotOrderId(UUID.randomUUID().toString());
        record.setUserOrderId(request.getUserOrderId());
        record.setIsMatchedWithUser(true);
        record.setUserId(request.getUserId());
        record.setTradeTime(LocalDateTime.now());
        record.setCreatedAt(LocalDateTime.now());
        record.setDateHour(LocalDateTime.now().toString().substring(0, 13).replaceAll("[^0-9]", ""));
        
        botTradeRecordRepository.save(record);
        
        // TODO: 更新用户订单状态
        // spotOrderService.updateOrderStatus(request.getUserOrderId(), "FILLED");
        
        response.setSuccess(true);
        response.setBotOrderId(record.getBotOrderId());
        response.setUserOrderId(request.getUserOrderId());
        response.setMatchedPrice(matchedPrice);
        response.setMatchedQuantity(matchedQuantity);
        response.setMatchedAmount(matchedAmount);
        response.setMatchedTime(record.getTradeTime());
        response.setMessage("订单撮合成功");
        
        return response;
    }
    
    private MatchOrderResponse matchLimitOrder(MatchOrderRequest request, SpotTradingBotConfig config) {
        MatchOrderResponse response = new MatchOrderResponse();
        
        if (request.getPrice() == null) {
            throw new RuntimeException("限价单必须指定价格");
        }
        
        BigDecimal userPrice = request.getPrice();
        BigDecimal currentPrice = config.getCurrentPrice();
        
        // 检查价格是否在合理范围内
        BigDecimal priceDiff = config.getMaxPriceDiff();
        if ("RATIO".equals(config.getPriceDiffType())) {
            priceDiff = currentPrice.multiply(config.getMaxPriceDiff())
                    .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        }
        
        // 买入单：用户价格 >= 当前价格 - 差价
        // 卖出单：用户价格 <= 当前价格 + 差价
        boolean canMatch = false;
        if ("BUY".equals(request.getSide())) {
            canMatch = userPrice.compareTo(currentPrice.subtract(priceDiff)) >= 0;
        } else {
            canMatch = userPrice.compareTo(currentPrice.add(priceDiff)) <= 0;
        }
        
        if (!canMatch) {
            response.setSuccess(false);
            response.setMessage("价格不在合理范围内，无法撮合");
            return response;
        }
        
        // 可以撮合
        BigDecimal matchedPrice = userPrice;
        BigDecimal matchedQuantity = request.getQuantity();
        BigDecimal matchedAmount = matchedPrice.multiply(matchedQuantity)
                .setScale(8, RoundingMode.HALF_UP);
        
        // 创建机器人交易记录
        BotTradeRecord record = new BotTradeRecord();
        record.setId(UUID.randomUUID().toString());
        record.setPairName(request.getPairName());
        record.setBaseCurrency(config.getBaseCurrency());
        record.setQuoteCurrency(config.getQuoteCurrency());
        record.setSide("BUY".equals(request.getSide()) ? "SELL" : "BUY");
        record.setPrice(matchedPrice);
        record.setQuantity(matchedQuantity);
        record.setAmount(matchedAmount);
        record.setOrderType("LIMIT");
        record.setBotOrderId(UUID.randomUUID().toString());
        record.setUserOrderId(request.getUserOrderId());
        record.setIsMatchedWithUser(true);
        record.setUserId(request.getUserId());
        record.setTradeTime(LocalDateTime.now());
        record.setCreatedAt(LocalDateTime.now());
        record.setDateHour(LocalDateTime.now().toString().substring(0, 13).replaceAll("[^0-9]", ""));
        
        botTradeRecordRepository.save(record);
        
        // TODO: 更新用户订单状态
        // spotOrderService.updateOrderStatus(request.getUserOrderId(), "FILLED");
        
        response.setSuccess(true);
        response.setBotOrderId(record.getBotOrderId());
        response.setUserOrderId(request.getUserOrderId());
        response.setMatchedPrice(matchedPrice);
        response.setMatchedQuantity(matchedQuantity);
        response.setMatchedAmount(matchedAmount);
        response.setMatchedTime(record.getTradeTime());
        response.setMessage("订单撮合成功");
        
        return response;
    }
    
    @Override
    public boolean generateBotOrder(String pairName) {
        // 获取机器人配置
        SpotTradingBotConfig config = configRepository.findByPairName(pairName)
                .orElseThrow(() -> new RuntimeException("该交易对的机器人配置不存在"));
        
        if (!"ACTIVE".equals(config.getStatus())) {
            return false;
        }
        
        // 生成随机买卖方向
        String side = Math.random() > 0.5 ? "BUY" : "SELL";
        
        // 生成价格（在当前价格附近波动）
        BigDecimal price = generateBotPrice(config, side);
        
        // 生成数量
        BigDecimal quantity = generateBotQuantity(config);
        
        // 创建机器人交易记录（无用户订单）
        BotTradeRecord record = new BotTradeRecord();
        record.setId(UUID.randomUUID().toString());
        record.setPairName(pairName);
        record.setBaseCurrency(config.getBaseCurrency());
        record.setQuoteCurrency(config.getQuoteCurrency());
        record.setSide(side);
        record.setPrice(price);
        record.setQuantity(quantity);
        record.setAmount(price.multiply(quantity).setScale(8, RoundingMode.HALF_UP));
        record.setOrderType("MARKET");
        record.setBotOrderId(UUID.randomUUID().toString());
        record.setIsMatchedWithUser(false);
        record.setTradeTime(LocalDateTime.now());
        record.setCreatedAt(LocalDateTime.now());
        record.setDateHour(LocalDateTime.now().toString().substring(0, 13).replaceAll("[^0-9]", ""));
        
        botTradeRecordRepository.save(record);
        
        return true;
    }
    
    private BigDecimal generateBotPrice(SpotTradingBotConfig config, String side) {
        BigDecimal currentPrice = config.getCurrentPrice();
        BigDecimal priceDiff = config.getMaxPriceDiff();
        
        if ("RATIO".equals(config.getPriceDiffType())) {
            priceDiff = currentPrice.multiply(config.getMaxPriceDiff())
                    .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        }
        
        // 在合理范围内随机生成价格
        double randomFactor = Math.random();
        BigDecimal price;
        if ("BUY".equals(side)) {
            // 买盘价格：当前价格 - 差价 到 当前价格
            price = currentPrice.subtract(priceDiff.multiply(new BigDecimal(String.valueOf(randomFactor))));
        } else {
            // 卖盘价格：当前价格 到 当前价格 + 差价
            price = currentPrice.add(priceDiff.multiply(new BigDecimal(String.valueOf(randomFactor))));
        }
        
        return price.setScale(config.getPricePrecision(), RoundingMode.HALF_UP);
    }
    
    private BigDecimal generateBotQuantity(SpotTradingBotConfig config) {
        // 基于初始订单数量和随机因子生成数量
        java.util.Random random = new java.util.Random();
        double rand = random.nextDouble() * 100;
        
        BigDecimal factor;
        if (rand < getFactorValue(config.getVolumeRandomFactor1())) {
            factor = config.getVolumeRandomFactor1();
        } else if (rand < getFactorValue(config.getVolumeRandomFactor1()) + getFactorValue(config.getVolumeRandomFactor2())) {
            factor = config.getVolumeRandomFactor2();
        } else if (rand < getFactorValue(config.getVolumeRandomFactor1()) + getFactorValue(config.getVolumeRandomFactor2()) + 
                   getFactorValue(config.getVolumeRandomFactor3())) {
            factor = config.getVolumeRandomFactor3();
        } else if (rand < getFactorValue(config.getVolumeRandomFactor1()) + getFactorValue(config.getVolumeRandomFactor2()) + 
                   getFactorValue(config.getVolumeRandomFactor3()) + getFactorValue(config.getVolumeRandomFactor4())) {
            factor = config.getVolumeRandomFactor4();
        } else if (rand < getFactorValue(config.getVolumeRandomFactor1()) + getFactorValue(config.getVolumeRandomFactor2()) + 
                   getFactorValue(config.getVolumeRandomFactor3()) + getFactorValue(config.getVolumeRandomFactor4()) + 
                   getFactorValue(config.getVolumeRandomFactor5())) {
            factor = config.getVolumeRandomFactor5();
        } else if (rand < getFactorValue(config.getVolumeRandomFactor1()) + getFactorValue(config.getVolumeRandomFactor2()) + 
                   getFactorValue(config.getVolumeRandomFactor3()) + getFactorValue(config.getVolumeRandomFactor4()) + 
                   getFactorValue(config.getVolumeRandomFactor5()) + getFactorValue(config.getVolumeRandomFactor6())) {
            factor = config.getVolumeRandomFactor6();
        } else {
            factor = config.getVolumeRandomFactor7();
        }
        
        BigDecimal quantity = config.getInitialOrderQuantity()
                .multiply(factor != null ? factor : new BigDecimal("1"))
                .divide(new BigDecimal("100"), config.getQuantityPrecision(), RoundingMode.HALF_UP);
        
        if (quantity.compareTo(config.getMinTradeQuantity()) < 0) {
            quantity = config.getMinTradeQuantity();
        }
        
        return quantity;
    }
    
    private double getFactorValue(BigDecimal factor) {
        return factor != null ? factor.doubleValue() : 0.0;
    }
    
    @Override
    public void startBot(String pairName) {
        // TODO: 启动定时任务，按照配置的时间间隔生成订单
        // 可以使用Spring的@Scheduled或ScheduledExecutorService
        // 建议使用ScheduledExecutorService管理每个交易对的独立定时任务
    }
    
    @Override
    public void stopBot(String pairName) {
        // TODO: 停止定时任务
        // 停止指定交易对的定时任务
    }
}

