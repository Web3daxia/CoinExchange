/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.util;

/**
 * 订单显示格式化工具类
 */
public class OrderDisplayUtil {

    /**
     * 格式化合约委托方向显示
     * 例如：OPEN_LONG -> 买入开多, OPEN_SHORT -> 买入开空, CLOSE_LONG -> 卖出平多, CLOSE_SHORT -> 卖出平空
     */
    public static String formatOrderDirection(String orderDirection, String side) {
        if (orderDirection == null) {
            return "";
        }
        
        switch (orderDirection) {
            case "OPEN_LONG":
                return "买入开多";
            case "OPEN_SHORT":
                return "买入开空";
            case "CLOSE_LONG":
                return "卖出平多";
            case "CLOSE_SHORT":
                return "卖出平空";
            default:
                // 如果没有匹配，尝试根据side和orderDirection组合判断
                if (side != null) {
                    if ("BUY".equals(side)) {
                        if (orderDirection.contains("LONG")) {
                            return "买入开多";
                        } else if (orderDirection.contains("SHORT")) {
                            return "买入开空";
                        }
                    } else if ("SELL".equals(side)) {
                        if (orderDirection.contains("LONG")) {
                            return "卖出平多";
                        } else if (orderDirection.contains("SHORT")) {
                            return "卖出平空";
                        }
                    }
                }
                return orderDirection;
        }
    }

    /**
     * 格式化杠杆显示
     * 例如：10 -> 10X
     */
    public static String formatLeverage(Integer leverage) {
        if (leverage == null) {
            return "";
        }
        return leverage + "X";
    }

    /**
     * 格式化合约账户类型显示
     * 例如：FUTURES_USDT -> USDT合约账户, FUTURES_COIN -> 币本位合约账户
     */
    public static String formatAccountType(String accountType) {
        if (accountType == null) {
            return "";
        }
        
        switch (accountType) {
            case "FUTURES_USDT":
                return "USDT合约账户";
            case "FUTURES_COIN":
                return "币本位合约账户";
            default:
                return accountType;
        }
    }

    /**
     * 格式化仓位模式显示
     * 例如：CROSS -> 全仓, ISOLATED -> 逐仓, SEGMENTED -> 分仓, COMBINED -> 合仓
     */
    public static String formatMarginMode(String marginMode) {
        if (marginMode == null) {
            return "";
        }
        
        switch (marginMode) {
            case "CROSS":
                return "全仓";
            case "ISOLATED":
                return "逐仓";
            case "SEGMENTED":
                return "分仓";
            case "COMBINED":
                return "合仓";
            default:
                return marginMode;
        }
    }

    /**
     * 格式化订单类型显示（包含所有高级委托方式）
     */
    public static String formatOrderType(String orderType) {
        if (orderType == null) {
            return "";
        }
        
        switch (orderType.toUpperCase()) {
            // 基础类型
            case "LIMIT":
                return "限价";
            case "MARKET":
                return "市价";
            // 止损止盈
            case "STOP_LOSS":
                return "止损单";
            case "TAKE_PROFIT":
                return "止盈单";
            case "STOP_LOSS_LIMIT":
                return "止损限价单";
            case "TAKE_PROFIT_LIMIT":
                return "止盈限价单";
            // 高级委托
            case "ICEBERG":
                return "冰山订单";
            case "TWAP":
                return "TWAP订单";
            case "IOC":
                return "立即成交或取消(IOC)";
            case "FOK":
                return "全部成交或取消(FOK)";
            case "POST_ONLY":
                return "只做Maker";
            // 合约专用
            case "TRIGGER_ORDER":
                return "计划委托";
            case "TRAILING_STOP":
                return "跟踪止损";
            default:
                return orderType;
        }
    }

    /**
     * 格式化订单状态显示
     */
    public static String formatOrderStatus(String status) {
        if (status == null) {
            return "";
        }
        
        switch (status) {
            case "PENDING":
                return "交易中";
            case "FILLED":
                return "已完成";
            case "CANCELLED":
                return "已取消";
            case "TIMEOUT":
                return "超时";
            case "FAILED":
                return "委托失败";
            default:
                return status;
        }
    }

    /**
     * 格式化订单方向显示
     */
    public static String formatSide(String side) {
        if (side == null) {
            return "";
        }
        
        switch (side) {
            case "BUY":
                return "买入";
            case "SELL":
                return "卖出";
            default:
                return side;
        }
    }

    /**
     * 格式化订单来源显示
     */
    public static String formatOrderSource(String orderSource) {
        if (orderSource == null) {
            return "";
        }
        
        switch (orderSource) {
            case "USER":
                return "用户";
            case "ROBOT":
                return "机器人";
            default:
                return orderSource;
        }
    }
}

