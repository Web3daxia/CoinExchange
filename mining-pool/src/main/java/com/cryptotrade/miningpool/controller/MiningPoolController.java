/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.miningpool.dto.request.MiningPoolCreateRequest;
import com.cryptotrade.miningpool.entity.MiningPool;
import com.cryptotrade.miningpool.service.MiningPoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 矿池管理Controller（后台管理）
 */
@RestController
@RequestMapping("/admin/mining/pool")
@Api(tags = "矿池管理")
public class MiningPoolController {

    @Autowired
    private MiningPoolService poolService;

    @PostMapping("/create")
    @ApiOperation(value = "创建矿池", notes = "创建新的矿池")
    public Result<MiningPool> createPool(
            @ApiParam(value = "矿池信息", required = true) @RequestBody MiningPoolCreateRequest request) {
        try {
            MiningPool pool = poolService.createPool(request);
            return Result.success("矿池创建成功", pool);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有矿池", notes = "获取所有矿池列表")
    public Result<List<MiningPool>> getAllPools() {
        try {
            List<MiningPool> pools = poolService.getAllPools();
            return Result.success(pools);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/active")
    @ApiOperation(value = "获取活跃矿池", notes = "获取所有活跃的矿池")
    public Result<List<MiningPool>> getActivePools() {
        try {
            List<MiningPool> pools = poolService.getActivePools();
            return Result.success(pools);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{poolId}")
    @ApiOperation(value = "获取矿池详情", notes = "根据ID获取矿池详情")
    public Result<MiningPool> getPoolById(
            @ApiParam(value = "矿池ID", required = true) @PathVariable Long poolId) {
        try {
            MiningPool pool = poolService.getPoolById(poolId);
            return Result.success(pool);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














