/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 币种分类关联实体（一个币种可以属于多个分类）
 */
@Entity
@Table(name = "currency_category_relations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"currency_id", "category_id"})
})
@Data
@EntityListeners(AuditingEntityListener.class)
public class CurrencyCategoryRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_id", nullable = false)
    private Long currencyId; // 币种ID

    @Column(name = "category_id", nullable = false)
    private Long categoryId; // 分类ID

    @Column(name = "sort_order")
    private Integer sortOrder = 0; // 在此分类下的排序

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 关联关系（可选，用于查询）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", insertable = false, updatable = false)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private CurrencyCategory category;
}














