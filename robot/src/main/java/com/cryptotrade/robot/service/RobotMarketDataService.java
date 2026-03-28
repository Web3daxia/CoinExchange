/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.robot.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 机器人市场数据服务接口
 * 用于获取市场数据（价格、K线等）
 */
public interface RobotMarketDataService {
    /**
     * 获取当前价格
     */
    BigDecimal getCurrentPrice(String pairName, String marketType);

    /**
     * 获取K线数据
     */
    List<Map<String, Object>> getKlineData(String pairName, String marketType, String interval, Integer limit);

    /**
     * 获取市场深度
     */
    Map<String, Object> getMarketDepth(String pairName, String marketType, Integer limit);

    /**
     * 获取24小时行情数据
     */
    Map<String, Object> get24hrTicker(String pairName, String marketType);
}













