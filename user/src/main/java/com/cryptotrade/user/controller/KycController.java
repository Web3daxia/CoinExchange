/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.user.dto.request.KycAdvancedSubmitRequest;
import com.cryptotrade.user.dto.request.KycBasicSubmitRequest;
import com.cryptotrade.user.dto.response.KycAdvancedStatusResponse;
import com.cryptotrade.user.dto.response.KycBasicStatusResponse;
import com.cryptotrade.user.service.KycService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/kyc")
@Api(tags = "KYC身份认证模块")
public class KycController {

    @Autowired
    private KycService kycService;

    @PostMapping("/basic/submit")
    @ApiOperation(value = "提交基础身份认证", notes = "提交姓名、证件号码和正反面照片，系统会进行OCR识别")
    public Result<Void> submitBasicKyc(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @ModelAttribute KycBasicSubmitRequest request) {
        kycService.submitBasicKyc(userId, request);
        return Result.success("基础KYC认证资料已提交，正在审核中", null);
    }

    @GetMapping("/basic/status")
    @ApiOperation(value = "查询基础身份认证状态", notes = "查询当前用户的基础KYC认证状态")
    public Result<KycBasicStatusResponse> getBasicKycStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        KycBasicStatusResponse response = kycService.getBasicKycStatus(userId);
        return Result.success(response);
    }

    @PostMapping("/advanced/submit")
    @ApiOperation(value = "提交高级身份认证", notes = "提交手持证件照片和验证视频，系统会进行人脸识别比对")
    public Result<Void> submitAdvancedKyc(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @ModelAttribute KycAdvancedSubmitRequest request) {
        kycService.submitAdvancedKyc(userId, request);
        return Result.success("高级KYC认证资料已提交，正在审核中", null);
    }

    @GetMapping("/advanced/status")
    @ApiOperation(value = "查询高级身份认证状态", notes = "查询当前用户的高级KYC认证状态")
    public Result<KycAdvancedStatusResponse> getAdvancedKycStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        KycAdvancedStatusResponse response = kycService.getAdvancedKycStatus(userId);
        return Result.success(response);
    }
}















