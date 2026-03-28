/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.SpotOrderSearchRequest;
import com.cryptotrade.systemmanagement.entity.SpotOrderManagement;
import com.cryptotrade.systemmanagement.service.SpotOrderManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 后台现货订单管理Controller
 */
@RestController
@RequestMapping("/admin/spot-order")
@Api(tags = "现货订单管理")
public class AdminSpotOrderController {

    @Autowired
    private SpotOrderManagementService spotOrderManagementService;

    @PostMapping("/current/search")
    @ApiOperation(value = "搜索当前委托", notes = "搜索现货当前委托订单")
    public Result<Page<SpotOrderManagement>> searchCurrentOrders(
            @ApiParam(value = "搜索条件", required = true) @RequestBody SpotOrderSearchRequest request) {
        try {
            Page<SpotOrderManagement> orders = spotOrderManagementService.searchCurrentOrders(request);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/history/search")
    @ApiOperation(value = "搜索历史委托", notes = "搜索现货历史委托订单")
    public Result<Page<SpotOrderManagement>> searchHistoryOrders(
            @ApiParam(value = "搜索条件", required = true) @RequestBody SpotOrderSearchRequest request) {
        try {
            Page<SpotOrderManagement> orders = spotOrderManagementService.searchHistoryOrders(request);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取订单详情", notes = "根据ID获取订单详细信息")
    public Result<SpotOrderManagement> getOrderDetail(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long id) {
        try {
            SpotOrderManagement order = spotOrderManagementService.getOrderDetail(id);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/cancel")
    @ApiOperation(value = "撤销订单", notes = "撤销指定的现货订单")
    public Result<Void> cancelOrder(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            spotOrderManagementService.cancelOrder(orderId);
            return Result.success("订单撤销成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














