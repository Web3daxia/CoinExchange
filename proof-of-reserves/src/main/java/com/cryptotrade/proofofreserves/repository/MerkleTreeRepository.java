/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.repository;

import com.cryptotrade.proofofreserves.entity.MerkleTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Merkle树Repository
 */
@Repository
public interface MerkleTreeRepository extends JpaRepository<MerkleTree, Long> {
    /**
     * 根据树根查询
     */
    Optional<MerkleTree> findByTreeRoot(String treeRoot);

    /**
     * 查询最新的树
     */
    Optional<MerkleTree> findFirstByOrderByCreatedAtDesc();
}















