/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 账户编号生成工具类
 */
public class AccountNoGenerator {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * 生成体验金账户编号
     */
    public static String generateAccountNo() {
        return "EXP-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    /**
     * 生成交易编号
     */
    public static String generateTradeNo() {
        return "EXP-TRADE-" + LocalDateTime.now().format(DATE_FORMATTER) + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
}














