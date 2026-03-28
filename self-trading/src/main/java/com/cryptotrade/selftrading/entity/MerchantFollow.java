/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 商家关注实体
 */
@Entity
@Table(name = "merchant_follows")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MerchantFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId; // 商家ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 关注用户ID

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















