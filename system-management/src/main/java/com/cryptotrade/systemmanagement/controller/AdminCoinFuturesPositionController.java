/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.CoinFuturesPositionSearchRequest;
import com.cryptotrade.systemmanagement.dto.response.FuturesPositionDisplayResponse;
import com.cryptotrade.systemmanagement.entity.CoinFuturesPositionManagement;
import com.cryptotrade.systemmanagement.service.CoinFuturesPositionManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 后台币本位永续合约仓位管理Controller（持仓管理/合约资产管理）
 */
@RestController
@RequestMapping("/admin/coin-futures-position")
@Api(tags = "币本位永续合约仓位管理")
public class AdminCoinFuturesPositionController {

    @Autowired
    private CoinFuturesPositionManagementService positionManagementService;

    @PostMapping("/search")
    @ApiOperation(value = "搜索仓位", notes = "搜索币本位永续合约仓位（持仓管理/合约资产管理）")
    public Result<Page<FuturesPositionDisplayResponse>> searchPositions(
            @ApiParam(value = "搜索条件", required = true) @RequestBody CoinFuturesPositionSearchRequest request) {
        try {
            Page<CoinFuturesPositionManagement> positions = positionManagementService.searchPositions(request);
            // 转换为显示响应DTO
            Page<FuturesPositionDisplayResponse> responsePage = positions.map(this::convertToDisplayResponse);
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
            CoinFuturesPositionManagement position = positionManagementService.getPositionById(id);
            FuturesPositionDisplayResponse response = convertToDisplayResponse(position);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 将币本位合约仓位实体转换为显示响应DTO
     */
    private FuturesPositionDisplayResponse convertToDisplayResponse(CoinFuturesPositionManagement entity) {
        FuturesPositionDisplayResponse response = new FuturesPositionDisplayResponse();
        response.setUserId(entity.getUserId());
        response.setMemberName(entity.getMemberName());
        // 格式化合约账户显示
        response.setContractAccountDisplay(com.cryptotrade.systemmanagement.util.OrderDisplayUtil.formatAccountType(entity.getAccountType()));
        response.setContractAccount(entity.getAccountType());
        response.setAvailableBalance(entity.getAvailableBalance());
        response.setFrozenBalance(entity.getFrozenBalance());
        // 格式化仓位模式显示
        response.setMarginModeDisplay(com.cryptotrade.systemmanagement.util.OrderDisplayUtil.formatMarginMode(entity.getMarginMode()));
        response.setMarginMode(entity.getMarginMode());
        response.setLongPosition(entity.getLongPosition());
        response.setLongFrozen(entity.getLongFrozen());
        response.setLongAvgPrice(entity.getLongAvgPrice());
        // 格式化多仓盈亏显示：0.00 | 0.00%
        String longPnlDisplay = (entity.getLongUnrealizedPnl() != null ? entity.getLongUnrealizedPnl().toString() : "0.00")
                + " | " 
                + (entity.getLongUnrealizedPnlPercent() != null ? entity.getLongUnrealizedPnlPercent().toString() + "%" : "0.00%");
        response.setLongPnlDisplay(longPnlDisplay);
        response.setLongUnrealizedPnl(entity.getLongUnrealizedPnl());
        response.setLongUnrealizedPnlPercent(entity.getLongUnrealizedPnlPercent());
        // 格式化多仓杠杆显示：10X
        response.setLongLeverageDisplay(com.cryptotrade.systemmanagement.util.OrderDisplayUtil.formatLeverage(entity.getLongLeverage()));
        response.setLongLeverage(entity.getLongLeverage());
        response.setLongMargin(entity.getLongMargin());
        response.setShortPosition(entity.getShortPosition());
        response.setShortFrozen(entity.getShortFrozen());
        response.setShortAvgPrice(entity.getShortAvgPrice());
        // 格式化空仓盈亏显示：0.00 | 0.00%
        String shortPnlDisplay = (entity.getShortUnrealizedPnl() != null ? entity.getShortUnrealizedPnl().toString() : "0.00")
                + " | " 
                + (entity.getShortUnrealizedPnlPercent() != null ? entity.getShortUnrealizedPnlPercent().toString() + "%" : "0.00%");
        response.setShortPnlDisplay(shortPnlDisplay);
        response.setShortUnrealizedPnl(entity.getShortUnrealizedPnl());
        response.setShortUnrealizedPnlPercent(entity.getShortUnrealizedPnlPercent());
        // 格式化空仓杠杆显示：10X
        response.setShortLeverageDisplay(com.cryptotrade.systemmanagement.util.OrderDisplayUtil.formatLeverage(entity.getShortLeverage()));
        response.setShortLeverage(entity.getShortLeverage());
        response.setShortMargin(entity.getShortMargin());
        return response;
    }
}














