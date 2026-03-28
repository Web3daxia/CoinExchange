/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.pledgeloan.dto.request.TopupRequest;
import com.cryptotrade.pledgeloan.entity.PledgeLoanTopup;
import com.cryptotrade.pledgeloan.service.PledgeLoanTopupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 质押借币补仓Controller
 */
@RestController
@RequestMapping("/api/pledge-loan/topup")
@Api(tags = "质押借币-补仓")
public class PledgeLoanTopupController {

    @Autowired
    private PledgeLoanTopupService topupService;

    @PostMapping("/topup")
    @ApiOperation(value = "补仓", notes = "增加质押资产")
    public Result<PledgeLoanTopup> topup(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "补仓信息", required = true) @RequestBody TopupRequest request) {
        try {
            PledgeLoanTopup topup = topupService.topup(request.getOrderId(), userId, request.getTopupAmount());
            return Result.success("补仓成功", topup);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-topups")
    @ApiOperation(value = "获取我的补仓记录", notes = "获取当前用户的补仓记录")
    public Result<List<PledgeLoanTopup>> getMyTopups(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<PledgeLoanTopup> topups = topupService.getUserTopups(userId);
            return Result.success(topups);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














