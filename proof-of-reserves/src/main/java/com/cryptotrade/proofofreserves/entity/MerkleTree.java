/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Merkle树实体
 */
@Entity
@Table(name = "merkle_trees")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MerkleTree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tree_root", unique = true, nullable = false)
    private String treeRoot; // Merkle树根节点哈希

    @Column(name = "tree_version", nullable = false)
    private String treeVersion; // 树版本号

    @Column(name = "total_assets", precision = 36, scale = 18, nullable = false)
    private java.math.BigDecimal totalAssets; // 总资产

    @Column(name = "total_deposits", precision = 36, scale = 18, nullable = false)
    private java.math.BigDecimal totalDeposits; // 总存款

    @Column(name = "coverage_ratio", precision = 10, scale = 4, nullable = false)
    private java.math.BigDecimal coverageRatio; // 覆盖率（总资产/总存款）

    @Column(name = "tree_data", columnDefinition = "LONGTEXT")
    private String treeData; // 树数据（JSON格式）

    @Column(name = "signature")
    private String signature; // 平台签名

    @Column(name = "blockchain_hash")
    private String blockchainHash; // 区块链存证哈希

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















