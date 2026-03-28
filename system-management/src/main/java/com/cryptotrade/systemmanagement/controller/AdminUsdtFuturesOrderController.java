/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.UsdtFuturesOrderSearchRequest;
import com.cryptotrade.systemmanagement.dto.response.FuturesOrderDisplayResponse;
import com.cryptotrade.systemmanagement.entity.UsdtFuturesOrderManagement;
import com.cryptotrade.systemmanagement.service.UsdtFuturesOrderManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 后台U本位永续合约委托管理Controller
 */
@RestController
@RequestMapping("/admin/usdt-futures-order")
@Api(tags = "U本位永续合约委托管理")
public class AdminUsdtFuturesOrderController {

    @Autowired
    private UsdtFuturesOrderManagementService orderManagementService;

    @PostMapping("/current/search")
    @ApiOperation(value = "搜索当前委托", notes = "搜索U本位永续合约当前委托订单")
    public Result<Page<FuturesOrderDisplayResponse>> searchCurrentOrders(
            @ApiParam(value = "搜索条件", required = true) @RequestBody UsdtFuturesOrderSearchRequest request) {
        try {
            Page<UsdtFuturesOrderManagement> orders = orderManagementService.searchCurrentOrders(request);
            // 转换为显示响应DTO
            Page<FuturesOrderDisplayResponse> responsePage = orders.map(FuturesOrderDisplayResponse::from);
            return Result.success(responsePage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/history/search")
    @ApiOperation(value = "搜索历史委托", notes = "搜索U本位永续合约历史委托订单")
    public Result<Page<FuturesOrderDisplayResponse>> searchHistoryOrders(
            @ApiParam(value = "搜索条件", required = true) @RequestBody UsdtFuturesOrderSearchRequest request) {
        try {
            Page<UsdtFuturesOrderManagement> orders = orderManagementService.searchHistoryOrders(request);
            // 转换为显示响应DTO
            Page<FuturesOrderDisplayResponse> responsePage = orders.map(FuturesOrderDisplayResponse::from);
            return Result.success(responsePage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取委托详情", notes = "根据ID获取委托详细信息")
    public Result<FuturesOrderDisplayResponse> getOrderDetail(
            @ApiParam(value = "委托ID", required = true) @PathVariable Long id) {
        try {
            UsdtFuturesOrderManagement order = orderManagementService.getOrderById(id);
            FuturesOrderDisplayResponse response = FuturesOrderDisplayResponse.from(order);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/force-cancel")
    @ApiOperation(value = "强制撤销订单", notes = "强制撤销指定的U本位永续合约订单")
    public Result<Void> forceCancelOrder(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            orderManagementService.forceCancelOrder(orderId);
            return Result.success("订单强制撤销成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














