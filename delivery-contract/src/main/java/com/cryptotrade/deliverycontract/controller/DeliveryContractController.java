/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.deliverycontract.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.deliverycontract.dto.request.CreateDeliveryOrderRequest;
import com.cryptotrade.deliverycontract.dto.request.TransferRequest;
import com.cryptotrade.deliverycontract.dto.response.AccountBalanceResponse;
import com.cryptotrade.deliverycontract.dto.response.DeliveryOrderResponse;
import com.cryptotrade.deliverycontract.dto.response.DeliveryPositionResponse;
import com.cryptotrade.deliverycontract.entity.*;
import com.cryptotrade.deliverycontract.service.DeliveryAccountService;
import com.cryptotrade.deliverycontract.service.DeliveryContractService;
import com.cryptotrade.deliverycontract.service.DeliveryRiskManagementService;
import com.cryptotrade.deliverycontract.service.DeliverySettlementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 交割合约控制器
 */
@RestController
@RequestMapping("/delivery-contract")
@Api(tags = "交割合约模块")
public class DeliveryContractController {
    @Autowired
    private DeliveryContractService deliveryContractService;

    @Autowired
    private DeliveryAccountService deliveryAccountService;

    @Autowired
    private DeliverySettlementService deliverySettlementService;

    @Autowired
    private DeliveryRiskManagementService deliveryRiskManagementService;

    @PostMapping("/order")
    @ApiOperation(value = "创建订单", notes = "创建交割合约订单")
    public Result<DeliveryOrderResponse> createOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody CreateDeliveryOrderRequest request) {
        try {
            DeliveryOrder order = new DeliveryOrder();
            BeanUtils.copyProperties(request, order);
            order = deliveryContractService.createOrder(userId, request.getContractId(), order);
            
            DeliveryOrderResponse response = new DeliveryOrderResponse();
            BeanUtils.copyProperties(order, response);
            return Result.success("订单创建成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/{orderId}/cancel")
    @ApiOperation(value = "取消订单", notes = "取消未成交的订单")
    public Result<Void> cancelOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            deliveryContractService.cancelOrder(userId, orderId);
            return Result.success("订单取消成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/orders")
    @ApiOperation(value = "获取订单列表", notes = "查询用户的订单列表")
    public Result<List<DeliveryOrder>> getOrders(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单状态") @RequestParam(required = false) String status) {
        try {
            List<DeliveryOrder> orders = deliveryContractService.getOrders(userId, status);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/positions")
    @ApiOperation(value = "获取持仓列表", notes = "查询用户的持仓列表")
    public Result<List<DeliveryPositionResponse>> getPositions(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "合约ID筛选") @RequestParam(required = false) Long contractId,
            @ApiParam(value = "方向筛选: LONG, SHORT") @RequestParam(required = false) String side,
            @ApiParam(value = "状态筛选: ACTIVE, CLOSED, LIQUIDATED") @RequestParam(required = false) String status,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<DeliveryPosition> positions = deliveryContractService.getPositions(userId);
            
            // 应用筛选条件
            if (contractId != null) {
                positions = positions.stream().filter(p -> p.getContractId().equals(contractId)).collect(Collectors.toList());
            }
            if (side != null) {
                positions = positions.stream().filter(p -> side.equals(p.getSide())).collect(Collectors.toList());
            }
            if (status != null) {
                positions = positions.stream().filter(p -> status.equals(p.getStatus())).collect(Collectors.toList());
            }
            
            List<DeliveryPositionResponse> responses = positions.stream().map(p -> {
                DeliveryPositionResponse response = new DeliveryPositionResponse();
                BeanUtils.copyProperties(p, response);
                return response;
            }).collect(Collectors.toList());
            
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/position/{positionId}/close")
    @ApiOperation(value = "平仓", notes = "平掉指定持仓")
    public Result<DeliveryOrder> closePosition(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "持仓ID", required = true) @PathVariable Long positionId,
            @ApiParam(value = "平仓数量") @RequestParam(required = false) BigDecimal quantity) {
        try {
            DeliveryOrder order = deliveryContractService.closePosition(userId, positionId, quantity);
            return Result.success("平仓成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market-data/{contractId}")
    @ApiOperation(value = "获取行情数据", notes = "获取合约的实时行情数据")
    public Result<DeliveryMarketData> getMarketData(
            @ApiParam(value = "合约ID", required = true) @PathVariable Long contractId) {
        try {
            DeliveryMarketData marketData = deliveryContractService.getMarketData(contractId);
            return Result.success(marketData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/contracts")
    @ApiOperation(value = "获取合约列表", notes = "获取可交易的交割合约列表")
    public Result<List<DeliveryContract>> getContracts(
            @ApiParam(value = "合约类型筛选: USDT_MARGINED, COIN_MARGINED") @RequestParam(required = false) String contractType,
            @ApiParam(value = "交割周期筛选: HOURLY, DAILY, WEEKLY, MONTHLY") @RequestParam(required = false) String deliveryCycle,
            @ApiParam(value = "状态筛选: ACTIVE, INACTIVE, SETTLED") @RequestParam(required = false) String status,
            @ApiParam(value = "标的资产筛选") @RequestParam(required = false) String underlyingAsset,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            // TODO: 实现合约列表查询
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/contracts/{contractId}")
    @ApiOperation(value = "获取合约详情", notes = "获取指定合约的详细信息")
    public Result<DeliveryContract> getContractDetail(
            @ApiParam(value = "合约ID", required = true) @PathVariable Long contractId) {
        try {
            // TODO: 实现合约详情查询
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/balance")
    @ApiOperation(value = "查询合约账户余额", notes = "查询用户的合约账户余额信息")
    public Result<AccountBalanceResponse> getAccountBalance(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "币种筛选") @RequestParam(required = false) String currency) {
        try {
            Map<String, Object> balanceData = deliveryAccountService.getAccountBalance(userId, currency);
            AccountBalanceResponse response = new AccountBalanceResponse();
            // TODO: 转换数据
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/account/transfer/in")
    @ApiOperation(value = "转账到合约账户", notes = "从现货账户转账到合约账户")
    public Result<Map<String, Object>> transferIn(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody TransferRequest request) {
        try {
            Map<String, Object> result = deliveryAccountService.transferIn(userId, request.getCurrency(), request.getAmount());
            return Result.success("转账成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/account/transfer/out")
    @ApiOperation(value = "从合约账户转出", notes = "从合约账户转账到现货账户")
    public Result<Map<String, Object>> transferOut(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody TransferRequest request) {
        try {
            Map<String, Object> result = deliveryAccountService.transferOut(userId, request.getCurrency(), request.getAmount());
            return Result.success("转账成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/transfer/history")
    @ApiOperation(value = "查询转账记录", notes = "查询账户转账历史记录")
    public Result<List<Map<String, Object>>> getTransferHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "币种筛选") @RequestParam(required = false) String currency,
            @ApiParam(value = "方向筛选: IN, OUT") @RequestParam(required = false) String direction,
            @ApiParam(value = "开始时间") @RequestParam(required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) String endTime,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<Map<String, Object>> history = deliveryAccountService.getTransferHistory(userId, currency, direction, startTime, endTime);
            return Result.success(history);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/settlements")
    @ApiOperation(value = "查询结算记录", notes = "查询用户的合约结算记录")
    public Result<List<DeliverySettlement>> getSettlements(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "合约ID筛选") @RequestParam(required = false) Long contractId,
            @ApiParam(value = "结算类型筛选: DELIVERY, LIQUIDATION, MANUAL") @RequestParam(required = false) String settlementType,
            @ApiParam(value = "状态筛选: PENDING, SETTLED, FAILED") @RequestParam(required = false) String status,
            @ApiParam(value = "开始时间") @RequestParam(required = false) String startTime,
            @ApiParam(value = "结束时间") @RequestParam(required = false) String endTime,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量") @RequestParam(required = false, defaultValue = "20") Integer size) {
        try {
            List<DeliverySettlement> settlements = deliverySettlementService.getSettlements(userId, contractId, settlementType, status, startTime, endTime);
            return Result.success(settlements);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/risk-status")
    @ApiOperation(value = "查询账户风险状态", notes = "查询用户的账户风险状态")
    public Result<Map<String, Object>> getAccountRiskStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            Map<String, Object> riskStatus = deliveryRiskManagementService.getAccountRiskStatus(userId);
            return Result.success(riskStatus);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}



