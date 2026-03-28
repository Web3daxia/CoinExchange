/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户红包领取记录实体
 */
@Entity
@Table(name = "red_packet_receives")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RedPacketReceive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "activity_id", nullable = false)
    private Long activityId; // 红包活动ID

    @Column(name = "packet_no", unique = true, nullable = false, length = 50)
    private String packetNo; // 红包编号（唯一标识）

    @Column(name = "packet_type", nullable = false, length = 50)
    private String packetType; // 红包类型

    @Column(name = "amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal amount; // 红包金额

    @Column(name = "remaining_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal remainingAmount; // 剩余金额

    @Column(name = "currency", length = 20)
    private String currency; // 币种（现金红包）

    @Column(name = "discount_rate", precision = 10, scale = 6)
    private BigDecimal discountRate; // 折扣比例（折扣红包）

    @Column(name = "receive_time", nullable = false)
    private LocalDateTime receiveTime; // 领取时间

    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime; // 过期时间

    @Column(name = "status", nullable = false, length = 20)
    private String status = "VALID"; // 状态: VALID, USED, EXPIRED, CANCELLED

    @Column(name = "use_count", nullable = false)
    private Integer useCount = 0; // 使用次数

    @Column(name = "max_use_count")
    private Integer maxUseCount; // 最大使用次数

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}














