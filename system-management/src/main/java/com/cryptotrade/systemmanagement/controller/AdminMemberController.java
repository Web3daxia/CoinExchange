/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.*;
import com.cryptotrade.systemmanagement.dto.response.MemberAssetResponse;
import com.cryptotrade.systemmanagement.entity.MemberManagement;
import com.cryptotrade.systemmanagement.service.MemberAssetService;
import com.cryptotrade.systemmanagement.service.MemberManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台会员管理Controller
 */
@RestController
@RequestMapping("/admin/member")
@Api(tags = "会员管理")
public class AdminMemberController {

    @Autowired
    private MemberManagementService memberManagementService;

    @Autowired
    private MemberAssetService memberAssetService;

    // ========== 会员信息管理 ==========

    @GetMapping("/list")
    @ApiOperation(value = "获取会员列表", notes = "分页获取所有会员列表")
    public Result<Page<MemberManagement>> getMemberList(
            @ApiParam(value = "页码，从0开始", defaultValue = "0") @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MemberManagement> members = memberManagementService.getAllMembers(pageable);
            return Result.success(members);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索会员", notes = "根据关键字、交易状态、用户状态搜索会员")
    public Result<Page<MemberManagement>> searchMembers(
            @ApiParam(value = "关键字（会员名称、UID、邮箱、手机）") @RequestParam(required = false) String keyword,
            @ApiParam(value = "交易状态: NORMAL, FROZEN") @RequestParam(required = false) String tradingStatus,
            @ApiParam(value = "用户状态: NORMAL, DISABLED") @RequestParam(required = false) String userStatus,
            @ApiParam(value = "页码", defaultValue = "0") @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "每页大小", defaultValue = "20") @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<MemberManagement> members = memberManagementService.searchMembers(keyword, tradingStatus, userStatus, pageable);
            return Result.success(members);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取会员详情", notes = "根据ID获取会员详细信息")
    public Result<MemberManagement> getMemberById(
            @ApiParam(value = "会员ID", required = true) @PathVariable Long id) {
        try {
            MemberManagement member = memberManagementService.getMemberById(id)
                    .orElseThrow(() -> new RuntimeException("会员不存在"));
            return Result.success(member);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "根据用户ID获取会员信息", notes = "根据用户ID获取会员信息")
    public Result<MemberManagement> getMemberByUserId(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId) {
        try {
            MemberManagement member = memberManagementService.getMemberByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("会员不存在"));
            return Result.success(member);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/info")
    @ApiOperation(value = "更新会员基本信息", notes = "更新会员的邮箱、手机、国家")
    public Result<MemberManagement> updateMemberInfo(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "会员信息", required = true) @RequestBody UpdateMemberInfoRequest request) {
        try {
            MemberManagement member = memberManagementService.updateMemberInfo(userId, request);
            return Result.success("更新成功", member);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/password/login")
    @ApiOperation(value = "修改登录密码", notes = "管理员修改会员的登录密码")
    public Result<Void> updateLoginPassword(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {
        try {
            memberManagementService.updateLoginPassword(userId, newPassword);
            return Result.success("密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/password/asset")
    @ApiOperation(value = "修改资产管理密码", notes = "管理员修改会员的资产管理密码")
    public Result<Void> updateAssetPassword(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {
        try {
            memberManagementService.updateAssetPassword(userId, newPassword);
            return Result.success("资产管理密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/google-auth")
    @ApiOperation(value = "删除谷歌验证码", notes = "删除会员的谷歌验证码绑定")
    public Result<Void> deleteGoogleAuth(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId) {
        try {
            memberManagementService.deleteGoogleAuth(userId);
            return Result.success("谷歌验证码已删除", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/device/disable")
    @ApiOperation(value = "禁用设备", notes = "禁用会员的指定设备")
    public Result<Void> disableDevice(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "设备指纹", required = true) @RequestParam String deviceFingerprint) {
        try {
            memberManagementService.disableDevice(userId, deviceFingerprint);
            return Result.success("设备已禁用", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/ip/disable")
    @ApiOperation(value = "禁用IP", notes = "禁用会员的指定IP")
    public Result<Void> disableIp(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "IP地址", required = true) @RequestParam String ip) {
        try {
            memberManagementService.disableIp(userId, ip);
            return Result.success("IP已禁用", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/trading-status")
    @ApiOperation(value = "更新交易状态", notes = "冻结或解冻会员的交易功能")
    public Result<MemberManagement> updateTradingStatus(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "交易状态: NORMAL, FROZEN", required = true) @RequestParam String tradingStatus) {
        try {
            MemberManagement member = memberManagementService.updateTradingStatus(userId, tradingStatus);
            return Result.success("交易状态更新成功", member);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/user-status")
    @ApiOperation(value = "更新用户状态", notes = "启用或禁用会员账户")
    public Result<MemberManagement> updateUserStatus(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "用户状态: NORMAL, DISABLED", required = true) @RequestParam String userStatus) {
        try {
            MemberManagement member = memberManagementService.updateUserStatus(userId, userStatus);
            return Result.success("用户状态更新成功", member);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/withdraw-status")
    @ApiOperation(value = "更新提现状态", notes = "允许或拒绝会员提现")
    public Result<MemberManagement> updateWithdrawStatus(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "提现状态: ALLOWED, REJECTED", required = true) @RequestParam String withdrawStatus) {
        try {
            MemberManagement member = memberManagementService.updateWithdrawStatus(userId, withdrawStatus);
            return Result.success("提现状态更新成功", member);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ========== 会员资产管理 ==========

    @GetMapping("/{userId}/assets")
    @ApiOperation(value = "获取会员资产列表", notes = "获取指定会员的所有币种资产")
    public Result<List<MemberAssetResponse>> getMemberAssets(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId) {
        try {
            List<MemberAssetResponse> assets = memberAssetService.getMemberAssets(userId);
            return Result.success(assets);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{userId}/assets/{currency}")
    @ApiOperation(value = "获取会员指定币种资产", notes = "获取指定会员的指定币种资产")
    public Result<MemberAssetResponse> getMemberAssetByCurrency(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "币种", required = true) @PathVariable String currency) {
        try {
            MemberAssetResponse asset = memberAssetService.getMemberAssetByCurrency(userId, currency);
            return Result.success(asset);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{userId}/assets/deposit")
    @ApiOperation(value = "充币", notes = "为会员的指定币种充币")
    public Result<MemberAssetResponse> deposit(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "充币信息", required = true) @RequestBody MemberAssetOperationRequest request) {
        try {
            MemberAssetResponse asset = convertToResponse(memberAssetService.deposit(userId, request));
            return Result.success("充币成功", asset);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{userId}/assets/freeze")
    @ApiOperation(value = "冻结资产", notes = "冻结会员的指定币种资产")
    public Result<MemberAssetResponse> freeze(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "冻结信息", required = true) @RequestBody MemberAssetOperationRequest request) {
        try {
            MemberAssetResponse asset = convertToResponse(memberAssetService.freeze(userId, request));
            return Result.success("资产冻结成功", asset);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{userId}/assets/unfreeze")
    @ApiOperation(value = "解冻资产", notes = "解冻会员的指定币种资产")
    public Result<MemberAssetResponse> unfreeze(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "解冻信息", required = true) @RequestBody MemberAssetOperationRequest request) {
        try {
            MemberAssetResponse asset = convertToResponse(memberAssetService.unfreeze(userId, request));
            return Result.success("资产解冻成功", asset);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/assets/lock")
    @ApiOperation(value = "锁定/解锁资产", notes = "锁定或解锁会员的指定币种资产")
    public Result<MemberAssetResponse> lockAsset(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "锁定信息", required = true) @RequestBody LockAssetRequest request) {
        try {
            MemberAssetResponse asset = convertToResponse(memberAssetService.lockAsset(userId, request));
            return Result.success("操作成功", asset);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{userId}/assets/deposit-address")
    @ApiOperation(value = "重置充值地址", notes = "为会员的指定币种重置充值地址")
    public Result<MemberAssetResponse> resetDepositAddress(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId,
            @ApiParam(value = "充值地址信息", required = true) @RequestBody ResetDepositAddressRequest request) {
        try {
            MemberAssetResponse asset = convertToResponse(memberAssetService.resetDepositAddress(userId, request));
            return Result.success("充值地址重置成功", asset);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 转换为响应DTO
     */
    private MemberAssetResponse convertToResponse(com.cryptotrade.systemmanagement.entity.MemberAsset asset) {
        MemberAssetResponse response = new MemberAssetResponse();
        response.setId(asset.getId());
        response.setUserId(asset.getUserId());
        response.setCurrency(asset.getCurrency());
        response.setAvailableBalance(asset.getAvailableBalance());
        response.setFrozenBalance(asset.getFrozenBalance());
        response.setPendingReleaseBalance(asset.getPendingReleaseBalance());
        response.setIsLocked(asset.getIsLocked());
        response.setDepositAddress(asset.getDepositAddress());
        response.setDepositAddressTag(asset.getDepositAddressTag());
        return response;
    }
}














