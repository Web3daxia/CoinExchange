/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.service;

import com.cryptotrade.spot.dto.request.MarketAlertRequest;
import com.cryptotrade.spot.entity.MarketAlert;

import java.util.List;

public interface MarketAlertService {
    /**
     * 设置市场异动提醒
     */
    MarketAlert setAlert(Long userId, MarketAlertRequest request);

    /**
     * 查询用户的提醒列表
     */
    List<MarketAlert> getUserAlerts(Long userId);

    /**
     * 查询提醒状态
     */
    MarketAlert getAlertStatus(Long userId, Long alertId);

    /**
     * 删除提醒
     */
    void removeAlert(Long userId, Long alertId);

    /**
     * 启用/禁用提醒
     */
    void toggleAlert(Long userId, Long alertId, boolean enabled);

    /**
     * 检查并触发提醒
     */
    void checkAndTriggerAlerts(String pairName);
}















