/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 红包使用记录实体
 */
@Entity
@Table(name = "red_packet_uses")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RedPacketUse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "packet_id", nullable = false)
    private Long packetId; // 红包领取记录ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "activity_id", nullable = false)
    private Long activityId; // 红包活动ID

    @Column(name = "packet_no", nullable = false, length = 50)
    private String packetNo; // 红包编号

    @Column(name = "use_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal useAmount; // 使用金额

    @Column(name = "use_type", nullable = false, length = 50)
    private String useType; // 使用类型: TRADE_FEE, TRADE, WITHDRAW, OTHER

    @Column(name = "use_target", length = 100)
    private String useTarget; // 使用目标

    @Column(name = "order_no", length = 50)
    private String orderNo; // 关联订单号

    @Column(name = "before_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal beforeAmount; // 使用前红包余额

    @Column(name = "after_amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal afterAmount; // 使用后红包余额

    @Column(name = "use_time", nullable = false)
    private LocalDateTime useTime; // 使用时间

    @Column(name = "remark", length = 500)
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














