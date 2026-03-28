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
 * 代理商用户关系实体
 */
@Entity
@Table(name = "agent_user_relations")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AgentUserRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agent_id", nullable = false)
    private Long agentId; // 代理商ID

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // 用户ID

    @Column(name = "invite_code")
    private String inviteCode; // 邀请码

    @Column(name = "invite_link")
    private String inviteLink; // 邀请链接

    @Column(name = "user_group")
    private String userGroup; // 用户分组，如 HIGH_ASSET, HIGH_VOLUME, NORMAL

    @Column(name = "status", nullable = false)
    private String status; // ACTIVE（有效）、INACTIVE（无效）

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















