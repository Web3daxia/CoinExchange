/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.News;
import com.cryptotrade.newsfeed.repository.NewsRepository;
import com.cryptotrade.newsfeed.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 后台管理 - 新闻管理Controller
 */
@RestController
@RequestMapping("/api/admin/news")
@Api(tags = "后台管理-新闻管理")
public class AdminNewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/list")
    @ApiOperation(value = "获取新闻列表", notes = "获取新闻列表（支持按状态筛选）")
    public Result<Page<News>> getNewsList(
            @ApiParam(value = "状态", required = false) @RequestParam(required = false) String status,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") Integer page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<News> newsList = newsService.getNewsList(status, page, size);
            return Result.success(newsList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{newsId}")
    @ApiOperation(value = "获取新闻详情", notes = "获取新闻详情")
    public Result<News> getNewsById(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            News news = newsService.getNewsById(newsId);
            return Result.success(news);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/review/{newsId}")
    @ApiOperation(value = "审核新闻", notes = "审核新闻（通过/拒绝）")
    public Result<News> reviewNews(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "审核人ID", required = true) @RequestHeader("adminId") Long reviewerId,
            @ApiParam(value = "审核状态: APPROVED, REJECTED, PUBLISHED", required = true) @RequestParam String status,
            @ApiParam(value = "审核备注") @RequestParam(required = false) String remark) {
        try {
            News news = newsService.reviewNews(newsId, reviewerId, status, remark);
            return Result.success("审核成功", news);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/hot/{newsId}")
    @ApiOperation(value = "设置热门新闻", notes = "设置或取消热门新闻")
    public Result<Void> setHotNews(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "是否热门", required = true) @RequestParam Boolean isHot) {
        try {
            newsService.setHotNews(newsId, isHot);
            return Result.success("设置成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/recommended/{newsId}")
    @ApiOperation(value = "设置推荐新闻", notes = "设置或取消推荐新闻")
    public Result<Void> setRecommendedNews(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "是否推荐", required = true) @RequestParam Boolean isRecommended) {
        try {
            newsService.setRecommendedNews(newsId, isRecommended);
            return Result.success("设置成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/category/{newsId}")
    @ApiOperation(value = "设置新闻分类", notes = "设置新闻的分类")
    public Result<News> setNewsCategory(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "分类ID", required = true) @RequestParam Long categoryId) {
        try {
            News news = newsService.getNewsById(newsId);
            news.setCategoryId(categoryId);
            News saved = newsRepository.save(news);
            return Result.success("分类设置成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{newsId}")
    @ApiOperation(value = "删除新闻", notes = "删除新闻")
    public Result<Void> deleteNews(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            if (!newsRepository.existsById(newsId)) {
                return Result.error("新闻不存在: " + newsId);
            }
            newsRepository.deleteById(newsId);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














