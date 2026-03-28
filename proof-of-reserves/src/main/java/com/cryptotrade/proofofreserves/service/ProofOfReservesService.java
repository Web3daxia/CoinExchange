/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.service;

import com.cryptotrade.proofofreserves.entity.*;

import java.util.List;
import java.util.Map;

/**
 * 储备金证明服务接口
 */
public interface ProofOfReservesService {
    /**
     * 同步平台资产
     */
    void syncPlatformAssets();

    /**
     * 同步用户存款
     */
    void syncUserDeposits();

    /**
     * 生成Merkle树
     */
    MerkleTree generateMerkleTree();

    /**
     * 获取用户Merkle证明
     */
    MerkleProof getUserProof(Long userId, Long treeId);

    /**
     * 验证Merkle证明
     */
    Boolean verifyProof(Long userId, Long treeId, String proofPath);

    /**
     * 生成储备金证明报告
     */
    ReserveReport generateReserveReport(Long treeId);

    /**
     * 获取储备金统计
     */
    Map<String, Object> getReserveStatistics();

    /**
     * 查询公开报告
     */
    List<ReserveReport> getPublicReports();
}















