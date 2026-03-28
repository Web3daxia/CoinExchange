/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.NewsSubscription;
import com.cryptotrade.newsfeed.service.NewsSubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 新闻订阅Controller
 */
@RestController
@RequestMapping("/api/news/subscription")
@Api(tags = "新闻订阅")
public class NewsSubscriptionController {

    @Autowired
    private NewsSubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    @ApiOperation(value = "订阅新闻", notes = "订阅新闻分类或标签")
    public Result<NewsSubscription> subscribe(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "分类ID") @RequestParam(required = false) Long categoryId,
            @ApiParam(value = "标签ID") @RequestParam(required = false) Long tagId,
            @ApiParam(value = "推送频率", defaultValue = "DAILY") @RequestParam(required = false, defaultValue = "DAILY") String pushFrequency,
            @ApiParam(value = "推送方式（JSON格式）") @RequestParam(required = false) String pushMethod) {
        try {
            NewsSubscription subscription = subscriptionService.subscribe(userId, categoryId, tagId, pushFrequency, pushMethod);
            return Result.success("订阅成功", subscription);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{subscriptionId}")
    @ApiOperation(value = "取消订阅", notes = "取消新闻订阅")
    public Result<Void> unsubscribe(
            @ApiParam(value = "订阅ID", required = true) @PathVariable Long subscriptionId) {
        try {
            subscriptionService.unsubscribe(subscriptionId);
            return Result.success("取消订阅成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-subscriptions")
    @ApiOperation(value = "获取我的订阅", notes = "获取当前用户的订阅列表")
    public Result<List<NewsSubscription>> getMySubscriptions(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<NewsSubscription> subscriptions = subscriptionService.getUserSubscriptions(userId);
            return Result.success(subscriptions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














