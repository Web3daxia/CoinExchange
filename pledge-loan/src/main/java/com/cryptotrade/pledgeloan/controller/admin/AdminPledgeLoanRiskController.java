/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller.admin;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.entity.PledgeLoanRiskRecord;
import com.cryptotrade.pledgeloan.service.PledgeLoanRiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 - 质押借币风险监控Controller
 */
@RestController
@RequestMapping("/api/admin/pledge-loan/risk")
@Api(tags = "后台管理-质押借币风险监控")
public class AdminPledgeLoanRiskController {

    @Autowired
    private PledgeLoanRiskService riskService;

    @GetMapping("/unprocessed")
    @ApiOperation(value = "获取未处理的风险记录", notes = "获取所有未处理的风险记录")
    public Result<List<PledgeLoanRiskRecord>> getUnprocessedRiskRecords() {
        try {
            List<PledgeLoanRiskRecord> records = riskService.getUnprocessedRiskRecords();
            return Result.success(records);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    @ApiOperation(value = "获取订单的风险记录", notes = "获取指定订单的风险记录")
    public Result<List<PledgeLoanRiskRecord>> getOrderRiskRecords(
            @ApiParam(value = "订单ID", required = true) @PathVariable Long orderId) {
        try {
            List<PledgeLoanRiskRecord> records = riskService.getOrderRiskRecords(orderId);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/notify/{riskRecordId}")
    @ApiOperation(value = "通知用户风险", notes = "向用户发送风险通知")
    public Result<Void> notifyUserRisk(
            @ApiParam(value = "风险记录ID", required = true) @PathVariable Long riskRecordId) {
        try {
            riskService.notifyUserRisk(riskRecordId);
            return Result.success("通知成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/mark-processed/{riskRecordId}")
    @ApiOperation(value = "标记风险记录为已处理", notes = "标记风险记录为已处理")
    public Result<Void> markAsProcessed(
            @ApiParam(value = "风险记录ID", required = true) @PathVariable Long riskRecordId,
            @ApiParam(value = "处理人ID", required = true) @RequestHeader("adminId") Long processorId) {
        try {
            riskService.markAsProcessed(riskRecordId, processorId);
            return Result.success("标记成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














