/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.NewsFavorite;
import com.cryptotrade.newsfeed.service.NewsFavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻收藏Controller
 */
@RestController
@RequestMapping("/api/news/favorite")
@Api(tags = "新闻收藏")
public class NewsFavoriteController {

    @Autowired
    private NewsFavoriteService favoriteService;

    @PostMapping("/{newsId}")
    @ApiOperation(value = "收藏新闻", notes = "收藏新闻")
    public Result<NewsFavorite> favoriteNews(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            NewsFavorite favorite = favoriteService.favoriteNews(userId, newsId);
            return Result.success("收藏成功", favorite);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{newsId}")
    @ApiOperation(value = "取消收藏", notes = "取消收藏新闻")
    public Result<Void> unfavoriteNews(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            favoriteService.unfavoriteNews(userId, newsId);
            return Result.success("取消收藏成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-favorites")
    @ApiOperation(value = "获取我的收藏", notes = "获取当前用户的新闻收藏列表")
    public Result<Page<NewsFavorite>> getMyFavorites(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<NewsFavorite> favorites = favoriteService.getUserFavorites(userId, page, size);
            return Result.success(favorites);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check/{newsId}")
    @ApiOperation(value = "检查是否已收藏", notes = "检查新闻是否已收藏")
    public Result<Boolean> checkFavorite(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            boolean isFavorited = favoriteService.isFavorited(userId, newsId);
            return Result.success(isFavorited);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














