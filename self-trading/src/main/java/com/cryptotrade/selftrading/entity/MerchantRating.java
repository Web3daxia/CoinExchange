/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 商家评价实体
 */
@Entity
@Table(name = "merchant_ratings")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MerchantRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId; // 商家ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 评价用户ID

    @Column(name = "order_id", nullable = false)
    private Long orderId; // 关联的订单ID

    @Column(name = "rating", nullable = false)
    private Integer rating; // 评分（1-5星）

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment; // 评论内容

    @Column(name = "is_anonymous")
    private Boolean isAnonymous; // 是否匿名评价

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（有效）、DELETED（已删除）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















