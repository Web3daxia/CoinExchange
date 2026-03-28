/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.UsdtFuturesPositionSearchRequest;
import com.cryptotrade.systemmanagement.dto.response.FuturesPositionDisplayResponse;
import com.cryptotrade.systemmanagement.entity.UsdtFuturesPositionManagement;
import com.cryptotrade.systemmanagement.service.UsdtFuturesPositionManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 后台U本位永续合约仓位管理Controller（持仓管理/合约资产管理）
 */
@RestController
@RequestMapping("/admin/usdt-futures-position")
@Api(tags = "U本位永续合约仓位管理")
public class AdminUsdtFuturesPositionController {

    @Autowired
    private UsdtFuturesPositionManagementService positionManagementService;

    @PostMapping("/search")
    @ApiOperation(value = "搜索仓位", notes = "搜索U本位永续合约仓位（持仓管理/合约资产管理）")
    public Result<Page<FuturesPositionDisplayResponse>> searchPositions(
            @ApiParam(value = "搜索条件", required = true) @RequestBody UsdtFuturesPositionSearchRequest request) {
        try {
            Page<UsdtFuturesPositionManagement> positions = positionManagementService.searchPositions(request);
            // 转换为显示响应DTO
            Page<FuturesPositionDisplayResponse> responsePage = positions.map(FuturesPositionDisplayResponse::from);
            return Result.success(responsePage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取仓位详情", notes = "根据ID获取仓位详细信息")
    public Result<FuturesPositionDisplayResponse> getPositionDetail(
            @ApiParam(value = "仓位ID", required = true) @PathVariable Long id) {
        try {
            UsdtFuturesPositionManagement position = positionManagementService.getPositionById(id);
            FuturesPositionDisplayResponse response = FuturesPositionDisplayResponse.from(position);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














