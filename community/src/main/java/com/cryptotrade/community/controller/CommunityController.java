/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.community.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.community.entity.*;
import com.cryptotrade.community.service.CommunityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 社区控制器
 */
@RestController
@RequestMapping("/community")
@Api(tags = "社区模块")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @PostMapping("/create-content")
    @ApiOperation(value = "创建内容", notes = "用户创建新内容（文章、图片、视频）")
    public Result<CommunityContent> createContent(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody CommunityContent content) {
        try {
            content = communityService.createContent(userId, content);
            return Result.success("内容创建成功", content);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取内容列表", notes = "支持分类和类型筛选")
    public Result<List<CommunityContent>> getContentList(
            @ApiParam(value = "分类") @RequestParam(required = false) String category,
            @ApiParam(value = "内容类型") @RequestParam(required = false) String contentType) {
        try {
            List<CommunityContent> contents = communityService.getContentList(category, contentType);
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/detail/{contentId}")
    @ApiOperation(value = "获取内容详情", notes = "获取某篇内容的详细信息")
    public Result<CommunityContent> getContentDetail(
            @ApiParam(value = "内容ID", required = true) @PathVariable Long contentId) {
        try {
            CommunityContent content = communityService.getContentDetail(contentId);
            return Result.success(content);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/comment")
    @ApiOperation(value = "添加评论", notes = "用户在内容下进行评论")
    public Result<CommunityComment> addComment(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "内容ID", required = true) @RequestParam Long contentId,
            @ApiParam(value = "评论内容", required = true) @RequestParam String commentText,
            @ApiParam(value = "父评论ID") @RequestParam(required = false) Long parentId) {
        try {
            CommunityComment comment = communityService.addComment(contentId, userId, commentText, parentId);
            return Result.success("评论成功", comment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/like")
    @ApiOperation(value = "点赞内容", notes = "用户点赞内容")
    public Result<Void> likeContent(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "内容ID", required = true) @RequestParam Long contentId) {
        try {
            communityService.likeContent(contentId, userId);
            return Result.success("点赞成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/follow")
    @ApiOperation(value = "关注用户", notes = "用户关注内容发布者")
    public Result<Void> followUser(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "被关注者ID", required = true) @RequestParam Long followingId) {
        try {
            communityService.followUser(userId, followingId);
            return Result.success("关注成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reward")
    @ApiOperation(value = "赞赏内容", notes = "用户赞赏内容创作者")
    public Result<CommunityReward> rewardContent(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "内容ID", required = true) @RequestParam Long contentId,
            @ApiParam(value = "币种", required = true) @RequestParam String currency,
            @ApiParam(value = "金额", required = true) @RequestParam BigDecimal amount) {
        try {
            CommunityReward reward = communityService.rewardContent(contentId, userId, currency, amount);
            return Result.success("赞赏成功", reward);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/leaderboard")
    @ApiOperation(value = "获取排行榜", notes = "获取社区活跃用户排行榜")
    public Result<List<Map<String, Object>>> getLeaderboard(
            @ApiParam(value = "排行类型", example = "reward") @RequestParam(required = false) String rankType,
            @ApiParam(value = "限制数量", example = "10") @RequestParam(required = false) Integer limit) {
        try {
            List<Map<String, Object>> leaderboard = communityService.getLeaderboard(rankType, limit);
            return Result.success(leaderboard);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















