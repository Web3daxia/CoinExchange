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
 * Merkle证明实体
 */
@Entity
@Table(name = "merkle_proofs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MerkleProof {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tree_id", nullable = false)
    private Long treeId; // Merkle树ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 用户ID

    @Column(name = "deposit_id", nullable = false)
    private Long depositId; // 存款ID

    @Column(name = "leaf_hash", nullable = false)
    private String leafHash; // 叶子节点哈希

    @Column(name = "proof_path", columnDefinition = "TEXT", nullable = false)
    private String proofPath; // 证明路径（JSON数组）

    @Column(name = "verified", nullable = false)
    private Boolean verified; // 是否已验证

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}















