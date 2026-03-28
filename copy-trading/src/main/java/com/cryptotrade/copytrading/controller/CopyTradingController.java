/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.copytrading.dto.request.FollowTraderRequest;
import com.cryptotrade.copytrading.dto.request.ReviewTraderApplicationRequest;
import com.cryptotrade.copytrading.dto.request.TraderApplicationRequest;
import com.cryptotrade.copytrading.dto.request.UpdateCopyTradingSettingsRequest;
import com.cryptotrade.copytrading.dto.response.TraderPerformanceResponse;
import com.cryptotrade.copytrading.dto.response.TraderResponse;
import com.cryptotrade.copytrading.entity.CopyTradingRelation;
import com.cryptotrade.copytrading.entity.Trader;
import com.cryptotrade.copytrading.entity.TraderApplication;
import com.cryptotrade.copytrading.entity.TraderPerformance;
import com.cryptotrade.copytrading.service.CopyTradingService;
import com.cryptotrade.copytrading.service.TraderLevelService;
import com.cryptotrade.copytrading.service.TraderPerformanceService;
import com.cryptotrade.copytrading.service.TraderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 跟单交易控制器
 */
@RestController
@RequestMapping("/copy-trading")
@Api(tags = "跟单交易模块")
public class CopyTradingController {

    @Autowired
    private TraderService traderService;

    @Autowired
    private CopyTradingService copyTradingService;

    @Autowired
    private TraderPerformanceService traderPerformanceService;

    @Autowired
    private TraderLevelService traderLevelService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ==================== 带单员管理 ====================

    @PostMapping("/trader/apply")
    @ApiOperation(value = "申请成为带单员", notes = "提交带单员申请")
    public Result<TraderApplication> applyForTrader(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody TraderApplicationRequest request) {
        try {
            TraderApplication application = traderService.applyForTrader(
                    userId, request.getTraderType(), request.getContactInfo(),
                    request.getAssetProof(), request.getTotalAssets());
            return Result.success("申请提交成功", application);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/market")
    @ApiOperation(value = "查询交易员列表", notes = "查询平台上的现货和合约交易员")
    public Result<List<TraderResponse>> getTraders(
            @ApiParam(value = "交易员类型（可选）") @RequestParam(required = false) String traderType) {
        try {
            List<Trader> traders = traderService.getPublicTraders(traderType);
            List<TraderResponse> responses = traders.stream()
                    .map(this::convertToTraderResponse)
                    .collect(Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/trader/{traderId}")
    @ApiOperation(value = "查询带单员详情", notes = "查询单个带单员的详细信息")
    public Result<TraderResponse> getTrader(
            @ApiParam(value = "带单员ID", required = true) @PathVariable Long traderId) {
        try {
            Trader trader = traderService.getTrader(traderId);
            return Result.success(convertToTraderResponse(trader));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/trader/invite")
    @ApiOperation(value = "生成邀请码", notes = "带单员生成私域跟单邀请码")
    public Result<String> generateInviteCode(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            String inviteCode = traderService.generateInviteCode(userId);
            return Result.success("邀请码生成成功", inviteCode);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/trader/review")
    @ApiOperation(value = "审核带单员申请", notes = "后台审核带单员申请（管理员接口）")
    public Result<TraderResponse> reviewTraderApplication(
            @ApiParam(value = "管理员ID", required = true) @RequestHeader("X-User-Id") Long reviewerId,
            @Valid @RequestBody ReviewTraderApplicationRequest request) {
        try {
            Trader trader = traderService.reviewTraderApplication(
                    request.getApplicationId(), request.getApproved(),
                    request.getRejectReason(), reviewerId);
            if (trader != null) {
                return Result.success("审核成功", convertToTraderResponse(trader));
            } else {
                return Result.success("审核完成（已拒绝）", null);
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 跟单操作 ====================

    @PostMapping("/follow")
    @ApiOperation(value = "跟随带单员", notes = "跟随某个带单员的操作（现货或合约）")
    public Result<CopyTradingRelation> followTrader(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody FollowTraderRequest request) {
        try {
            // 将邀请码添加到settings中
            Map<String, Object> settings = request.getSettings() != null ? 
                    new HashMap<>(request.getSettings()) : new HashMap<>();
            if (request.getInviteCode() != null) {
                settings.put("inviteCode", request.getInviteCode());
            }

            CopyTradingRelation relation = copyTradingService.followTrader(
                    userId, request.getTraderId(), request.getMarketType(), request.getCopyType(),
                    request.getAllocationAmount(), request.getCopyRatio(), settings);
            return Result.success("跟单成功", relation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/stop")
    @ApiOperation(value = "停止跟单", notes = "停止跟随带单员")
    public Result<Void> stopCopyTrading(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long relationId) {
        try {
            copyTradingService.stopCopyTrading(userId, relationId);
            return Result.success("停止跟单成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/pause")
    @ApiOperation(value = "暂停跟单", notes = "暂停跟随带单员")
    public Result<Void> pauseCopyTrading(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long relationId) {
        try {
            copyTradingService.pauseCopyTrading(userId, relationId);
            return Result.success("暂停跟单成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/resume")
    @ApiOperation(value = "恢复跟单", notes = "恢复跟随带单员")
    public Result<Void> resumeCopyTrading(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long relationId) {
        try {
            copyTradingService.resumeCopyTrading(userId, relationId);
            return Result.success("恢复跟单成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/account/status")
    @ApiOperation(value = "查询跟单状态", notes = "查询当前跟单状态")
    public Result<List<CopyTradingRelation>> getCopyTradingStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        try {
            List<CopyTradingRelation> relations = copyTradingService.getCopyTradingStatus(userId);
            return Result.success(relations);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/settings")
    @ApiOperation(value = "设置跟单参数", notes = "设置跟单参数（资金分配、止损、止盈等）")
    public Result<CopyTradingRelation> updateCopyTradingSettings(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long relationId,
            @Valid @RequestBody UpdateCopyTradingSettingsRequest request) {
        try {
            Map<String, Object> settings = new HashMap<>();
            if (request.getAllocationAmount() != null) {
                settings.put("allocationAmount", request.getAllocationAmount());
            }
            if (request.getStopLossPrice() != null) {
                settings.put("stopLossPrice", request.getStopLossPrice());
            }
            if (request.getTakeProfitPrice() != null) {
                settings.put("takeProfitPrice", request.getTakeProfitPrice());
            }
            if (request.getCopyRatio() != null) {
                settings.put("copyRatio", request.getCopyRatio());
            }
            if (request.getSettings() != null) {
                settings.putAll(request.getSettings());
            }

            CopyTradingRelation relation = copyTradingService.updateCopyTradingSettings(userId, relationId, settings);
            return Result.success("设置更新成功", relation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 带单员表现 ====================

    @GetMapping("/leaderboard")
    @ApiOperation(value = "查询带单员排行榜", notes = "查询带单员的绩效排名")
    public Result<List<Map<String, Object>>> getTraderLeaderboard(
            @ApiParam(value = "排序方式（AUM、PROFIT、WIN_RATE等）") @RequestParam(required = false, defaultValue = "totalAum") String sortBy,
            @ApiParam(value = "返回数量") @RequestParam(required = false, defaultValue = "100") Integer limit) {
        try {
            List<Map<String, Object>> leaderboard = traderPerformanceService.getTraderLeaderboard(sortBy, limit);
            return Result.success(leaderboard);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/leaderboard/{traderId}")
    @ApiOperation(value = "查询单个带单员详细表现", notes = "查询单个带单员的详细表现数据")
    public Result<Map<String, Object>> getTraderPerformanceDetails(
            @ApiParam(value = "带单员ID", required = true) @PathVariable Long traderId) {
        try {
            Map<String, Object> details = traderPerformanceService.getTraderPerformanceDetails(traderId);
            return Result.success(details);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/leaderboard/{traderId}/performance")
    @ApiOperation(value = "获取带单员历史业绩数据", notes = "获取带单员的历史业绩数据（收益率、最大回撤、夏普比率等）")
    public Result<List<TraderPerformanceResponse>> getTraderPerformance(
            @ApiParam(value = "带单员ID", required = true) @PathVariable Long traderId,
            @ApiParam(value = "周期类型（可选）") @RequestParam(required = false) String periodType) {
        try {
            List<TraderPerformance> performances = traderPerformanceService.getTraderPerformance(traderId, periodType);
            List<TraderPerformanceResponse> responses = performances.stream()
                    .map(this::convertToPerformanceResponse)
                    .collect(Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/leaderboard/performance/period")
    @ApiOperation(value = "按周期查询带单员表现", notes = "按周期（如日、月、年）查询带单员的表现")
    public Result<List<TraderPerformanceResponse>> getTraderPerformanceByPeriod(
            @ApiParam(value = "带单员ID", required = true) @RequestParam Long traderId,
            @ApiParam(value = "周期类型: DAILY, WEEKLY, MONTHLY, YEARLY", required = true) @RequestParam String periodType) {
        try {
            List<TraderPerformance> performances = traderPerformanceService.getTraderPerformance(traderId, periodType);
            List<TraderPerformanceResponse> responses = performances.stream()
                    .map(this::convertToPerformanceResponse)
                    .collect(Collectors.toList());
            return Result.success(responses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private TraderResponse convertToTraderResponse(Trader trader) {
        TraderResponse response = new TraderResponse();
        response.setId(trader.getId());
        response.setUserId(trader.getUserId());
        response.setTraderType(trader.getTraderType());
        response.setLevel(trader.getLevel());
        response.setStatus(trader.getStatus());
        response.setPublicEnabled(trader.getPublicEnabled());
        response.setPrivateEnabled(trader.getPrivateEnabled());
        response.setSubscriptionFee(trader.getSubscriptionFee());
        response.setProfitShareRate(trader.getProfitShareRate());
        response.setTotalFollowers(trader.getTotalFollowers());
        response.setTotalAum(trader.getTotalAum());
        response.setTotalProfit(trader.getTotalProfit());
        response.setTotalLoss(trader.getTotalLoss());
        response.setWinRate(trader.getWinRate());
        response.setSharpeRatio(trader.getSharpeRatio());
        response.setMaxDrawdown(trader.getMaxDrawdown());
        response.setLastLiquidationTime(trader.getLastLiquidationTime());
        return response;
    }

    private TraderPerformanceResponse convertToPerformanceResponse(TraderPerformance performance) {
        TraderPerformanceResponse response = new TraderPerformanceResponse();
        response.setTraderId(performance.getTraderId());
        response.setPeriodType(performance.getPeriodType());
        response.setPeriodStart(performance.getPeriodStart());
        response.setPeriodEnd(performance.getPeriodEnd());
        response.setTotalProfit(performance.getTotalProfit());
        response.setTotalLoss(performance.getTotalLoss());
        response.setNetProfit(performance.getNetProfit());
        response.setReturnRate(performance.getReturnRate());
        response.setWinRate(performance.getWinRate());
        response.setTotalTrades(performance.getTotalTrades());
        response.setWinningTrades(performance.getWinningTrades());
        response.setLosingTrades(performance.getLosingTrades());
        response.setAvgProfit(performance.getAvgProfit());
        response.setAvgLoss(performance.getAvgLoss());
        response.setProfitLossRatio(performance.getProfitLossRatio());
        response.setSharpeRatio(performance.getSharpeRatio());
        response.setMaxDrawdown(performance.getMaxDrawdown());
        response.setTotalAum(performance.getTotalAum());
        response.setTotalFollowers(performance.getTotalFollowers());
        response.setDailyAvgTrades(performance.getDailyAvgTrades());
        return response;
    }
}

