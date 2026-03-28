/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单号生成工具类
 */
public class OrderNoGenerator {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * 生成质押借币订单号
     */
    public static String generateOrderNo() {
        return "PL-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    /**
     * 生成还款单号
     */
    public static String generateRepaymentNo() {
        return "RP-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    /**
     * 生成平仓单号
     */
    public static String generateLiquidationNo() {
        return "LIQ-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    /**
     * 生成补仓单号
     */
    public static String generateTopupNo() {
        return "TOP-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
}














