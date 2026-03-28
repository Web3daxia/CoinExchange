/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.util;

import java.util.Arrays;
import java.util.List;

/**
 * K线周期验证工具类
 * K线周期最短为5分钟，最长为1周（1w）
 */
public class KlineIntervalValidator {
    
    /**
     * 支持的K线周期列表
     * 5m（最短）, 15m, 30m, 1h, 4h, 1d, 1w（最长）
     */
    private static final List<String> VALID_INTERVALS = Arrays.asList(
            "5m", "15m", "30m", "1h", "4h", "1d", "1w"
    );

    /**
     * 验证K线周期是否有效
     * @param interval K线周期
     * @return 是否有效
     */
    public static boolean isValid(String interval) {
        return interval != null && VALID_INTERVALS.contains(interval);
    }

    /**
     * 获取所有支持的K线周期
     * @return 支持的K线周期列表
     */
    public static List<String> getValidIntervals() {
        return VALID_INTERVALS;
    }

    /**
     * 验证并抛出异常（如果无效）
     * @param interval K线周期
     * @throws IllegalArgumentException 如果周期无效
     */
    public static void validate(String interval) {
        if (!isValid(interval)) {
            throw new IllegalArgumentException(
                    String.format("无效的K线周期: %s。支持的周期: %s（最短5m，最长1w）", 
                            interval, String.join(", ", VALID_INTERVALS))
            );
        }
    }

    /**
     * 获取周期对应的分钟数
     * @param interval K线周期
     * @return 分钟数，如果周期无效返回-1
     */
    public static int getMinutes(String interval) {
        if (interval == null) {
            return -1;
        }
        switch (interval) {
            case "5m":
                return 5;
            case "15m":
                return 15;
            case "30m":
                return 30;
            case "1h":
                return 60;
            case "4h":
                return 240;
            case "1d":
                return 1440;
            case "1w":
                return 10080;
            default:
                return -1;
        }
    }
}














