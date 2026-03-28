/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.inviterebate.dto.request.SetRebateConfigRequest;
import com.cryptotrade.inviterebate.entity.InviteRelation;
import com.cryptotrade.inviterebate.entity.InviteReward;
import com.cryptotrade.inviterebate.entity.RebateConfig;
import com.cryptotrade.inviterebate.entity.RebateRecord;
import com.cryptotrade.inviterebate.service.InviteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 邀请返佣控制器
 */
@RestController
@RequestMapping("/invite")
@Api(tags = "邀请返佣模块")
public class InviteController {
    @Autowired
    private InviteService inviteService;

    @PostMapping("/generate-link")
    @ApiOperation(value = "生成邀请链接和二维码", notes = "生成用户的个人邀请码链接和二维码")
    public Result<Map<String, String>> generateInviteLink(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            Map<String, String> result = inviteService.generateInviteLink(userId);
            return Result.success("邀请链接生成成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/status")
    @ApiOperation(value = "获取邀请奖励状态", notes = "获取用户的邀请人数、已返佣金额等信息")
    public Result<Map<String, Object>> getInviteStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            Map<String, Object> status = inviteService.getInviteStatus(userId);
            return Result.success(status);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reward")
    @ApiOperation(value = "提交奖励记录", notes = "处理一次性奖励（注册、KYC、首笔交易）")
    public Result<InviteReward> processReward(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "奖励类型", required = true, allowableValues = "REGISTER,KYC,FIRST_TRADE") @RequestParam String rewardType) {
        try {
            InviteReward reward = inviteService.processReward(userId, rewardType);
            return Result.success("奖励处理成功", reward);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/commission")
    @ApiOperation(value = "查询返佣金额和比例", notes = "查询用户的返佣金额和交易手续费返佣比例")
    public Result<Map<String, Object>> getCommission(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "状态", example = "SETTLED") @RequestParam(required = false) String status,
            @ApiParam(value = "周期", example = "DAILY") @RequestParam(required = false) String period) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            
            // 返佣配置
            RebateConfig config = inviteService.getUserRebateConfig(userId);
            result.put("rebateConfig", config);
            
            // 返佣记录
            List<RebateRecord> records = inviteService.getRebateRecords(userId, status, period);
            result.put("rebateRecords", records);
            
            // 累计返佣金额
            BigDecimal totalRebate = inviteService.getTotalRebateAmount(userId);
            result.put("totalRebate", totalRebate);
            
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/settings")
    @ApiOperation(value = "设置返佣配置", notes = "设置用户的返佣比例和奖励规则")
    public Result<RebateConfig> setRebateConfig(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody SetRebateConfigRequest request) {
        try {
            RebateConfig config = new RebateConfig();
            config.setSpotRebateRate(request.getSpotRebateRate());
            config.setFuturesUsdtRebateRate(request.getFuturesUsdtRebateRate());
            config.setFuturesCoinRebateRate(request.getFuturesCoinRebateRate());
            config.setCopyTradingRebateRate(request.getCopyTradingRebateRate());
            config.setOptionsRebateRate(request.getOptionsRebateRate());
            config.setDailyRebateLimit(request.getDailyRebateLimit());
            config.setRebatePeriod(request.getRebatePeriod());
            config.setRebateMethod(request.getRebateMethod());
            
            config = inviteService.setUserRebateConfig(userId, config);
            return Result.success("返佣配置设置成功", config);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/invited-users")
    @ApiOperation(value = "获取邀请的好友列表", notes = "获取用户邀请的所有好友信息")
    public Result<List<InviteRelation>> getInvitedUsers(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<InviteRelation> users = inviteService.getInvitedUsers(userId);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















