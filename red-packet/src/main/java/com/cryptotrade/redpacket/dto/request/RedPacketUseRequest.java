/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 红包使用请求DTO
 */
@Data
public class RedPacketUseRequest {
    private Long packetId; // 红包领取记录ID
    private BigDecimal useAmount; // 使用金额
    private String useType; // 使用类型: TRADE_FEE, TRADE, WITHDRAW, OTHER
    private String orderNo; // 关联订单号
    private String useTarget; // 使用目标
}














