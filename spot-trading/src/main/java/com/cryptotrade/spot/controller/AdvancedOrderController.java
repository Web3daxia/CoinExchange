/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.spot.dto.request.AdvancedOrderRequest;
import com.cryptotrade.spot.entity.AdvancedOrder;
import com.cryptotrade.spot.service.AdvancedOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/spot/order/advanced")
@Api(tags = "高级订单模块")
public class AdvancedOrderController {

    @Autowired
    private AdvancedOrderService advancedOrderService;

    @PostMapping
    @ApiOperation(value = "创建高级订单", notes = "创建高级限价单、分时委托、循环委托、追踪委托、冰山策略等")
    public Result<AdvancedOrder> createAdvancedOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody AdvancedOrderRequest request) {
        try {
            AdvancedOrder order = advancedOrderService.createAdvancedOrder(userId, request);
            return Result.success("高级订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询高级订单列表", notes = "查询用户的所有高级订单")
    public Result<List<AdvancedOrder>> getUserAdvancedOrders(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<AdvancedOrder> orders = advancedOrderService.getUserAdvancedOrders(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderId}/cancel")
    @ApiOperation(value = "取消高级订单", notes = "取消指定的高级订单")
    public Result<Void> cancelAdvancedOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            advancedOrderService.cancelAdvancedOrder(userId, orderId);
            return Result.success("高级订单取消成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}


