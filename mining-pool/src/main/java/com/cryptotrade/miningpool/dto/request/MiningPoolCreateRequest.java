/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 矿池创建请求DTO
 */
@Data
public class MiningPoolCreateRequest {
    private String poolName; // 矿池名称
    private String poolCode; // 矿池代码
    private String miningCurrency; // 挖矿币种
    private String algorithm; // 挖矿算法
    private String distributionMethod = "PPS"; // 收益分配方式: PPS, PPLNS, PROP
    private BigDecimal hashratePrice; // 算力价格
    private BigDecimal minHashrate; // 最低算力门槛
    private Integer maxParticipants; // 最大参与用户数
    private String status = "ACTIVE"; // 矿池状态
    private String riskLevel = "MEDIUM"; // 风险等级
    private String settlementCycle = "DAILY"; // 收益结算周期
    private String description; // 矿池描述
    private String riskWarning; // 风险提示
    private Integer sortOrder = 0; // 排序顺序
}














