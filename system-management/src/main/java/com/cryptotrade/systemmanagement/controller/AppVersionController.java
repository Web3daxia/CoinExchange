/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.AppVersion;
import com.cryptotrade.systemmanagement.service.AppVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * APP版本管理Controller
 */
@RestController
@RequestMapping("/admin/system/app-version")
@Api(tags = "APP版本管理")
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    @PostMapping("/create")
    @ApiOperation(value = "创建APP版本", notes = "创建新的APP版本")
    public Result<AppVersion> createVersion(
            @ApiParam(value = "APP版本信息", required = true) @RequestBody AppVersion appVersion) {
        try {
            AppVersion version = appVersionService.createVersion(appVersion);
            return Result.success("APP版本创建成功", version);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "更新APP版本", notes = "更新APP版本信息")
    public Result<AppVersion> updateVersion(
            @ApiParam(value = "版本ID", required = true) @PathVariable Long id,
            @ApiParam(value = "APP版本信息", required = true) @RequestBody AppVersion appVersion) {
        try {
            AppVersion version = appVersionService.updateVersion(id, appVersion);
            return Result.success("APP版本更新成功", version);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除APP版本", notes = "删除APP版本")
    public Result<Void> deleteVersion(
            @ApiParam(value = "版本ID", required = true) @PathVariable Long id) {
        try {
            appVersionService.deleteVersion(id);
            return Result.success("APP版本删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有APP版本", notes = "获取所有APP版本列表")
    public Result<List<AppVersion>> getAllVersions() {
        try {
            List<AppVersion> versions = appVersionService.getAllVersions();
            return Result.success(versions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/platform/{platform}")
    @ApiOperation(value = "根据平台获取版本列表", notes = "根据平台获取APP版本列表")
    public Result<List<AppVersion>> getVersionsByPlatform(
            @ApiParam(value = "平台", required = true) @PathVariable String platform) {
        try {
            List<AppVersion> versions = appVersionService.getVersionsByPlatform(platform);
            return Result.success(versions);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/platform/{platform}/latest")
    @ApiOperation(value = "获取最新版本", notes = "获取指定平台的最新版本")
    public Result<AppVersion> getLatestVersion(
            @ApiParam(value = "平台", required = true) @PathVariable String platform) {
        try {
            AppVersion version = appVersionService.getLatestVersion(platform);
            if (version == null) {
                return Result.error("未找到该平台的版本");
            }
            return Result.success(version);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取版本详情", notes = "根据ID获取APP版本详情")
    public Result<AppVersion> getVersionById(
            @ApiParam(value = "版本ID", required = true) @PathVariable Long id) {
        try {
            AppVersion version = appVersionService.getVersionById(id);
            return Result.success(version);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














