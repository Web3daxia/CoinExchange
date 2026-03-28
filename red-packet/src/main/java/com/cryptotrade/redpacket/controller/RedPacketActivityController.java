/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.redpacket.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.redpacket.dto.request.RedPacketActivityCreateRequest;
import com.cryptotrade.redpacket.entity.RedPacketActivity;
import com.cryptotrade.redpacket.service.RedPacketActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 红包活动管理Controller（后台管理）
 */
@RestController
@RequestMapping("/admin/red-packet/activity")
@Api(tags = "红包活动管理")
public class RedPacketActivityController {

    @Autowired
    private RedPacketActivityService activityService;

    @PostMapping("/create")
    @ApiOperation(value = "创建红包活动", notes = "创建新的红包活动")
    public Result<RedPacketActivity> createActivity(
            @ApiParam(value = "活动信息", required = true) @RequestBody RedPacketActivityCreateRequest request) {
        try {
            RedPacketActivity activity = activityService.createActivity(request);
            return Result.success("红包活动创建成功", activity);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取所有红包活动", notes = "获取所有红包活动列表")
    public Result<List<RedPacketActivity>> getAllActivities() {
        try {
            List<RedPacketActivity> activities = activityService.getAllActivities();
            return Result.success(activities);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/active")
    @ApiOperation(value = "获取活跃的红包活动", notes = "获取所有活跃的红包活动")
    public Result<List<RedPacketActivity>> getActiveActivities() {
        try {
            List<RedPacketActivity> activities = activityService.getActiveActivities();
            return Result.success(activities);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{activityId}")
    @ApiOperation(value = "获取活动详情", notes = "根据ID获取红包活动详情")
    public Result<RedPacketActivity> getActivityById(
            @ApiParam(value = "活动ID", required = true) @PathVariable Long activityId) {
        try {
            RedPacketActivity activity = activityService.getActivityById(activityId);
            return Result.success(activity);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














