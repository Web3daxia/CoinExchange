/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service.impl;

import com.cryptotrade.spot.dto.request.MarketAlertRequest;
import com.cryptotrade.spot.entity.MarketAlert;
import com.cryptotrade.spot.entity.TradingPair;
import com.cryptotrade.spot.repository.MarketAlertRepository;
import com.cryptotrade.spot.repository.TradingPairRepository;
import com.cryptotrade.spot.service.MarketAlertService;
import com.cryptotrade.spot.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MarketAlertServiceImpl implements MarketAlertService {

    @Autowired
    private MarketAlertRepository marketAlertRepository;

    @Autowired
    private TradingPairRepository tradingPairRepository;

    @Autowired
    private MarketDataService marketDataService;

    @Override
    @Transactional
    public MarketAlert setAlert(Long userId, MarketAlertRequest request) {
        // 验证交易对是否存在
        TradingPair tradingPair = tradingPairRepository.findByPairName(request.getPairName())
                .orElseThrow(() -> new RuntimeException("交易对不存在"));

        // 获取当前市场价格作为基准价格
        BigDecimal basePrice = tradingPair.getCurrentPrice();
        BigDecimal baseVolume = tradingPair.getVolume24h();

        // 创建提醒
        MarketAlert alert = new MarketAlert();
        alert.setUserId(userId);
        alert.setPairName(request.getPairName());
        alert.setAlertType(request.getAlertType());
        alert.setConditionType(request.getConditionType());
        alert.setThresholdValue(request.getThresholdValue());
        alert.setIsEnabled(true);
        alert.setIsTriggered(false);
        alert.setBasePrice(basePrice);
        alert.setBaseVolume(baseVolume != null ? baseVolume : BigDecimal.ZERO);

        return marketAlertRepository.save(alert);
    }

    @Override
    public List<MarketAlert> getUserAlerts(Long userId) {
        return marketAlertRepository.findByUserIdAndIsEnabledTrue(userId);
    }

    @Override
    public MarketAlert getAlertStatus(Long userId, Long alertId) {
        MarketAlert alert = marketAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("提醒不存在"));

        if (!alert.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看此提醒");
        }

        return alert;
    }

    @Override
    @Transactional
    public void removeAlert(Long userId, Long alertId) {
        MarketAlert alert = marketAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("提醒不存在"));

        if (!alert.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此提醒");
        }

        marketAlertRepository.delete(alert);
    }

    @Override
    @Transactional
    public void toggleAlert(Long userId, Long alertId, boolean enabled) {
        MarketAlert alert = marketAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("提醒不存在"));

        if (!alert.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作此提醒");
        }

        alert.setIsEnabled(enabled);
        marketAlertRepository.save(alert);
    }

    @Override
    @Transactional
    public void checkAndTriggerAlerts(String pairName) {
        List<MarketAlert> alerts = marketAlertRepository.findByPairNameAndIsEnabledTrue(pairName);

        if (alerts.isEmpty()) {
            return;
        }

        try {
            // 获取当前市场数据
            TradingPair tradingPair = tradingPairRepository.findByPairName(pairName)
                    .orElse(null);
            
            if (tradingPair == null || tradingPair.getCurrentPrice() == null) {
                return;
            }

            BigDecimal currentPrice = tradingPair.getCurrentPrice();
            BigDecimal currentVolume = tradingPair.getVolume24h() != null ? tradingPair.getVolume24h() : BigDecimal.ZERO;

            for (MarketAlert alert : alerts) {
                if (alert.getIsTriggered()) {
                    continue; // 已经触发过的提醒不再检查
                }

                boolean shouldTrigger = false;

                if ("PRICE_CHANGE".equals(alert.getAlertType())) {
                    shouldTrigger = checkPriceCondition(currentPrice, alert);
                } else if ("VOLUME_CHANGE".equals(alert.getAlertType())) {
                    shouldTrigger = checkVolumeCondition(currentVolume, alert);
                } else if ("LARGE_ORDER".equals(alert.getAlertType())) {
                    // 大单监控需要从订单簿中检查，这里简化处理
                    shouldTrigger = checkLargeOrderCondition(pairName, alert);
                }

                if (shouldTrigger) {
                    alert.setIsTriggered(true);
                    alert.setTriggeredAt(LocalDateTime.now());
                    marketAlertRepository.save(alert);

                    // 发送通知给用户
                    notifyUser(alert, currentPrice, currentVolume);
                }
            }
        } catch (Exception e) {
            // 记录错误日志，但不影响其他提醒的检查
            System.err.println("检查提醒时出错: " + e.getMessage());
        }
    }

    /**
     * 检查价格条件
     */
    private boolean checkPriceCondition(BigDecimal currentPrice, MarketAlert alert) {
        BigDecimal threshold = alert.getThresholdValue();
        if (threshold == null) {
            return false;
        }

        if ("ABOVE".equals(alert.getConditionType())) {
            return currentPrice.compareTo(threshold) >= 0;
        } else if ("BELOW".equals(alert.getConditionType())) {
            return currentPrice.compareTo(threshold) <= 0;
        } else if ("PERCENTAGE_CHANGE".equals(alert.getConditionType())) {
            // 百分比变化：计算相对于基准价格的变化百分比
            if (alert.getBasePrice() == null || alert.getBasePrice().compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }
            BigDecimal priceChange = currentPrice.subtract(alert.getBasePrice())
                    .divide(alert.getBasePrice(), 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")); // 转换为百分比
            BigDecimal absChange = priceChange.abs();
            // 如果价格变化百分比（绝对值）超过阈值，触发提醒
            return absChange.compareTo(threshold) >= 0;
        }

        return false;
    }

    /**
     * 检查成交量条件
     */
    private boolean checkVolumeCondition(BigDecimal currentVolume, MarketAlert alert) {
        BigDecimal threshold = alert.getThresholdValue();
        if (threshold == null) {
            return false;
        }

        if ("ABOVE".equals(alert.getConditionType())) {
            return currentVolume.compareTo(threshold) >= 0;
        } else if ("BELOW".equals(alert.getConditionType())) {
            return currentVolume.compareTo(threshold) <= 0;
        } else if ("PERCENTAGE_CHANGE".equals(alert.getConditionType())) {
            // 成交量百分比变化
            if (alert.getBaseVolume() == null || alert.getBaseVolume().compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }
            BigDecimal volumeChange = currentVolume.subtract(alert.getBaseVolume())
                    .divide(alert.getBaseVolume(), 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")); // 转换为百分比
            BigDecimal absChange = volumeChange.abs();
            return absChange.compareTo(threshold) >= 0;
        }

        return false;
    }

    /**
     * 检查大单条件
     */
    private boolean checkLargeOrderCondition(String pairName, MarketAlert alert) {
        try {
            // 获取市场深度数据
            Map<String, Object> depthData = marketDataService.getMarketDepth(pairName, 100);
            List<Map<String, Object>> bids = (List<Map<String, Object>>) depthData.get("bids");
            List<Map<String, Object>> asks = (List<Map<String, Object>>) depthData.get("asks");

            BigDecimal threshold = alert.getThresholdValue();
            if (threshold == null) {
                return false;
            }

            // 检查买盘大单
            if (bids != null) {
                for (Map<String, Object> bid : bids) {
                    BigDecimal quantity = new BigDecimal(bid.get("quantity").toString());
                    if (quantity.compareTo(threshold) >= 0) {
                        return true;
                    }
                }
            }

            // 检查卖盘大单
            if (asks != null) {
                for (Map<String, Object> ask : asks) {
                    BigDecimal quantity = new BigDecimal(ask.get("quantity").toString());
                    if (quantity.compareTo(threshold) >= 0) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            System.err.println("检查大单条件失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 通知用户
     */
    private void notifyUser(MarketAlert alert, BigDecimal currentPrice, BigDecimal currentVolume) {
        try {
            // TODO: 实现通知逻辑
            // 1. 站内通知（保存到通知表）
            // 2. 邮件通知
            // 3. 短信通知
            // 4. 推送通知

            String message = buildNotificationMessage(alert, currentPrice, currentVolume);
            System.out.println("提醒通知: 用户ID=" + alert.getUserId() + ", " + message);

            // 这里可以集成通知服务
            // notificationService.sendNotification(alert.getUserId(), message);
        } catch (Exception e) {
            System.err.println("发送通知失败: " + e.getMessage());
        }
    }

    /**
     * 构建通知消息
     */
    private String buildNotificationMessage(MarketAlert alert, BigDecimal currentPrice, BigDecimal currentVolume) {
        StringBuilder message = new StringBuilder();
        message.append("交易对: ").append(alert.getPairName()).append(", ");
        message.append("提醒类型: ").append(alert.getAlertType()).append(", ");

        if ("PRICE_CHANGE".equals(alert.getAlertType())) {
            message.append("当前价格: ").append(currentPrice).append(", ");
            if (alert.getBasePrice() != null) {
                BigDecimal changePercent = currentPrice.subtract(alert.getBasePrice())
                        .divide(alert.getBasePrice(), 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                message.append("变化: ").append(changePercent).append("%");
            }
        } else if ("VOLUME_CHANGE".equals(alert.getAlertType())) {
            message.append("当前成交量: ").append(currentVolume).append(", ");
            if (alert.getBaseVolume() != null && alert.getBaseVolume().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal changePercent = currentVolume.subtract(alert.getBaseVolume())
                        .divide(alert.getBaseVolume(), 4, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                message.append("变化: ").append(changePercent).append("%");
            }
        } else if ("LARGE_ORDER".equals(alert.getAlertType())) {
            message.append("检测到大单: ").append(alert.getThresholdValue());
        }

        return message.toString();
    }
}


