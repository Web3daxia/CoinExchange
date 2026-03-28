/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 代理商实体
 */
@Entity
@Table(name = "agents")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // 用户ID

    @Column(name = "agent_code", unique = true, nullable = false)
    private String agentCode; // 代理商编码

    @Column(name = "agent_name")
    private String agentName; // 代理商名称

    @Column(name = "level", nullable = false)
    private String level; // 代理商等级，如 LEVEL1, LEVEL2, LEVEL3

    @Column(name = "status", nullable = false)
    private String status; // PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）、SUSPENDED（已暂停）

    @Column(name = "apply_time")
    private LocalDateTime applyTime; // 申请时间

    @Column(name = "approve_time")
    private LocalDateTime approveTime; // 审核通过时间

    @Column(name = "approve_user_id")
    private Long approveUserId; // 审核人ID

    @Column(name = "reject_reason")
    private String rejectReason; // 拒绝原因

    @Column(name = "remark")
    private String remark; // 备注

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















