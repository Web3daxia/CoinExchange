/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.miningpool.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.miningpool.dto.request.HashrateRentalRequest;
import com.cryptotrade.miningpool.entity.HashrateRental;
import com.cryptotrade.miningpool.service.HashrateRentalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 算力租赁Controller
 */
@RestController
@RequestMapping("/api/mining/rental")
@Api(tags = "算力租赁")
public class HashrateRentalController {

    @Autowired
    private HashrateRentalService rentalService;

    @PostMapping("/rent")
    @ApiOperation(value = "租赁算力", notes = "用户租赁算力")
    public Result<HashrateRental> rentHashrate(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId,
            @ApiParam(value = "租赁信息", required = true) @RequestBody HashrateRentalRequest request) {
        try {
            HashrateRental rental = rentalService.rentHashrate(userId, request);
            return Result.success("算力租赁成功", rental);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my-rentals")
    @ApiOperation(value = "获取我的租赁记录", notes = "获取当前用户的所有算力租赁记录")
    public Result<List<HashrateRental>> getMyRentals(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("userId") Long userId) {
        try {
            List<HashrateRental> rentals = rentalService.getUserRentals(userId);
            return Result.success(rentals);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/cost")
    @ApiOperation(value = "计算租赁费用", notes = "计算算力租赁的费用")
    public Result<BigDecimal> calculateCost(
            @ApiParam(value = "矿池ID", required = true) @RequestParam Long poolId,
            @ApiParam(value = "算力数量", required = true) @RequestParam BigDecimal hashrateAmount,
            @ApiParam(value = "租赁周期（天）", required = true) @RequestParam Integer rentalPeriod) {
        try {
            BigDecimal cost = rentalService.calculateRentalCost(poolId, hashrateAmount, rentalPeriod);
            return Result.success(cost);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














