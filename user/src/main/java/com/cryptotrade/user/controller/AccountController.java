/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.user.dto.request.*;
import com.cryptotrade.user.dto.response.*;
import com.cryptotrade.user.entity.User;
import com.cryptotrade.user.repository.UserRepository;
import com.cryptotrade.user.service.AccountService;
import com.cryptotrade.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/account")
@Api(tags = "账户管理模块")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/profile")
    @ApiOperation(value = "获取用户基本信息", notes = "获取当前登录用户的个人资料")
    public Result<Map<String, Object>> getProfile(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return Result.error("用户不存在");
        }

        User user = userOpt.get();
        Map<String, Object> profile = new HashMap<>();
        profile.put("userId", user.getId());
        profile.put("username", user.getUsername());
        profile.put("nickname", user.getNickname());
        profile.put("email", user.getEmail());
        profile.put("phone", user.getPhone());
        profile.put("avatarUrl", user.getAvatarUrl());
        profile.put("gender", user.getGender());
        profile.put("birthday", user.getBirthday());
        profile.put("kycLevel", user.getKycLevel());
        profile.put("kycStatus", user.getKycStatus());
        profile.put("twoFaEnabled", user.getTwoFaEnabled());
        profile.put("accountStatus", user.getAccountStatus());

        // 获取OAuth绑定列表
        List<OAuthBindingResponse> oauthBindings = accountService.getOAuthBindings(userId);
        profile.put("oauthBindings", oauthBindings);

        return Result.success(profile);
    }

    @PostMapping("/profile/update")
    @ApiOperation(value = "更新用户个人资料", notes = "更新昵称、头像、生日等信息")
    public Result<Void> updateProfile(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @ModelAttribute UpdateProfileRequest request) {
        accountService.updateProfile(userId, request);
        return Result.success("更新成功", null);
    }

    @PostMapping("/avatar/upload")
    @ApiOperation(value = "上传头像", notes = "上传用户头像图片")
    public Result<Map<String, String>> uploadAvatar(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam("avatar") MultipartFile avatar) {
        String avatarUrl = accountService.uploadAvatar(userId, avatar);
        Map<String, String> result = new HashMap<>();
        result.put("avatarUrl", avatarUrl);
        return Result.success("头像上传成功", result);
    }

    @PostMapping("/bind/email")
    @ApiOperation(value = "绑定邮箱", notes = "绑定或更新用户邮箱，需要验证码验证")
    public Result<Void> bindEmail(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody BindEmailRequest request) {
        userService.bindEmail(userId, request);
        return Result.success("邮箱绑定成功", null);
    }

    @PostMapping("/bind/phone")
    @ApiOperation(value = "绑定手机号", notes = "绑定或更新用户手机号，需要验证码验证")
    public Result<Void> bindPhone(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody BindPhoneRequest request) {
        userService.bindPhone(userId, request);
        return Result.success("手机号绑定成功", null);
    }

    @PostMapping("/unbind/email")
    @ApiOperation(value = "解绑邮箱", notes = "解绑用户邮箱")
    public Result<Void> unbindEmail(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        accountService.unbindEmail(userId);
        return Result.success("邮箱解绑成功", null);
    }

    @PostMapping("/unbind/phone")
    @ApiOperation(value = "解绑手机号", notes = "解绑用户手机号")
    public Result<Void> unbindPhone(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        accountService.unbindPhone(userId);
        return Result.success("手机号解绑成功", null);
    }

    @PostMapping("/bind/oauth")
    @ApiOperation(value = "绑定OAuth账户", notes = "绑定第三方OAuth账户（Google、Facebook等）")
    public Result<Void> bindOAuth(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody BindOAuthRequest request) {
        accountService.bindOAuth(userId, request);
        return Result.success("OAuth账户绑定成功", null);
    }

    @PostMapping("/unbind/oauth")
    @ApiOperation(value = "解绑OAuth账户", notes = "解绑第三方OAuth账户")
    public Result<Void> unbindOAuth(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, String> request) {
        String provider = request.get("provider");
        accountService.unbindOAuth(userId, provider);
        return Result.success("OAuth账户解绑成功", null);
    }

    @PostMapping("/password/update")
    @ApiOperation(value = "修改密码", notes = "修改用户登录密码，需要验证旧密码")
    public Result<Void> updatePassword(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody UpdatePasswordRequest request) {
        accountService.updatePassword(userId, request);
        return Result.success("密码修改成功", null);
    }

    @GetMapping("/login-history")
    @ApiOperation(value = "获取登录历史", notes = "分页获取用户的登录历史记录")
    public Result<Page<LoginHistoryResponse>> getLoginHistory(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LoginHistoryResponse> histories = accountService.getLoginHistory(userId, pageable);
        return Result.success(histories);
    }

    @GetMapping("/devices")
    @ApiOperation(value = "获取设备列表", notes = "获取用户已登录的设备列表")
    public Result<List<UserDeviceResponse>> getDevices(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        List<UserDeviceResponse> devices = accountService.getDevices(userId);
        return Result.success(devices);
    }

    @PostMapping("/security/device/exit")
    @ApiOperation(value = "退出指定设备", notes = "退出指定设备的登录状态")
    public Result<Void> logoutDevice(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, Long> request) {
        Long deviceId = request.get("deviceId");
        accountService.logoutDevice(userId, deviceId);
        return Result.success("设备已退出", null);
    }

    @PostMapping("/security/device/logout-all")
    @ApiOperation(value = "退出所有设备", notes = "退出所有设备的登录状态")
    public Result<Void> logoutAllDevices(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        accountService.logoutAllDevices(userId);
        return Result.success("所有设备已退出", null);
    }

    @PostMapping("/security/ip-whitelist/add")
    @ApiOperation(value = "添加IP白名单", notes = "添加IP地址或IP段到白名单")
    public Result<Void> addIpWhitelist(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody AddIpWhitelistRequest request) {
        accountService.addIpWhitelist(userId, request);
        return Result.success("IP白名单添加成功", null);
    }

    @PostMapping("/security/ip-whitelist/remove")
    @ApiOperation(value = "移除IP白名单", notes = "从白名单中移除指定IP")
    public Result<Void> removeIpWhitelist(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, Long> request) {
        Long ipWhitelistId = request.get("ipWhitelistId");
        accountService.removeIpWhitelist(userId, ipWhitelistId);
        return Result.success("IP白名单移除成功", null);
    }

    @GetMapping("/security/ip-whitelist")
    @ApiOperation(value = "获取IP白名单列表", notes = "获取用户的IP白名单列表")
    public Result<List<IpWhitelistResponse>> getIpWhitelist(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        List<IpWhitelistResponse> whitelist = accountService.getIpWhitelist(userId);
        return Result.success(whitelist);
    }

    @PostMapping("/2fa/enable")
    @ApiOperation(value = "启用2FA", notes = "启用二次验证，返回二维码和密钥")
    public Result<Map<String, String>> enable2FA(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        String secret = userService.enable2FA(userId);
        Map<String, String> result = new HashMap<>();
        result.put("secret", secret);
        result.put("qrCode", "data:image/png;base64,..."); // TODO: 生成二维码
        return Result.success("2FA已启用，请使用Google Authenticator扫描二维码", result);
    }

    @PostMapping("/2fa/verify")
    @ApiOperation(value = "验证2FA", notes = "验证二次验证码")
    public Result<Void> verify2FA(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, String> request) {
        String code = request.get("code");
        if (userService.verify2FA(userId, code)) {
            return Result.success("验证成功", null);
        } else {
            return Result.error("验证码错误");
        }
    }

    @PostMapping("/2fa/disable")
    @ApiOperation(value = "禁用2FA", notes = "禁用二次验证，需要密码验证")
    public Result<Void> disable2FA(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        userService.disable2FA(userId, password);
        return Result.success("2FA已禁用", null);
    }

    @PostMapping("/freeze")
    @ApiOperation(value = "冻结账户", notes = "冻结用户账户")
    public Result<Void> freezeAccount(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId,
            @RequestBody Map<String, String> request) {
        String reason = request.get("reason");
        accountService.freezeAccount(userId, reason);
        return Result.success("账户已冻结", null);
    }

    @PostMapping("/unfreeze")
    @ApiOperation(value = "解冻账户", notes = "解冻用户账户")
    public Result<Void> unfreezeAccount(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        accountService.unfreezeAccount(userId);
        return Result.success("账户已解冻", null);
    }

    @GetMapping("/status")
    @ApiOperation(value = "查询账户状态", notes = "查询用户账户状态（ACTIVE、FROZEN、DISABLED）")
    public Result<Map<String, String>> getAccountStatus(
            @ApiParam(value = "用户ID", required = true) @RequestHeader("X-User-Id") Long userId) {
        String status = accountService.getAccountStatus(userId);
        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        return Result.success(result);
    }
}
