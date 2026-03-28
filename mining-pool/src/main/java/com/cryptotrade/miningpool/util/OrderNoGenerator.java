/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单号生成工具类
 */
public class OrderNoGenerator {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * 生成算力租赁订单号
     */
    public static String generateRentalOrderNo() {
        return "RENT-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    /**
     * 生成挖矿结算订单号
     */
    public static String generateSettlementOrderNo() {
        return "MIN-SET-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
}














