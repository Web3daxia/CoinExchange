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
 * 红包过期记录实体
 */
@Entity
@Table(name = "red_packet_expired")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RedPacketExpired {
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

    @Column(name = "amount", precision = 20, scale = 8, nullable = false)
    private BigDecimal amount; // 过期金额

    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime; // 过期时间

    @Column(name = "process_status", nullable = false, length = 20)
    private String processStatus = "PENDING"; // 处理状态: PENDING, PROCESSED, REISSUED

    @Column(name = "processed_time")
    private LocalDateTime processedTime; // 处理时间

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}














