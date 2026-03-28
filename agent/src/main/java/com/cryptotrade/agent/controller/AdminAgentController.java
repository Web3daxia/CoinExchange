/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.agent.controller;

import com.cryptotrade.agent.entity.Agent;
import com.cryptotrade.agent.entity.AgentRebateConfig;
import com.cryptotrade.agent.service.AdminAgentService;
import com.cryptotrade.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 后台代理商管理控制器
 */
@RestController
@RequestMapping("/admin/agent")
@Api(tags = "后台代理商管理")
public class AdminAgentController {
    @Autowired
    private AdminAgentService adminAgentService;

    @PostMapping("/{agentId}/review")
    @ApiOperation(value = "审核代理商申请", notes = "审核代理商申请，决定是否通过")
    public Result<Agent> reviewAgentApplication(
            @ApiParam(value = "代理商ID", required = true) @PathVariable Long agentId,
            @ApiParam(value = "管理员ID", required = true) @RequestHeader("X-Admin-Id") Long adminUserId,
            @ApiParam(value = "审核状态", required = true, allowableValues = "APPROVED,REJECTED") @RequestParam String status,
            @ApiParam(value = "审核备注") @RequestParam(required = false) String remark) {
        try {
            Agent agent = adminAgentService.reviewAgentApplication(agentId, adminUserId, status, remark);
            return Result.success("审核成功", agent);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{agentId}/commission-settings")
    @ApiOperation(value = "设置代理商返佣比例", notes = "后台管理员设置代理商的返佣比例")
    public Result<AgentRebateConfig> setAgentRebateConfig(
            @ApiParam(value = "代理商ID", required = true) @PathVariable Long agentId,
            @RequestBody AgentRebateConfig config) {
        try {
            AgentRebateConfig result = adminAgentService.setAgentRebateConfig(agentId, config);
            return Result.success("返佣配置设置成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{agentId}/level")
    @ApiOperation(value = "调整代理商等级", notes = "调整代理商的等级")
    public Result<Agent> updateAgentLevel(
            @ApiParam(value = "代理商ID", required = true) @PathVariable Long agentId,
            @ApiParam(value = "等级", required = true, allowableValues = "LEVEL1,LEVEL2,LEVEL3") @RequestParam String level) {
        try {
            Agent agent = adminAgentService.updateAgentLevel(agentId, level);
            return Result.success("等级调整成功", agent);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/performances")
    @ApiOperation(value = "获取代理商业绩列表", notes = "获取所有代理商的业绩数据")
    public Result<List<Map<String, Object>>> getAgentPerformanceList(
            @ApiParam(value = "排序方式", example = "rebate", allowableValues = "rebate,users") @RequestParam(required = false) String sortBy,
            @ApiParam(value = "限制数量", example = "10") @RequestParam(required = false) Integer limit) {
        try {
            List<Map<String, Object>> performances = adminAgentService.getAgentPerformanceList(sortBy, limit);
            return Result.success(performances);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/ranking")
    @ApiOperation(value = "获取代理商排行榜", notes = "按返佣金额、用户数量、交易量排行")
    public Result<List<Map<String, Object>>> getAgentRanking(
            @ApiParam(value = "排行类型", example = "rebate", allowableValues = "rebate,users") @RequestParam(required = false) String rankType,
            @ApiParam(value = "限制数量", example = "10") @RequestParam(required = false) Integer limit) {
        try {
            List<Map<String, Object>> ranking = adminAgentService.getAgentRanking(rankType, limit);
            return Result.success(ranking);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}















