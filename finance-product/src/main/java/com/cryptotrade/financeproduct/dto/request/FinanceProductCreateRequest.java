/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 理财产品创建请求DTO
 */
@Data
public class FinanceProductCreateRequest {
    private String productName; // 产品名称
    private String productCode; // 产品代码
    private String productType; // 产品类型: FIXED, FLEXIBLE
    private String riskLevel; // 风险等级: CONSERVATIVE, HIGH_RISK
    private BigDecimal annualRate; // 年化收益率
    private Integer investmentCycle; // 投资周期（天数）
    private BigDecimal minInvestmentAmount; // 最小投资金额
    private BigDecimal maxInvestmentAmount; // 最大投资金额
    private BigDecimal totalRaiseAmount; // 总募集金额
    private String supportedCurrency; // 支持的投资币种
    private Integer lockPeriod; // 锁仓期（天数）
    private String status = "AVAILABLE"; // 产品状态
    private LocalDateTime startTime; // 产品开始时间
    private LocalDateTime endTime; // 产品结束时间
    private String description; // 产品描述
    private String riskWarning; // 风险提示
    private String settlementMethod = "AUTO"; // 结算方式: AUTO, MANUAL
    private String settlementCycle; // 收益结算周期: DAILY, WEEKLY, MONTHLY, MATURITY
    private Integer sortOrder = 0; // 排序顺序
}














