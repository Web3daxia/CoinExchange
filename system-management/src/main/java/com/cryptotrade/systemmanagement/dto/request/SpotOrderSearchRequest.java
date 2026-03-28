/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 现货订单搜索请求DTO
 */
@Data
public class SpotOrderSearchRequest {
    private String email; // 会员邮箱
    private Long userId; // 用户ID
    private String memberUid; // 会员UID
    private String phone; // 手机号
    private String baseCurrency; // 交易的币种
    private String quoteCurrency; // 结算的币种
    private String orderNo; // 委托单号
    private LocalDateTime filledAtStart; // 成交时间开始
    private LocalDateTime filledAtEnd; // 成交时间结束
    private String pairName; // 交易对
    private String status; // 订单状态: PENDING（交易中）, FILLED（已完成）, CANCELLED（已取消）, TIMEOUT（超时）, ALL（全部）
    private String side; // 订单方向: BUY（买入）, SELL（卖出）, ALL（全部）
    private String orderType; // 挂单类型: LIMIT（限价委托）, MARKET（市价委托）等, ALL（全部）
    private String orderSource; // 订单来源: USER（只看用户）, NOT_USER（不看用户）, ROBOT（只看机器人）, NOT_ROBOT（不看机器人）, ALL（全部）
    
    // 分页参数
    private Integer page = 0;
    private Integer size = 20;
}














