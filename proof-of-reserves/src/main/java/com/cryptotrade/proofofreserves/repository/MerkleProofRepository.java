/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.repository;

import com.cryptotrade.proofofreserves.entity.MerkleProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Merkle证明Repository
 */
@Repository
public interface MerkleProofRepository extends JpaRepository<MerkleProof, Long> {
    /**
     * 根据用户ID查询证明
     */
    List<MerkleProof> findByUserId(Long userId);

    /**
     * 根据树ID和用户ID查询
     */
    Optional<MerkleProof> findByTreeIdAndUserId(Long treeId, Long userId);
}















