/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.spot.dto.request.CreateOrderRequest;
import com.cryptotrade.common.dto.MarketDepthRequest;
import com.cryptotrade.common.dto.MarketDepthResponse;
import com.cryptotrade.spot.dto.response.KlineDataResponse;
import com.cryptotrade.spot.dto.response.MarketDataResponse;
import com.cryptotrade.spot.dto.response.OrderResponse;
import com.cryptotrade.spot.entity.SpotOrder;
import com.cryptotrade.spot.repository.SpotOrderRepository;
import com.cryptotrade.spot.repository.TradingPairRepository;
import com.cryptotrade.spot.service.MarketDataService;
import com.cryptotrade.spot.service.SpotOrderService;
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
import java.net.URLDecoder;

@RestController
@RequestMapping("/spot")
@Api(tags = "现货交易模块")
public class SpotTradingController {

    @Autowired
    private TradingPairRepository tradingPairRepository;

    @Autowired
    private SpotOrderRepository spotOrderRepository;

    @Autowired
    private SpotOrderService spotOrderService;

    @Autowired
    private MarketDataService marketDataService;

    @GetMapping("/market/**")
    @ApiOperation(value = "获取交易对市场数据", notes = "获取指定交易对的实时价格、涨跌幅、成交量等。支持路径变量形式：/market/BTC/USDT 或 URL 编码：/market/BTC%2FUSDT")
    public Result<MarketDataResponse> getMarketData(HttpServletRequest request) {
        try {
            // 从请求路径中提取交易对名称
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            String path = requestURI.substring(contextPath.length());
            
            // 提取 /spot/market/ 之后的部分
            String prefix = "/spot/market/";
            int prefixIndex = path.indexOf(prefix);
            if (prefixIndex == -1) {
                return Result.error("无效的请求路径");
            }
            
            String pair = path.substring(prefixIndex + prefix.length());
            
            // 处理 URL 编码（如果存在）
            try {
                pair = URLDecoder.decode(pair, "UTF-8");
            } catch (Exception e) {
                // 如果解码失败，使用原始值
            }
            
            // 如果路径中包含 /chart 或 /depth，说明是其他接口，不应该到这里
            if (pair.contains("/chart") || pair.contains("/depth") || pair.contains("/trades")) {
                return Result.error("请使用正确的接口路径");
            }
            
            MarketDataResponse marketData = marketDataService.getMarketData(pair);
            return Result.success(marketData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market/**/chart")
    @ApiOperation(value = "获取K线数据", notes = "获取指定交易对的K线数据。支持路径变量形式：/market/BTC/USDT/chart 或 URL 编码：/market/BTC%2FUSDT/chart")
    public Result<KlineDataResponse> getKlineData(
            HttpServletRequest request,
            @ApiParam(value = "时间粒度", example = "1h") @RequestParam(defaultValue = "1h") String interval,
            @ApiParam(value = "数据条数", example = "100") @RequestParam(defaultValue = "100") Integer limit) {
        try {
            // 从请求路径中提取交易对名称
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            String path = requestURI.substring(contextPath.length());
            
            // 提取 /spot/market/ 之后、/chart 之前的部分
            String prefix = "/spot/market/";
            String suffix = "/chart";
            int prefixIndex = path.indexOf(prefix);
            int suffixIndex = path.indexOf(suffix);
            
            if (prefixIndex == -1 || suffixIndex == -1 || suffixIndex <= prefixIndex) {
                return Result.error("无效的请求路径");
            }
            
            String pair = path.substring(prefixIndex + prefix.length(), suffixIndex);
            
            // 处理 URL 编码（如果存在）
            try {
                pair = URLDecoder.decode(pair, "UTF-8");
            } catch (Exception e) {
                // 如果解码失败，使用原始值
            }
            
            KlineDataResponse klineData = marketDataService.getKlineData(pair, interval, limit);
            return Result.success(klineData);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market/**/depth")
    @ApiOperation(value = "获取市场深度", notes = "获取指定交易对的买卖盘深度（支持买卖盘切换和价格精度）。支持路径变量形式：/market/BTC/USDT/depth 或 URL 编码：/market/BTC%2FUSDT/depth")
    public Result<MarketDepthResponse> getMarketDepth(
            HttpServletRequest request,
            @ApiParam(value = "深度条数", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "盘口类型：BUY（买盘）, SELL（卖盘）, BOTH（买卖盘，默认）", example = "BOTH") @RequestParam(defaultValue = "BOTH") String depthType,
            @ApiParam(value = "价格精度（可选，如0.01表示保留两位小数）", example = "0.01") @RequestParam(required = false) String pricePrecision) {
        try {
            // 从请求路径中提取交易对名称
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            String path = requestURI.substring(contextPath.length());
            
            // 提取 /spot/market/ 之后、/depth 之前的部分
            String prefix = "/spot/market/";
            String suffix = "/depth";
            int prefixIndex = path.indexOf(prefix);
            int suffixIndex = path.indexOf(suffix);
            
            if (prefixIndex == -1 || suffixIndex == -1 || suffixIndex <= prefixIndex) {
                return Result.error("无效的请求路径");
            }
            
            String pair = path.substring(prefixIndex + prefix.length(), suffixIndex);
            
            // 处理 URL 编码（如果存在）
            try {
                pair = URLDecoder.decode(pair, "UTF-8");
            } catch (Exception e) {
                // 如果解码失败，使用原始值
            }
            
            MarketDepthRequest depthRequest = new MarketDepthRequest();
            depthRequest.setPair(pair);
            depthRequest.setLimit(limit);
            depthRequest.setDepthType(depthType);
            depthRequest.setPricePrecision(pricePrecision);
            MarketDepthResponse depth = marketDataService.getMarketDepthEnhanced(depthRequest);
            return Result.success(depth);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order")
    @ApiOperation(value = "提交现货订单", notes = "提交买入或卖出订单")
    public Result<OrderResponse> createOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOrderRequest request) {
        try {
            SpotOrder order = spotOrderService.createOrder(userId, request);
            OrderResponse response = spotOrderService.getOrderStatus(userId, order.getId());
            return Result.success("订单提交成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/history")
    @ApiOperation(value = "查询订单历史", notes = "查询用户的订单历史记录")
    public Result<Page<OrderResponse>> getOrderHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<OrderResponse> orders = spotOrderService.getOrderHistory(userId, pageable);
        return Result.success(orders);
    }

    @GetMapping("/order/{orderId}")
    @ApiOperation(value = "查询订单状态", notes = "根据订单ID查询订单详情")
    public Result<OrderResponse> getOrderStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            OrderResponse order = spotOrderService.getOrderStatus(userId, orderId);
            return Result.success(order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/{orderId}/cancel")
    @ApiOperation(value = "取消订单", notes = "取消待成交状态的订单")
    public Result<Void> cancelOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            spotOrderService.cancelOrder(userId, orderId);
            return Result.success("订单取消成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}


