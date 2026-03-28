/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.futures.usdt.dto.request.AdjustLeverageRequest;
import com.cryptotrade.futures.usdt.dto.request.CreateFuturesAdvancedOrderRequest;
import com.cryptotrade.futures.usdt.dto.request.CreateFuturesOrderRequest;
import com.cryptotrade.futures.usdt.dto.request.CreateSegmentRequest;
import com.cryptotrade.futures.usdt.dto.request.SetMarginModeRequest;
import com.cryptotrade.common.dto.MarketDepthRequest;
import com.cryptotrade.common.dto.MarketDepthResponse;
import com.cryptotrade.futures.usdt.dto.response.FuturesMarketDataResponse;
import com.cryptotrade.futures.usdt.dto.response.FuturesOrderResponse;
import com.cryptotrade.futures.usdt.dto.response.FuturesPositionResponse;
import com.cryptotrade.futures.usdt.entity.FuturesAdvancedOrder;
import com.cryptotrade.futures.usdt.entity.FuturesOrder;
import com.cryptotrade.futures.usdt.entity.GradientRule;
import com.cryptotrade.futures.usdt.entity.MarginMode;
import com.cryptotrade.futures.usdt.entity.MarginSegment;
import com.cryptotrade.futures.usdt.service.FuturesAdvancedOrderService;
import com.cryptotrade.futures.usdt.service.FuturesMarketDataService;
import com.cryptotrade.futures.usdt.service.FuturesOrderService;
import com.cryptotrade.futures.usdt.service.ContractCalculatorService;
import com.cryptotrade.futures.usdt.service.GradientService;
import com.cryptotrade.futures.usdt.service.MarginModeService;
import com.cryptotrade.futures.usdt.service.PositionService;
import com.cryptotrade.futures.usdt.dto.request.ContractCalculatorRequest;
import com.cryptotrade.futures.usdt.dto.response.ContractCalculatorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/futures")
@Api(tags = "USDT本位永续合约模块")
public class FuturesController {

    @Autowired
    private FuturesMarketDataService futuresMarketDataService;

    @Autowired
    private FuturesOrderService futuresOrderService;

    @Autowired
    private FuturesAdvancedOrderService futuresAdvancedOrderService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private MarginModeService marginModeService;

    @Autowired
    private GradientService gradientService;
    
    @Autowired
    private ContractCalculatorService contractCalculatorService;

    // ==================== 市场数据接口 ====================

    @GetMapping("/market/**")
    @ApiOperation(value = "获取市场数据", notes = "获取指定合约的实时价格、标记价格、指数价格、资金费率等。支持路径变量形式：/market/BTC/USDT 或 URL 编码：/market/BTC%2FUSDT")
    public Result<FuturesMarketDataResponse> getMarketData(HttpServletRequest request) {
        try {
            // 从请求路径中提取交易对名称
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            String path = requestURI.substring(contextPath.length());
            
            // 提取 /futures/market/ 之后的部分
            String prefix = "/futures/market/";
            int prefixIndex = path.indexOf(prefix);
            if (prefixIndex == -1) {
                return Result.error("无效的请求路径");
            }
            
            String pair = path.substring(prefixIndex + prefix.length());
            
            // 处理 URL 编码（如果存在）
            try {
                pair = java.net.URLDecoder.decode(pair, "UTF-8");
            } catch (Exception e) {
                // 如果解码失败，使用原始值
            }
            
            // 如果路径中包含 /chart 或 /depth，说明是其他接口，不应该到这里
            if (pair.contains("/chart") || pair.contains("/depth")) {
                return Result.error("请使用正确的接口路径");
            }
            
            FuturesMarketDataResponse marketData = futuresMarketDataService.getMarketData(pair);
            return Result.success(marketData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/market")
    @ApiOperation(value = "获取市场数据（查询参数方式）", notes = "获取指定合约的实时价格、标记价格、指数价格、资金费率等（兼容旧版本）")
    public Result<FuturesMarketDataResponse> getMarketDataByParam(
            @ApiParam(value = "交易对名称", required = true, example = "BTC/USDT") @RequestParam("pair") String pair) {
        try {
            FuturesMarketDataResponse marketData = futuresMarketDataService.getMarketData(pair);
            return Result.success(marketData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market/{pair}/chart")
    @ApiOperation(value = "获取K线数据", notes = "获取指定合约的K线数据")
    public Result<List<Map<String, Object>>> getKlineData(
            @ApiParam(value = "交易对名称", required = true, example = "BTC/USDT") @PathVariable String pair,
            @ApiParam(value = "时间粒度", example = "1h") @RequestParam(defaultValue = "1h") String interval,
            @ApiParam(value = "数据条数", example = "100") @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<Map<String, Object>> klineData = futuresMarketDataService.getKlineData(pair, interval, limit);
            return Result.success(klineData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market/{pair}/depth")
    @ApiOperation(value = "获取市场深度", notes = "获取指定合约的买卖盘深度（支持买卖盘切换和价格精度）")
    public Result<MarketDepthResponse> getMarketDepth(
            @ApiParam(value = "交易对名称", required = true, example = "BTC/USDT") @PathVariable String pair,
            @ApiParam(value = "深度条数", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "盘口类型：BUY（买盘）, SELL（卖盘）, BOTH（买卖盘，默认）", example = "BOTH") @RequestParam(defaultValue = "BOTH") String depthType,
            @ApiParam(value = "价格精度（可选，如0.01表示保留两位小数）", example = "0.01") @RequestParam(required = false) String pricePrecision) {
        try {
            MarketDepthRequest request = new MarketDepthRequest();
            request.setPair(pair);
            request.setLimit(limit);
            request.setDepthType(depthType);
            request.setPricePrecision(pricePrecision);
            MarketDepthResponse depth = futuresMarketDataService.getMarketDepthEnhanced(request);
            return Result.success(depth);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 订单管理接口 ====================

    @PostMapping("/order")
    @ApiOperation(value = "提交订单", notes = "提交永续合约订单（通用接口）")
    public Result<FuturesOrderResponse> createOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesOrderRequest request) {
        try {
            FuturesOrder order = futuresOrderService.createOrder(userId, request);
            FuturesOrderResponse response = futuresOrderService.getOrderStatus(userId, order.getId());
            return Result.success("订单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/market")
    @ApiOperation(value = "提交市价单", notes = "提交永续合约市价单")
    public Result<FuturesOrderResponse> createMarketOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesOrderRequest request) {
        try {
            request.setOrderType("MARKET");
            FuturesOrder order = futuresOrderService.createOrder(userId, request);
            FuturesOrderResponse response = futuresOrderService.getOrderStatus(userId, order.getId());
            return Result.success("市价单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/limit")
    @ApiOperation(value = "提交限价单", notes = "提交永续合约限价单")
    public Result<FuturesOrderResponse> createLimitOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesOrderRequest request) {
        try {
            request.setOrderType("LIMIT");
            FuturesOrder order = futuresOrderService.createOrder(userId, request);
            FuturesOrderResponse response = futuresOrderService.getOrderStatus(userId, order.getId());
            return Result.success("限价单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/stop-loss")
    @ApiOperation(value = "提交止损单", notes = "提交永续合约止损单")
    public Result<FuturesOrderResponse> createStopLossOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesOrderRequest request) {
        try {
            request.setOrderType("STOP_LOSS");
            FuturesOrder order = futuresOrderService.createOrder(userId, request);
            FuturesOrderResponse response = futuresOrderService.getOrderStatus(userId, order.getId());
            return Result.success("止损单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/take-profit")
    @ApiOperation(value = "提交止盈单", notes = "提交永续合约止盈单")
    public Result<FuturesOrderResponse> createTakeProfitOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesOrderRequest request) {
        try {
            request.setOrderType("TAKE_PROFIT");
            FuturesOrder order = futuresOrderService.createOrder(userId, request);
            FuturesOrderResponse response = futuresOrderService.getOrderStatus(userId, order.getId());
            return Result.success("止盈单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/stop-limit")
    @ApiOperation(value = "提交止损限价单", notes = "提交永续合约止损限价单")
    public Result<FuturesOrderResponse> createStopLimitOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesOrderRequest request) {
        try {
            request.setOrderType("STOP_LIMIT");
            FuturesOrder order = futuresOrderService.createOrder(userId, request);
            FuturesOrderResponse response = futuresOrderService.getOrderStatus(userId, order.getId());
            return Result.success("止损限价单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/conditional")
    @ApiOperation(value = "提交条件单", notes = "提交永续合约条件单")
    public Result<FuturesOrderResponse> createConditionalOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesOrderRequest request) {
        try {
            request.setOrderType("CONDITIONAL");
            FuturesOrder order = futuresOrderService.createOrder(userId, request);
            FuturesOrderResponse response = futuresOrderService.getOrderStatus(userId, order.getId());
            return Result.success("条件单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/cancel")
    @ApiOperation(value = "取消订单", notes = "取消待成交状态的订单")
    public Result<Void> cancelOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId) {
        try {
            futuresOrderService.cancelOrder(userId, orderId);
            return Result.success("订单取消成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/status")
    @ApiOperation(value = "查询订单状态", notes = "根据订单ID查询订单详情")
    public Result<FuturesOrderResponse> getOrderStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId) {
        try {
            FuturesOrderResponse order = futuresOrderService.getOrderStatus(userId, orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/history")
    @ApiOperation(value = "查询订单历史", notes = "查询用户的订单历史记录")
    public Result<Page<FuturesOrderResponse>> getOrderHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<FuturesOrderResponse> orders = futuresOrderService.getOrderHistory(userId, pageable);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 高级订单接口 ====================

    @PostMapping("/order/advanced-limit")
    @ApiOperation(value = "提交高级限价单", notes = "提交高级限价单（支持GTC/IOC/FOK）")
    public Result<FuturesAdvancedOrder> createAdvancedLimitOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesAdvancedOrderRequest request) {
        try {
            request.setOrderType("ADVANCED_LIMIT");
            FuturesAdvancedOrder order = futuresAdvancedOrderService.createAdvancedOrder(userId, request);
            return Result.success("高级限价单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/trailing")
    @ApiOperation(value = "提交追踪委托", notes = "提交追踪委托订单")
    public Result<FuturesAdvancedOrder> createTrailingOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesAdvancedOrderRequest request) {
        try {
            request.setOrderType("TRAILING");
            FuturesAdvancedOrder order = futuresAdvancedOrderService.createAdvancedOrder(userId, request);
            return Result.success("追踪委托创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/trailing-limit")
    @ApiOperation(value = "提交追逐限价单", notes = "提交追逐限价单订单")
    public Result<FuturesAdvancedOrder> createTrailingLimitOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesAdvancedOrderRequest request) {
        try {
            request.setOrderType("TRAILING_LIMIT");
            FuturesAdvancedOrder order = futuresAdvancedOrderService.createAdvancedOrder(userId, request);
            return Result.success("追逐限价单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/iceberg")
    @ApiOperation(value = "提交冰山策略", notes = "提交冰山策略订单")
    public Result<FuturesAdvancedOrder> createIcebergOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesAdvancedOrderRequest request) {
        try {
            request.setOrderType("ICEBERG");
            FuturesAdvancedOrder order = futuresAdvancedOrderService.createAdvancedOrder(userId, request);
            return Result.success("冰山策略创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/segmented")
    @ApiOperation(value = "提交分段委托", notes = "提交分段委托订单")
    public Result<FuturesAdvancedOrder> createSegmentedOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesAdvancedOrderRequest request) {
        try {
            request.setOrderType("SEGMENTED");
            FuturesAdvancedOrder order = futuresAdvancedOrderService.createAdvancedOrder(userId, request);
            return Result.success("分段委托创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/time-weighted")
    @ApiOperation(value = "提交分时委托", notes = "提交分时委托订单")
    public Result<FuturesAdvancedOrder> createTimeWeightedOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateFuturesAdvancedOrderRequest request) {
        try {
            request.setOrderType("TIME_WEIGHTED");
            FuturesAdvancedOrder order = futuresAdvancedOrderService.createAdvancedOrder(userId, request);
            return Result.success("分时委托创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/advanced/cancel")
    @ApiOperation(value = "取消高级订单", notes = "取消指定的高级订单")
    public Result<Void> cancelAdvancedOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @RequestParam Long orderId) {
        try {
            futuresAdvancedOrderService.cancelAdvancedOrder(userId, orderId);
            return Result.success("高级订单取消成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/advanced/list")
    @ApiOperation(value = "查询高级订单列表", notes = "查询用户的所有高级订单")
    public Result<List<FuturesAdvancedOrder>> getUserAdvancedOrders(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<FuturesAdvancedOrder> orders = futuresAdvancedOrderService.getUserAdvancedOrders(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 仓位管理接口 ====================

    @GetMapping("/account/positions")
    @ApiOperation(value = "查询仓位列表", notes = "查询用户的所有持仓仓位，支持按交易对筛选")
    public Result<List<FuturesPositionResponse>> getPositions(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "交易对名称（可选，用于筛选）", required = false) @RequestParam(required = false) String pairName) {
        try {
            List<FuturesPositionResponse> positions = positionService.getPositions(userId, pairName);
            return Result.success(positions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/position/{positionId}")
    @ApiOperation(value = "查询单个仓位", notes = "根据仓位ID查询仓位详情")
    public Result<FuturesPositionResponse> getPosition(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "仓位ID", required = true) @PathVariable Long positionId) {
        try {
            FuturesPositionResponse position = positionService.getPosition(userId, positionId);
            return Result.success(position);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/account/margin")
    @ApiOperation(value = "设置保证金模式", notes = "设置用户的保证金模式（全仓/逐仓/分仓/组合）")
    public Result<MarginMode> setMarginMode(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody SetMarginModeRequest request) {
        try {
            MarginMode marginMode = marginModeService.setMarginMode(userId, request);
            return Result.success("保证金模式设置成功", marginMode);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/account/segment")
    @ApiOperation(value = "创建分仓", notes = "在分仓模式下创建新的分仓")
    public Result<MarginSegment> createSegment(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateSegmentRequest request) {
        try {
            MarginSegment segment = marginModeService.createSegment(userId, request);
            return Result.success("分仓创建成功", segment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/segments")
    @ApiOperation(value = "查询分仓列表", notes = "查询用户的所有分仓")
    public Result<List<MarginSegment>> getSegments(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<MarginSegment> segments = marginModeService.getSegments(userId);
            return Result.success(segments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 梯度模式接口 ====================

    @GetMapping("/account/gradient")
    @ApiOperation(value = "查询梯度杠杆规则", notes = "根据交易对和仓位大小查询适用的梯度杠杆规则")
    public Result<GradientRule> getGradientRule(
            @ApiParam(value = "交易对名称", required = true, example = "BTC/USDT") @RequestParam String pairName,
            @ApiParam(value = "仓位大小", required = true, example = "1.0") @RequestParam java.math.BigDecimal positionSize) {
        try {
            java.util.Optional<GradientRule> ruleOpt = gradientService.getGradientRule(pairName, positionSize);
            if (ruleOpt.isPresent()) {
                return Result.success(ruleOpt.get());
            } else {
                return Result.error("未找到适用的梯度规则");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/gradient-adjust")
    @ApiOperation(value = "提交梯度杠杆调整", notes = "调整仓位的杠杆倍数")
    public Result<Void> adjustLeverage(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody AdjustLeverageRequest request) {
        try {
            // 验证仓位是否属于该用户
            FuturesPositionResponse position = positionService.getPosition(userId, request.getPositionId());
            if (position == null) {
                return Result.error("仓位不存在或无权操作");
            }
            
            gradientService.adjustLeverage(request.getPositionId(), request.getNewLeverage());
            return Result.success("杠杆调整成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 合约计算器接口 ====================

    @PostMapping("/calculator")
    @ApiOperation(value = "合约计算器", notes = "计算USDT本位合约的盈亏、平仓价格、强平价格等")
    public Result<ContractCalculatorResponse> calculate(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody ContractCalculatorRequest request) {
        try {
            ContractCalculatorResponse response = contractCalculatorService.calculate(request);
            return Result.success("计算成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

