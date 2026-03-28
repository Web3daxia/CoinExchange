/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.util;

import java.util.Arrays;
import java.util.List;

/**
 * 订单类型常量定义
 * 包含现货和合约的所有委托类型
 */
public class OrderTypeConstants {
    
    // ========== 现货订单类型 ==========
    
    /**
     * 限价委托
     */
    public static final String SPOT_LIMIT = "LIMIT";
    
    /**
     * 市价委托
     */
    public static final String SPOT_MARKET = "MARKET";
    
    /**
     * 止损单
     */
    public static final String SPOT_STOP_LOSS = "STOP_LOSS";
    
    /**
     * 止盈单
     */
    public static final String SPOT_TAKE_PROFIT = "TAKE_PROFIT";
    
    /**
     * 止损限价单
     */
    public static final String SPOT_STOP_LOSS_LIMIT = "STOP_LOSS_LIMIT";
    
    /**
     * 止盈限价单
     */
    public static final String SPOT_TAKE_PROFIT_LIMIT = "TAKE_PROFIT_LIMIT";
    
    /**
     * 冰山订单
     */
    public static final String SPOT_ICEBERG = "ICEBERG";
    
    /**
     * TWAP订单（时间加权平均价格）
     */
    public static final String SPOT_TWAP = "TWAP";
    
    /**
     * 立即成交或取消（IOC）
     */
    public static final String SPOT_IOC = "IOC";
    
    /**
     * 全部成交或取消（FOK）
     */
    public static final String SPOT_FOK = "FOK";
    
    /**
     * 只做Maker（POST_ONLY）
     */
    public static final String SPOT_POST_ONLY = "POST_ONLY";
    
    /**
     * 获取所有现货订单类型
     */
    public static List<String> getAllSpotOrderTypes() {
        return Arrays.asList(
            SPOT_LIMIT,
            SPOT_MARKET,
            SPOT_STOP_LOSS,
            SPOT_TAKE_PROFIT,
            SPOT_STOP_LOSS_LIMIT,
            SPOT_TAKE_PROFIT_LIMIT,
            SPOT_ICEBERG,
            SPOT_TWAP,
            SPOT_IOC,
            SPOT_FOK,
            SPOT_POST_ONLY
        );
    }
    
    // ========== 合约订单类型 ==========
    
    /**
     * 限价委托
     */
    public static final String FUTURES_LIMIT = "LIMIT";
    
    /**
     * 市价委托
     */
    public static final String FUTURES_MARKET = "MARKET";
    
    /**
     * 止损单
     */
    public static final String FUTURES_STOP_LOSS = "STOP_LOSS";
    
    /**
     * 止盈单
     */
    public static final String FUTURES_TAKE_PROFIT = "TAKE_PROFIT";
    
    /**
     * 止损限价单
     */
    public static final String FUTURES_STOP_LOSS_LIMIT = "STOP_LOSS_LIMIT";
    
    /**
     * 止盈限价单
     */
    public static final String FUTURES_TAKE_PROFIT_LIMIT = "TAKE_PROFIT_LIMIT";
    
    /**
     * 冰山订单
     */
    public static final String FUTURES_ICEBERG = "ICEBERG";
    
    /**
     * TWAP订单
     */
    public static final String FUTURES_TWAP = "TWAP";
    
    /**
     * 立即成交或取消（IOC）
     */
    public static final String FUTURES_IOC = "IOC";
    
    /**
     * 全部成交或取消（FOK）
     */
    public static final String FUTURES_FOK = "FOK";
    
    /**
     * 只做Maker（POST_ONLY）
     */
    public static final String FUTURES_POST_ONLY = "POST_ONLY";
    
    /**
     * 计划委托（条件单）
     */
    public static final String FUTURES_TRIGGER_ORDER = "TRIGGER_ORDER";
    
    /**
     * 跟踪止损
     */
    public static final String FUTURES_TRAILING_STOP = "TRAILING_STOP";
    
    /**
     * 获取所有合约订单类型
     */
    public static List<String> getAllFuturesOrderTypes() {
        return Arrays.asList(
            FUTURES_LIMIT,
            FUTURES_MARKET,
            FUTURES_STOP_LOSS,
            FUTURES_TAKE_PROFIT,
            FUTURES_STOP_LOSS_LIMIT,
            FUTURES_TAKE_PROFIT_LIMIT,
            FUTURES_ICEBERG,
            FUTURES_TWAP,
            FUTURES_IOC,
            FUTURES_FOK,
            FUTURES_POST_ONLY,
            FUTURES_TRIGGER_ORDER,
            FUTURES_TRAILING_STOP
        );
    }
    
    /**
     * 获取所有订单类型（现货+合约）
     */
    public static List<String> getAllOrderTypes() {
        return Arrays.asList(
            // 现货
            SPOT_LIMIT,
            SPOT_MARKET,
            SPOT_STOP_LOSS,
            SPOT_TAKE_PROFIT,
            SPOT_STOP_LOSS_LIMIT,
            SPOT_TAKE_PROFIT_LIMIT,
            SPOT_ICEBERG,
            SPOT_TWAP,
            SPOT_IOC,
            SPOT_FOK,
            SPOT_POST_ONLY,
            // 合约
            FUTURES_LIMIT,
            FUTURES_MARKET,
            FUTURES_STOP_LOSS,
            FUTURES_TAKE_PROFIT,
            FUTURES_STOP_LOSS_LIMIT,
            FUTURES_TAKE_PROFIT_LIMIT,
            FUTURES_ICEBERG,
            FUTURES_TWAP,
            FUTURES_IOC,
            FUTURES_FOK,
            FUTURES_POST_ONLY,
            FUTURES_TRIGGER_ORDER,
            FUTURES_TRAILING_STOP
        );
    }
}














