/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 币种更新请求DTO
 */
@Data
public class CurrencyUpdateRequest {
    private String currencyName; // 币种名称（如果提供，将更新所有语言名称）
    private String symbol; // 符号
    private String iconUrl; // 图标URL
    private Integer decimals; // 小数位数
    private BigDecimal minWithdrawAmount; // 最小提现金额
    private BigDecimal maxWithdrawAmount; // 最大提现金额
    private BigDecimal withdrawFee; // 提现手续费
    private Boolean depositEnabled; // 是否启用充值
    private Boolean withdrawEnabled; // 是否启用提现
    private Boolean spotEnabled; // 现货交易区是否启用
    private Boolean futuresUsdtEnabled; // USDT本位合约是否启用
    private Boolean futuresCoinEnabled; // 币本位合约是否启用
    private Boolean optionsEnabled; // 期权交易是否启用
    private Boolean leveragedEnabled; // 杠杆交易是否启用
    private String status; // 状态
    private Integer sortOrder; // 排序顺序
    private String description; // 币种描述
    
    // 新增字段
    private Long agentId; // 币种所属代理商ID
    private String currencyUnit; // 币种单位
    private BigDecimal totalSupply; // 总数量
    private String detailUrl; // 详情链接
    private LocalDate listingDate; // 上线日期
    private String logoUrl; // 币种logo URL
    private String logoFilePath; // 币种logo文件路径
    private String intro; // 币种简介
    private BigDecimal baseExchangeRateCny; // 基础汇率（相对于人民币）
    private BigDecimal baseExchangeRateUsd; // 基础汇率（相对于美元）
    
    // 币种分类（如果提供，将更新分类关联）
    private List<Long> categoryIds; // 分类ID列表
}














