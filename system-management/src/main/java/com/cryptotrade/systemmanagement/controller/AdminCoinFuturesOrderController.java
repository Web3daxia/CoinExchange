/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.CoinFuturesOrderSearchRequest;
import com.cryptotrade.systemmanagement.dto.response.FuturesOrderDisplayResponse;
import com.cryptotrade.systemmanagement.entity.CoinFuturesOrderManagement;
import com.cryptotrade.systemmanagement.service.CoinFuturesOrderManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 后台币本位永续合约委托管理Controller
 */
@RestController
@RequestMapping("/admin/coin-futures-order")
@Api(tags = "币本位永续合约委托管理")
public class AdminCoinFuturesOrderController {

    @Autowired
    private CoinFuturesOrderManagementService orderManagementService;

    @PostMapping("/current/search")
    @ApiOperation(value = "搜索当前委托", notes = "搜索币本位永续合约当前委托订单")
    public Result<Page<FuturesOrderDisplayResponse>> searchCurrentOrders(
            @ApiParam(value = "搜索条件", required = true) @RequestBody CoinFuturesOrderSearchRequest request) {
        try {
            Page<CoinFuturesOrderManagement> orders = orderManagementService.searchCurrentOrders(request);
            // 转换为显示响应DTO（需要创建一个适配方法，因为实体类型不同）
            Page<FuturesOrderDisplayResponse> responsePage = orders.map(this::convertToDisplayResponse);
            return Result.success(responsePage);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/history/search")
    @ApiOperation(value = "搜索历史委托", notes = "搜索币本位永续合约历史委托订单")
    public Result<Page<FuturesOrderDisplayResponse>> searchHistoryOrders(
            @ApiParam(value = "搜索条件", required = true) @RequestBody CoinFuturesOrderSearchRequest request) {
        try {
            Page<CoinFuturesOrderManagement> orders = orderManagementService.searchHistoryOrders(request);
            // 转换为显示响应DTO
            Page<FuturesOrderDisplayResponse> responsePage = orders.map(this::convertToDisplayResponse);
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
            CoinFuturesOrderManagement order = orderManagementService.getOrderById(id);
            FuturesOrderDisplayResponse response = convertToDisplayResponse(order);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/force-cancel")
    @ApiOperation(value = "强制撤销订单", notes = "强制撤销指定的币本位永续合约订单")
    public Result<Void> forceCancelOrder(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            orderManagementService.forceCancelOrder(orderId);
            return Result.success("订单强制撤销成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 将币本位合约委托实体转换为显示响应DTO
     */
    private FuturesOrderDisplayResponse convertToDisplayResponse(CoinFuturesOrderManagement entity) {
        FuturesOrderDisplayResponse response = new FuturesOrderDisplayResponse();
        response.setId(entity.getId());
        response.setOrderId(entity.getOrderId());
        response.setOrderNo(entity.getOrderNo());
        response.setUserId(entity.getUserId());
        response.setMemberName(entity.getMemberName());
        response.setContractName(entity.getContractName());
        // 格式化委托类型显示（买入开多、买入开空等）
        response.setOrderTypeDisplay(com.cryptotrade.systemmanagement.util.OrderDisplayUtil.formatOrderDirection(entity.getOrderDirection(), entity.getSide()));
        response.setOrderType(entity.getOrderType());
        response.setOrderStatusDisplay(com.cryptotrade.systemmanagement.util.OrderDisplayUtil.formatOrderStatus(entity.getStatus()));
        response.setOrderStatus(entity.getStatus());
        response.setIsLiquidation(entity.getIsLiquidation());
        response.setQuantity(entity.getQuantity());
        response.setTriggerPrice(entity.getTriggerPrice());
        response.setPrice(entity.getPrice());
        response.setFilledPrice(entity.getFilledPrice());
        response.setFilledQuantity(entity.getFilledQuantity());
        response.setOpenFee(entity.getOpenFee());
        response.setMarginFrozen(entity.getMarginFrozen());
        response.setCreatedAt(entity.getCreatedAt());
        response.setTriggeredAt(entity.getTriggeredAt());
        response.setFilledAt(entity.getFilledAt());
        response.setCancelledAt(entity.getCancelledAt());
        return response;
    }
}

