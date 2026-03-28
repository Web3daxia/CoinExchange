/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.controller;

import com.cryptotrade.agent.entity.Agent;
import com.cryptotrade.agent.service.AgentService;
import com.cryptotrade.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 代理商控制器
 */
@RestController
@RequestMapping("/agent")
@Api(tags = "代理商模块")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @PostMapping("/apply")
    @ApiOperation(value = "申请成为代理商", notes = "用户申请成为代理商")
    public Result<Agent> applyAgent(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @ApiParam(value = "代理商名称", required = true) @RequestParam String agentName) {
        try {
            Agent agent = agentService.applyAgent(userId, agentName);
            return Result.success("申请成功，等待审核", agent);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/generate-invite-link")
    @ApiOperation(value = "生成代理商邀请链接和二维码", notes = "生成代理商的个性化邀请链接和二维码")
    public Result<Map<String, String>> generateInviteLink(
            @ApiParam(value = "代理商ID", required = true) @RequestHeader("X-Agent-Id") Long agentId) {
        try {
            Map<String, String> result = agentService.generateInviteLink(agentId);
            return Result.success("邀请链接生成成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/users")
    @ApiOperation(value = "获取代理商邀请的所有用户信息", notes = "获取注册信息、资产信息、交易记录等")
    public Result<List<Map<String, Object>>> getAgentUsers(
            @ApiParam(value = "代理商ID", required = true) @RequestHeader("X-Agent-Id") Long agentId,
            @ApiParam(value = "用户分组", example = "HIGH_ASSET") @RequestParam(required = false) String userGroup) {
        try {
            List<Map<String, Object>> users = agentService.getAgentUsers(agentId, userGroup);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/commission")
    @ApiOperation(value = "查询代理商返佣情况", notes = "查询已发放的返佣金额、未结算的返佣金额等")
    public Result<Map<String, Object>> getAgentCommission(
            @ApiParam(value = "代理商ID", required = true) @RequestHeader("X-Agent-Id") Long agentId,
            @ApiParam(value = "状态", example = "SETTLED") @RequestParam(required = false) String status,
            @ApiParam(value = "周期", example = "DAILY") @RequestParam(required = false) String period) {
        try {
            Map<String, Object> commission = agentService.getAgentCommission(agentId, status, period);
            return Result.success(commission);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/performances")
    @ApiOperation(value = "查询代理商业绩报告", notes = "查询邀请的用户数量、交易量、返佣金额等")
    public Result<Map<String, Object>> getAgentPerformance(
            @ApiParam(value = "代理商ID", required = true) @RequestHeader("X-Agent-Id") Long agentId,
            @ApiParam(value = "周期", example = "DAILY") @RequestParam(required = false) String period) {
        try {
            Map<String, Object> performance = agentService.getAgentPerformance(agentId, period);
            return Result.success(performance);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

