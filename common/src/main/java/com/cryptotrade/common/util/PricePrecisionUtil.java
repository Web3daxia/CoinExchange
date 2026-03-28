/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 价格精度工具类
 * 用于根据设置的精度返回价格
 */
public class PricePrecisionUtil {
    
    /**
     * 根据精度设置返回价格
     * @param price 原始价格
     * @param precision 精度（如0.01表示保留两位小数，1表示整数）
     * @return 格式化后的价格
     */
    public static BigDecimal formatPrice(BigDecimal price, BigDecimal precision) {
        if (price == null || precision == null || precision.compareTo(BigDecimal.ZERO) <= 0) {
            return price;
        }
        
        // 计算精度的小数位数
        int scale = getPrecisionScale(precision);
        
        // 如果精度是整数（如1, 10, 100等），直接向下取整到整数
        if (precision.compareTo(BigDecimal.ONE) >= 0 && precision.scale() == 0) {
            // 对于整数精度，需要将价格除以精度，向下取整，再乘以精度
            BigDecimal divided = price.divide(precision, 0, RoundingMode.DOWN);
            return divided.multiply(precision);
        }
        
        // 对于小数精度（如0.01, 0.001等），按精度向下取整
        BigDecimal divided = price.divide(precision, 0, RoundingMode.DOWN);
        return divided.multiply(precision).setScale(scale, RoundingMode.DOWN);
    }
    
    /**
     * 获取精度的小数位数
     */
    private static int getPrecisionScale(BigDecimal precision) {
        if (precision.compareTo(BigDecimal.ONE) >= 0) {
            return 0; // 整数精度，返回0位小数
        }
        
        String precisionStr = precision.toPlainString();
        if (precisionStr.contains(".")) {
            String decimalPart = precisionStr.split("\\.")[1];
            // 计算小数位数
            int scale = decimalPart.length();
            // 去掉尾部的0
            while (scale > 0 && decimalPart.charAt(scale - 1) == '0') {
                scale--;
            }
            return scale;
        }
        return 0;
    }
    
    /**
     * 示例用法
     */
    public static void main(String[] args) {
        BigDecimal price = new BigDecimal("87500.556895");
        
        // 精度0.01，返回87500.55
        BigDecimal result1 = formatPrice(price, new BigDecimal("0.01"));
        System.out.println("精度0.01: " + result1); // 87500.55
        
        // 精度1，返回87500
        BigDecimal result2 = formatPrice(price, new BigDecimal("1"));
        System.out.println("精度1: " + result2); // 87500
        
        // 精度0.1，返回87500.5
        BigDecimal result3 = formatPrice(price, new BigDecimal("0.1"));
        System.out.println("精度0.1: " + result3); // 87500.5
        
        // 精度10，返回87500
        BigDecimal result4 = formatPrice(price, new BigDecimal("10"));
        System.out.println("精度10: " + result4); // 87500
    }
}














