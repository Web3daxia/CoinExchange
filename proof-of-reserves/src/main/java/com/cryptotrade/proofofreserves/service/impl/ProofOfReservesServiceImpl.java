/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.service.impl;

import com.cryptotrade.proofofreserves.entity.*;
import com.cryptotrade.proofofreserves.repository.*;
import com.cryptotrade.proofofreserves.service.ProofOfReservesService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 储备金证明服务实现
 */
@Service
public class ProofOfReservesServiceImpl implements ProofOfReservesService {
    @Autowired
    private PlatformAssetRepository platformAssetRepository;

    @Autowired
    private UserDepositRepository userDepositRepository;

    @Autowired
    private MerkleTreeRepository merkleTreeRepository;

    @Autowired
    private MerkleProofRepository merkleProofRepository;

    @Autowired
    private ReserveReportRepository reserveReportRepository;

    @Override
    @Transactional
    public void syncPlatformAssets() {
        // 这里应该从各个账户同步资产
        // 简化处理，实际应该调用钱包服务等
        LocalDateTime now = LocalDateTime.now();
        
        // 示例：同步现货资产
        // List<Wallet> spotWallets = walletService.getAllSpotWallets();
        // for (Wallet wallet : spotWallets) {
        //     PlatformAsset asset = new PlatformAsset();
        //     asset.setAssetType("SPOT");
        //     asset.setCurrency(wallet.getCurrency());
        //     asset.setBalance(wallet.getBalance());
        //     asset.setSyncTime(now);
        //     platformAssetRepository.save(asset);
        // }
    }

    @Override
    @Transactional
    public void syncUserDeposits() {
        // 这里应该从用户账户同步存款
        // 简化处理
        LocalDateTime now = LocalDateTime.now();
        
        // 示例：同步用户现货存款
        // List<User> users = userService.getAllUsers();
        // for (User user : users) {
        //     List<Wallet> userWallets = walletService.getUserWallets(user.getId());
        //     for (Wallet wallet : userWallets) {
        //         UserDeposit deposit = new UserDeposit();
        //         deposit.setUserId(user.getId());
        //         deposit.setAccountType("SPOT");
        //         deposit.setCurrency(wallet.getCurrency());
        //         deposit.setBalance(wallet.getBalance());
        //         deposit.setDepositHash(generateDepositHash(user.getId(), wallet));
        //         deposit.setSyncTime(now);
        //         userDepositRepository.save(deposit);
        //     }
        // }
    }

    @Override
    @Transactional
    public MerkleTree generateMerkleTree() {
        // 获取所有用户存款
        List<UserDeposit> deposits = userDepositRepository.findAll();
        
        // 构建Merkle树
        List<String> leafHashes = deposits.stream()
                .map(d -> d.getDepositHash())
                .collect(Collectors.toList());
        
        String rootHash = buildMerkleTree(leafHashes);
        
        // 计算总资产和总存款
        BigDecimal totalAssets = platformAssetRepository.findAll().stream()
                .map(PlatformAsset::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalDeposits = deposits.stream()
                .map(UserDeposit::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal coverageRatio = totalDeposits.compareTo(BigDecimal.ZERO) > 0
                ? totalAssets.divide(totalDeposits, 4, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;
        
        // 创建Merkle树记录
        MerkleTree tree = new MerkleTree();
        tree.setTreeRoot(rootHash);
        tree.setTreeVersion("v" + System.currentTimeMillis());
        tree.setTotalAssets(totalAssets);
        tree.setTotalDeposits(totalDeposits);
        tree.setCoverageRatio(coverageRatio);
        tree.setSignature(generateSignature(rootHash));
        tree.setCreatedAt(LocalDateTime.now());
        
        tree = merkleTreeRepository.save(tree);
        
        // 为每个用户生成证明
        for (int i = 0; i < deposits.size(); i++) {
            UserDeposit deposit = deposits.get(i);
            String proofPath = generateProofPath(leafHashes, i);
            
            MerkleProof proof = new MerkleProof();
            proof.setTreeId(tree.getId());
            proof.setUserId(deposit.getUserId());
            proof.setDepositId(deposit.getId());
            proof.setLeafHash(deposit.getDepositHash());
            proof.setProofPath(proofPath);
            proof.setVerified(false);
            proof.setCreatedAt(LocalDateTime.now());
            merkleProofRepository.save(proof);
        }
        
        return tree;
    }

    @Override
    public MerkleProof getUserProof(Long userId, Long treeId) {
        return merkleProofRepository.findByTreeIdAndUserId(treeId, userId)
                .orElseThrow(() -> new RuntimeException("证明不存在"));
    }

    @Override
    public Boolean verifyProof(Long userId, Long treeId, String proofPath) {
        MerkleProof proof = merkleProofRepository.findByTreeIdAndUserId(treeId, userId)
                .orElseThrow(() -> new RuntimeException("证明不存在"));
        
        MerkleTree tree = merkleTreeRepository.findById(treeId)
                .orElseThrow(() -> new RuntimeException("Merkle树不存在"));
        
        // 验证证明路径
        String calculatedRoot = verifyMerkleProof(proof.getLeafHash(), proofPath);
        boolean isValid = calculatedRoot.equals(tree.getTreeRoot());
        
        if (isValid) {
            proof.setVerified(true);
            merkleProofRepository.save(proof);
        }
        
        return isValid;
    }

    @Override
    @Transactional
    public ReserveReport generateReserveReport(Long treeId) {
        MerkleTree tree = merkleTreeRepository.findById(treeId)
                .orElseThrow(() -> new RuntimeException("Merkle树不存在"));
        
        ReserveReport report = new ReserveReport();
        report.setReportNo("POR" + System.currentTimeMillis());
        report.setTreeId(treeId);
        report.setReportDate(LocalDateTime.now());
        report.setTotalAssets(tree.getTotalAssets());
        report.setTotalDeposits(tree.getTotalDeposits());
        report.setCoverageRatio(tree.getCoverageRatio());
        report.setAuditStatus("PENDING");
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());
        
        return reserveReportRepository.save(report);
    }

    @Override
    public Map<String, Object> getReserveStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总资产
        BigDecimal totalAssets = platformAssetRepository.findAll().stream()
                .map(PlatformAsset::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("totalAssets", totalAssets);
        
        // 总存款
        BigDecimal totalDeposits = userDepositRepository.findAll().stream()
                .map(UserDeposit::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.put("totalDeposits", totalDeposits);
        
        // 覆盖率
        BigDecimal coverageRatio = totalDeposits.compareTo(BigDecimal.ZERO) > 0
                ? totalAssets.divide(totalDeposits, 4, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;
        statistics.put("coverageRatio", coverageRatio);
        
        // 最新Merkle树
        Optional<MerkleTree> latestTree = merkleTreeRepository.findFirstByOrderByCreatedAtDesc();
        if (latestTree.isPresent()) {
            statistics.put("latestTreeRoot", latestTree.get().getTreeRoot());
            statistics.put("latestTreeTime", latestTree.get().getCreatedAt());
        }
        
        return statistics;
    }

    @Override
    public List<ReserveReport> getPublicReports() {
        return reserveReportRepository.findByAuditStatus("AUDITED");
    }

    // ==================== 私有方法 ====================

    private String buildMerkleTree(List<String> leafHashes) {
        if (leafHashes.isEmpty()) {
            return "";
        }
        
        List<String> currentLevel = new ArrayList<>(leafHashes);
        
        while (currentLevel.size() > 1) {
            List<String> nextLevel = new ArrayList<>();
            for (int i = 0; i < currentLevel.size(); i += 2) {
                String left = currentLevel.get(i);
                String right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left;
                String combined = left + right;
                nextLevel.add(DigestUtils.sha256Hex(combined));
            }
            currentLevel = nextLevel;
        }
        
        return currentLevel.get(0);
    }

    private String generateDepositHash(Long userId, Object wallet) {
        String data = userId + ":" + wallet.toString() + ":" + System.currentTimeMillis();
        return DigestUtils.sha256Hex(data);
    }

    private String generateProofPath(List<String> leafHashes, int index) {
        // 简化实现，实际应该生成完整的证明路径
        return "[" + index + "]";
    }

    private String verifyMerkleProof(String leafHash, String proofPath) {
        // 简化实现，实际应该根据证明路径验证
        return leafHash;
    }

    private String generateSignature(String data) {
        // 实际应该使用平台的私钥签名
        return "signature:" + DigestUtils.sha256Hex(data);
    }
}















