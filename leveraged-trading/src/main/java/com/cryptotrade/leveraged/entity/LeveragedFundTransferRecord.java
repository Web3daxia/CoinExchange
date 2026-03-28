/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 杠杆资金转账记录实体
 */
@Entity
@Table(name = "leveraged_fund_transfer_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LeveragedFundTransferRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_id", unique = true, nullable = false)
    private String transferId; // 转账ID

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "pair_name", nullable = false)
    private String pairName; // 交易对名称

    @Column(name = "currency", nullable = false)
    private String currency; // 币种

    @Column(name = "amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal amount; // 转账金额

    @Column(name = "from_account", nullable = false)
    private String fromAccount; // 转出账户: SPOT（现货账户）, LEVERAGED（杠杆账户）

    @Column(name = "to_account", nullable = false)
    private String toAccount; // 转入账户: SPOT（现货账户）, LEVERAGED（杠杆账户）

    @Column(name = "transfer_type", nullable = false)
    private String transferType; // 转账类型: DEPOSIT（转入）, WITHDRAW（转出）

    @Column(name = "status", nullable = false)
    private String status; // SUCCESS（成功）, PENDING（处理中）, FAILED（失败）

    @Column(name = "remark")
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}













