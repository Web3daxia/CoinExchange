/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.proofofreserves.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.proofofreserves.entity.MerkleProof;
import com.cryptotrade.proofofreserves.entity.MerkleTree;
import com.cryptotrade.proofofreserves.entity.ReserveReport;
import com.cryptotrade.proofofreserves.service.ProofOfReservesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 储备金证明控制器
 */
@RestController
@RequestMapping("/proof-of-reserves")
@Api(tags = "储备金证明模块")
public class ProofOfReservesController {
    @Autowired
    private ProofOfReservesService proofOfReservesService;

    @PostMapping("/generate-tree")
    @ApiOperation(value = "生成Merkle树", notes = "生成储备金证明的Merkle树")
    public Result<MerkleTree> generateMerkleTree() {
        try {
            MerkleTree tree = proofOfReservesService.generateMerkleTree();
            return Result.success("Merkle树生成成功", tree);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/proof")
    @ApiOperation(value = "获取用户证明", notes = "获取用户在Merkle树中的证明路径")
    public Result<MerkleProof> getUserProof(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "树ID") @RequestParam(required = false) Long treeId) {
        try {
            if (treeId == null) {
                // 获取最新的报告，使用其树ID
                List<ReserveReport> reports = proofOfReservesService.getPublicReports();
                if (reports.isEmpty()) {
                    return Result.error("未找到Merkle树");
                }
                treeId = reports.get(0).getTreeId();
            }
            MerkleProof proof = proofOfReservesService.getUserProof(userId, treeId);
            return Result.success(proof);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/verify")
    @ApiOperation(value = "验证证明", notes = "验证用户的Merkle证明")
    public Result<Boolean> verifyProof(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "树ID", required = true) @RequestParam Long treeId,
            @ApiParam(value = "证明路径", required = true) @RequestParam String proofPath) {
        try {
            Boolean isValid = proofOfReservesService.verifyProof(userId, treeId, proofPath);
            return Result.success("验证完成", isValid);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "获取储备金统计", notes = "获取平台储备金统计数据")
    public Result<Map<String, Object>> getReserveStatistics() {
        try {
            Map<String, Object> statistics = proofOfReservesService.getReserveStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/reports")
    @ApiOperation(value = "获取公开报告", notes = "获取已审计的储备金证明报告")
    public Result<List<ReserveReport>> getPublicReports() {
        try {
            List<ReserveReport> reports = proofOfReservesService.getPublicReports();
            return Result.success(reports);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

