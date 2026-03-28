/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 币本位永续合约仓位搜索请求DTO（与U本位相同结构）
 */
@Data
public class CoinFuturesPositionSearchRequest {
    private String email; // 会员邮箱
    private Long userId; // 用户ID
    private String memberUid; // 会员UID
    private String phone; // 手机号
    private BigDecimal availableBalanceGreaterThan; // 可用余额大于
    private BigDecimal frozenBalanceGreaterThan; // 冻结余额大于
    private Integer longLeverageGreaterThan; // 多单杠杆大于
    private BigDecimal longPositionGreaterThan; // 多单仓位大于
    private BigDecimal longFrozenGreaterThan; // 冻结多仓大于
    private BigDecimal longMarginGreaterThan; // 多单保证金大于
    private Integer shortLeverageGreaterThan; // 空单杠杆大于
    private BigDecimal shortPositionGreaterThan; // 空单仓位大于
    private BigDecimal shortFrozenGreaterThan; // 冻结空仓大于
    private BigDecimal shortMarginGreaterThan; // 空单保证金大于
    private String pairName; // 合约交易对
    private String marginMode; // 仓位模式: CROSS（全仓）, ISOLATED（逐仓）, SEGMENTED（分仓）, COMBINED（合仓）, ALL（全部）
    
    // 分页参数
    private Integer page = 0;
    private Integer size = 20;
}














