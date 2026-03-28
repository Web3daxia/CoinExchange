/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 币种多语言名称实体
 */
@Entity
@Table(name = "currency_names", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"currency_id", "language_code"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class CurrencyName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_id", nullable = false)
    private Long currencyId; // 币种ID

    @Column(name = "language_code", nullable = false, length = 20)
    private String languageCode; // 语言代码，如: zh-CN, en-US等

    @Column(name = "currency_name", nullable = false, length = 100)
    private String currencyName; // 币种名称（该语言下的名称）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 关联关系（可选，用于查询）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", insertable = false, updatable = false)
    private Currency currency;
}














