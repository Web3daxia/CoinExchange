/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.NewsInteraction;
import com.cryptotrade.newsfeed.service.NewsInteractionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻互动Controller
 */
@RestController
@RequestMapping("/api/news/interaction")
@Api(tags = "新闻互动")
public class NewsInteractionController {

    @Autowired
    private NewsInteractionService interactionService;

    @PostMapping("/like/{newsId}")
    @ApiOperation(value = "点赞/取消点赞", notes = "点赞或取消点赞新闻")
    public Result<NewsInteraction> likeNews(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            NewsInteraction interaction = interactionService.likeNews(userId, newsId);
            return Result.success(interaction != null ? "点赞成功" : "取消点赞成功", interaction);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/share/{newsId}")
    @ApiOperation(value = "分享新闻", notes = "分享新闻到指定平台")
    public Result<NewsInteraction> shareNews(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "分享平台") @RequestParam(required = false) String platform) {
        try {
            NewsInteraction interaction = interactionService.shareNews(userId, newsId, platform);
            return Result.success("分享成功", interaction);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/like-count/{newsId}")
    @ApiOperation(value = "获取点赞数", notes = "获取新闻的点赞数")
    public Result<Long> getLikeCount(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            Long count = interactionService.getLikeCount(newsId);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check-like/{newsId}")
    @ApiOperation(value = "检查是否已点赞", notes = "检查用户是否已点赞该新闻")
    public Result<Boolean> checkLike(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            boolean isLiked = interactionService.isLiked(userId, newsId);
            return Result.success(isLiked);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














