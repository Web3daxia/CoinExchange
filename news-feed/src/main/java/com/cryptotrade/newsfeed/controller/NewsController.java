/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.News;
import com.cryptotrade.newsfeed.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 新闻Controller
 */
@RestController
@RequestMapping("/api/news")
@Api(tags = "快讯")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/list")
    @ApiOperation(value = "获取新闻列表", notes = "获取新闻列表（分页）")
    public Result<Page<News>> getNewsList(
            @ApiParam(value = "状态", defaultValue = "PUBLISHED") @RequestParam(required = false, defaultValue = "PUBLISHED") String status,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<News> newsList = newsService.getNewsList(status, page, size);
            return Result.success(newsList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/hot")
    @ApiOperation(value = "获取热门新闻", notes = "获取热门新闻列表")
    public Result<Page<News>> getHotNews(
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<News> newsList = newsService.getHotNews(page, size);
            return Result.success(newsList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/recommended")
    @ApiOperation(value = "获取推荐新闻", notes = "获取推荐新闻列表")
    public Result<Page<News>> getRecommendedNews(
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<News> newsList = newsService.getRecommendedNews(page, size);
            return Result.success(newsList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索新闻", notes = "根据关键词搜索新闻")
    public Result<Page<News>> searchNews(
            @ApiParam(value = "关键词", required = true) @RequestParam String keyword,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<News> newsList = newsService.searchNews(keyword, page, size);
            return Result.success(newsList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{newsId}")
    @ApiOperation(value = "获取新闻详情", notes = "根据ID获取新闻详情，并增加阅读量。支持语言参数，自动返回对应语言的翻译版本")
    public Result<News> getNewsById(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "语言代码，如: zh-CN, en-US") @RequestHeader(value = "Accept-Language", required = false, defaultValue = "en-US") String languageCode,
            @ApiParam(value = "用户ID") @RequestHeader(value = "userId", required = false) Long userId) {
        try {
            // 根据语言获取新闻（自动返回翻译版本）
            News news = newsService.getNewsById(newsId, languageCode);
            // 增加阅读量
            newsService.incrementViewCount(newsId);
            // 如果用户已登录，记录浏览
            if (userId != null) {
                // TODO: 调用interactionService.viewNews(userId, newsId);
            }
            return Result.success(news);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

