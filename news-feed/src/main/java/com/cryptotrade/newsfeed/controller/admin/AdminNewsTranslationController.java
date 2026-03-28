/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.newsfeed.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.newsfeed.entity.News;
import com.cryptotrade.newsfeed.entity.NewsTranslation;
import com.cryptotrade.newsfeed.service.NewsTranslationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 - 新闻翻译管理Controller
 */
@RestController
@RequestMapping("/api/admin/news-translation")
@Api(tags = "后台管理-新闻翻译管理")
public class AdminNewsTranslationController {

    @Autowired
    private NewsTranslationService translationService;

    @PostMapping("/translate/{newsId}")
    @ApiOperation(value = "翻译新闻到指定语言", notes = "将新闻翻译到指定的语言")
    public Result<NewsTranslation> translateNews(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "目标语言代码，如: zh-CN, en-US", required = true) @RequestParam String targetLanguage) {
        try {
            NewsTranslation translation = translationService.translateNews(newsId, targetLanguage);
            return Result.success("翻译成功", translation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/translate-all/{newsId}")
    @ApiOperation(value = "翻译新闻到所有语言", notes = "将新闻翻译到所有支持的语言")
    public Result<Void> translateNewsToAllLanguages(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            translationService.translateNewsToAllLanguages(newsId);
            return Result.success("翻译任务已启动", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{newsId}")
    @ApiOperation(value = "获取新闻的所有翻译", notes = "获取新闻的所有语言翻译版本")
    public Result<List<NewsTranslation>> getNewsTranslations(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId) {
        try {
            List<NewsTranslation> translations = translationService.getNewsTranslations(newsId);
            return Result.success(translations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{newsId}/{languageCode}")
    @ApiOperation(value = "获取指定语言的翻译", notes = "获取新闻的指定语言翻译版本")
    public Result<NewsTranslation> getNewsTranslation(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "语言代码", required = true) @PathVariable String languageCode) {
        try {
            NewsTranslation translation = translationService.getNewsTranslation(newsId, languageCode);
            if (translation == null) {
                return Result.error("翻译不存在");
            }
            return Result.success(translation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/retranslate/{newsId}")
    @ApiOperation(value = "重新翻译新闻", notes = "删除旧翻译并重新翻译新闻")
    public Result<NewsTranslation> retranslateNews(
            @ApiParam(value = "新闻ID", required = true) @PathVariable Long newsId,
            @ApiParam(value = "语言代码", required = true) @RequestParam String languageCode) {
        try {
            NewsTranslation translation = translationService.retranslateNews(newsId, languageCode);
            return Result.success("重新翻译成功", translation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/pending")
    @ApiOperation(value = "获取待翻译的新闻列表", notes = "获取指定语言下待翻译的新闻列表")
    public Result<List<News>> getPendingTranslation(
            @ApiParam(value = "语言代码", required = true) @RequestParam String languageCode) {
        try {
            List<News> newsList = translationService.getNewsPendingTranslation(languageCode);
            return Result.success(newsList);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














