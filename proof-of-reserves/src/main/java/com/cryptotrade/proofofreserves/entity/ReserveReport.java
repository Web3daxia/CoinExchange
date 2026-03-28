/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 储备金证明报告实体
 */
@Entity
@Table(name = "reserve_reports")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ReserveReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_no", unique = true, nullable = false)
    private String reportNo; // 报告编号

    @Column(name = "tree_id", nullable = false)
    private Long treeId; // Merkle树ID

    @Column(name = "report_date", nullable = false)
    private LocalDateTime reportDate; // 报告日期

    @Column(name = "total_assets", precision = 36, scale = 18, nullable = false)
    private BigDecimal totalAssets; // 总资产

    @Column(name = "total_deposits", precision = 36, scale = 18, nullable = false)
    private BigDecimal totalDeposits; // 总存款

    @Column(name = "coverage_ratio", precision = 10, scale = 4, nullable = false)
    private BigDecimal coverageRatio; // 覆盖率

    @Column(name = "report_content", columnDefinition = "LONGTEXT")
    private String reportContent; // 报告内容（JSON格式）

    @Column(name = "auditor_name")
    private String auditorName; // 审计机构名称

    @Column(name = "audit_status", nullable = false)
    private String auditStatus; // 审计状态: PENDING（待审计）、AUDITED（已审计）、REJECTED（已拒绝）

    @Column(name = "audit_time")
    private LocalDateTime auditTime; // 审计时间

    @Column(name = "public_url")
    private String publicUrl; // 公开URL

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}















