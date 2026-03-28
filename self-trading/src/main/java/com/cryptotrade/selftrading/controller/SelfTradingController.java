/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.selftrading.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.selftrading.dto.request.*;
import com.cryptotrade.selftrading.entity.*;
import com.cryptotrade.selftrading.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自选交易控制器
 */
@RestController
@RequestMapping("/self-trading")
@Api(tags = "自选交易模块")
public class SelfTradingController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private TradingAdService tradingAdService;

    @Autowired
    private SelfTradingOrderService selfTradingOrderService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private MerchantRatingService merchantRatingService;

    // ==================== 商家管理 ====================

    @PostMapping("/merchant/apply")
    @ApiOperation(value = "申请成为商家", notes = "用户申请成为商家")
    public Result<MerchantApplication> applyForMerchant(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody MerchantApplicationRequest request) {
        try {
            MerchantApplication application = merchantService.applyForMerchant(
                    userId, request.getMerchantName(), request.getAvatar(),
                    request.getSignature(), request.getBio(), request.getCountry(),
                    request.getRegion(), request.getAssetProof(), request.getTotalAssets());
            return Result.success("申请提交成功", application);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market")
    @ApiOperation(value = "获取商家列表", notes = "获取平台上所有活跃商家的广告列表")
    public Result<List<Merchant>> getMerchants(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String level) {
        try {
            Map<String, Object> filters = new HashMap<>();
            if (country != null) {
                filters.put("country", country);
            }
            if (level != null) {
                filters.put("level", level);
            }
            List<Merchant> merchants = merchantService.getMerchants(filters);
            return Result.success(merchants);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/merchant/{merchantId}")
    @ApiOperation(value = "查询商家详情", notes = "查询商家详细信息")
    public Result<Merchant> getMerchant(
            @ApiParam(value = "商家ID", required = true) @PathVariable Long merchantId) {
        try {
            Merchant merchant = merchantService.getMerchant(merchantId);
            return Result.success(merchant);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/merchant/follow")
    @ApiOperation(value = "关注/取消关注商家", notes = "关注或取消关注商家")
    public Result<Void> followMerchant(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long merchantId,
            @RequestParam Boolean follow) {
        try {
            merchantService.followMerchant(userId, merchantId, follow);
            return Result.success(follow ? "关注成功" : "取消关注成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 广告管理 ====================

    @PostMapping("/create-ad")
    @ApiOperation(value = "创建广告", notes = "商家创建交易广告")
    public Result<TradingAd> createAd(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateAdRequest request) {
        try {
            // TODO: 从userId获取merchantId
            Long merchantId = userId; // 简化处理，实际应该查询
            TradingAd ad = tradingAdService.createAd(
                    merchantId, request.getAdType(), request.getCryptoCurrency(),
                    request.getFiatCurrency(), request.getPrice(), request.getMinAmount(),
                    request.getMaxAmount(), request.getPaymentMethods(), request.getSettings());
            return Result.success("广告创建成功", ad);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/ad/{adId}")
    @ApiOperation(value = "查看广告详情", notes = "查看商家广告详情")
    public Result<TradingAd> getAd(
            @ApiParam(value = "广告ID", required = true) @PathVariable Long adId) {
        try {
            TradingAd ad = tradingAdService.getAd(adId);
            return Result.success(ad);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 订单管理 ====================

    @PostMapping("/order")
    @ApiOperation(value = "创建订单", notes = "用户发起购买请求")
    public Result<SelfTradingOrder> createOrder(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateOrderRequest request) {
        try {
            SelfTradingOrder order = selfTradingOrderService.createOrder(
                    userId, request.getAdId(), request.getCryptoAmount());
            return Result.success("订单创建成功", order);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/orders")
    @ApiOperation(value = "查询订单历史", notes = "获取用户的交易历史记录")
    public Result<List<SelfTradingOrder>> getOrders(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false) String status) {
        try {
            List<SelfTradingOrder> orders = selfTradingOrderService.getUserOrders(userId, status);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/confirm-payment")
    @ApiOperation(value = "确认支付", notes = "确认支付并上传支付凭证")
    public Result<Void> confirmPayment(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long orderId,
            @RequestParam String paymentProof) {
        try {
            selfTradingOrderService.confirmPayment(userId, orderId, paymentProof);
            return Result.success("支付确认成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/order/release")
    @ApiOperation(value = "放币", notes = "商家放币")
    public Result<Void> releaseCrypto(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long orderId) {
        try {
            selfTradingOrderService.releaseCrypto(userId, orderId);
            return Result.success("放币成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 聊天对话 ====================

    @PostMapping("/chat/send")
    @ApiOperation(value = "发送消息", notes = "发送聊天消息")
    public Result<ChatMessage> sendMessage(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody SendMessageRequest request) {
        try {
            ChatMessage message = chatService.sendMessage(
                    request.getOrderId(), userId, request.getReceiverId(),
                    request.getMessageType(), request.getContent(), request.getFileUrl());
            return Result.success("消息发送成功", message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/chat/{orderId}")
    @ApiOperation(value = "查询聊天记录", notes = "查询订单的聊天记录")
    public Result<List<ChatMessage>> getChatMessages(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            List<ChatMessage> messages = chatService.getOrderMessages(orderId);
            return Result.success(messages);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 评价系统 ====================

    @PostMapping("/rating")
    @ApiOperation(value = "创建评价", notes = "用户对商家进行评价")
    public Result<MerchantRating> createRating(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateRatingRequest request) {
        try {
            MerchantRating rating = merchantRatingService.createRating(
                    userId, request.getMerchantId(), request.getOrderId(),
                    request.getRating(), request.getComment(), request.getIsAnonymous());
            return Result.success("评价成功", rating);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/rating")
    @ApiOperation(value = "查询商家评价", notes = "获取商家的评价和评分")
    public Result<List<MerchantRating>> getRatings(
            @RequestParam Long merchantId) {
        try {
            List<MerchantRating> ratings = merchantRatingService.getMerchantRatings(merchantId);
            return Result.success(ratings);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/merchant-level")
    @ApiOperation(value = "获取商家等级信息", notes = "获取商家等级、完成订单量等信息")
    public Result<Merchant> getMerchantLevel(
            @RequestParam Long merchantId) {
        try {
            Merchant merchant = merchantService.getMerchant(merchantId);
            return Result.success(merchant);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















