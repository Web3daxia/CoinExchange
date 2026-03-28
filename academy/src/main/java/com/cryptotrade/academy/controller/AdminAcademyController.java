/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.academy.controller;

import com.cryptotrade.academy.entity.AcademyContent;
import com.cryptotrade.academy.service.AcademyService;
import com.cryptotrade.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台学院管理控制器
 */
@RestController
@RequestMapping("/admin/academy")
@Api(tags = "后台学院管理")
public class AdminAcademyController {
    @Autowired
    private AcademyService academyService;

    @PostMapping("/create")
    @ApiOperation(value = "创建学院内容", notes = "创建新的教程、公告等")
    public Result<AcademyContent> createContent(@RequestBody AcademyContent content) {
        try {
            content = academyService.createContent(content);
            return Result.success("内容创建成功", content);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/update/{contentId}")
    @ApiOperation(value = "更新学院内容", notes = "更新内容信息")
    public Result<AcademyContent> updateContent(
            @ApiParam(value = "内容ID", required = true) @PathVariable Long contentId,
            @RequestBody AcademyContent content) {
        try {
            content = academyService.updateContent(contentId, content);
            return Result.success("内容更新成功", content);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/delete/{contentId}")
    @ApiOperation(value = "删除学院内容", notes = "删除指定内容")
    public Result<Void> deleteContent(
            @ApiParam(value = "内容ID", required = true) @PathVariable Long contentId) {
        try {
            academyService.deleteContent(contentId);
            return Result.success("内容删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















