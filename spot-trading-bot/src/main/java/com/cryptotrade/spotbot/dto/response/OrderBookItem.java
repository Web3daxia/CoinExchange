/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spotbot.dto.response;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 盘口数据项
 */
@Data
public class OrderBookItem {
    private String side; // BUY, SELL
    private BigDecimal price; // 价格
    private BigDecimal quantity; // 数量
    private BigDecimal amount; // 金额
    private Integer index; // 索引（1-50）
}














