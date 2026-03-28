/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.academy.controller;

import com.cryptotrade.academy.entity.AcademyComment;
import com.cryptotrade.academy.entity.AcademyContent;
import com.cryptotrade.academy.entity.AcademyFeedback;
import com.cryptotrade.academy.service.AcademyService;
import com.cryptotrade.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学院控制器
 */
@RestController
@RequestMapping("/academy")
@Api(tags = "学院模块")
public class AcademyController {
    @Autowired
    private AcademyService academyService;

    @GetMapping("/list")
    @ApiOperation(value = "获取学院内容列表", notes = "支持分类和语言版本筛选")
    public Result<List<AcademyContent>> getContentList(
            @ApiParam(value = "内容类型", example = "TUTORIAL") @RequestParam(required = false) String contentType,
            @ApiParam(value = "分类", example = "交易帮助") @RequestParam(required = false) String category,
            @ApiParam(value = "语言代码", example = "en") @RequestParam(required = false) String languageCode) {
        try {
            List<AcademyContent> contents = academyService.getContentList(contentType, category, languageCode);
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/detail/{contentId}")
    @ApiOperation(value = "获取内容详情", notes = "获取特定内容的详细信息")
    public Result<AcademyContent> getContentDetail(
            @ApiParam(value = "内容ID", required = true) @PathVariable Long contentId) {
        try {
            AcademyContent content = academyService.getContentDetail(contentId);
            return Result.success(content);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/comment")
    @ApiOperation(value = "添加评论", notes = "用户在文章或视频下进行评论")
    public Result<AcademyComment> addComment(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "内容ID", required = true) @RequestParam Long contentId,
            @ApiParam(value = "评论内容", required = true) @RequestParam String commentText,
            @ApiParam(value = "父评论ID") @RequestParam(required = false) Long parentId) {
        try {
            AcademyComment comment = academyService.addComment(contentId, userId, commentText, parentId);
            return Result.success("评论成功", comment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/comments/{contentId}")
    @ApiOperation(value = "获取评论列表", notes = "获取文章或视频下的所有评论")
    public Result<List<AcademyComment>> getComments(
            @ApiParam(value = "内容ID", required = true) @PathVariable Long contentId) {
        try {
            List<AcademyComment> comments = academyService.getComments(contentId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/feedback")
    @ApiOperation(value = "提交反馈", notes = "用户提交对文章或视频的反馈或评分")
    public Result<AcademyFeedback> submitFeedback(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "内容ID", required = true) @RequestParam Long contentId,
            @ApiParam(value = "评分", example = "5") @RequestParam(required = false) Integer rating,
            @ApiParam(value = "反馈内容") @RequestParam(required = false) String feedbackText) {
        try {
            AcademyFeedback feedback = academyService.submitFeedback(contentId, userId, rating, feedbackText);
            return Result.success("反馈提交成功", feedback);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















