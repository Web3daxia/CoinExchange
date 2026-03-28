/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 算力租赁请求DTO
 */
@Data
public class HashrateRentalRequest {
    private Long poolId; // 矿池ID
    private Long hashrateTypeId; // 算力类型ID
    private BigDecimal hashrateAmount; // 租赁的算力数量
    private Integer rentalPeriod; // 租赁周期（天数）
    private String currency; // 支付币种
}














