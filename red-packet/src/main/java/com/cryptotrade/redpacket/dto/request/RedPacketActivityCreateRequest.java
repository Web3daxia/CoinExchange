/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 红包活动创建请求DTO
 */
@Data
public class RedPacketActivityCreateRequest {
    private String activityName; // 活动名称
    private String activityCode; // 活动代码
    private String packetType; // 红包类型: CASH, COUPON, POINTS, DISCOUNT
    private String amountType; // 金额类型: FIXED, RANDOM
    private BigDecimal fixedAmount; // 固定金额
    private BigDecimal minAmount; // 最小金额（随机金额）
    private BigDecimal maxAmount; // 最大金额（随机金额）
    private BigDecimal totalAmount; // 红包总金额
    private Integer totalCount; // 红包总数量
    private String distributionScope; // 发放范围: SPECIFIC, ALL, VIP
    private String receiveCondition; // 领取条件: TRADE, CHECKIN, INVITE, NONE
    private BigDecimal conditionValue; // 条件值
    private Integer validDays = 7; // 有效期（天数）
    private String useScope; // 使用范围限制（JSON格式）
    private String useTimeLimit; // 使用时间限制（JSON格式）
    private String status = "ACTIVE"; // 活动状态
    private LocalDateTime startTime; // 活动开始时间
    private LocalDateTime endTime; // 活动结束时间
    private String description; // 活动描述
    private Boolean autoIssue = false; // 是否自动发放
    private String issueCycle; // 发放周期: DAILY, WEEKLY, MONTHLY
}














