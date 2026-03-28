/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.user.dto.request.*;
import com.cryptotrade.user.dto.response.LoginResponse;
import com.cryptotrade.user.dto.response.RegisterResponse;
import com.cryptotrade.user.entity.UserRegistrationKey;
import com.cryptotrade.user.service.RegistrationKeyService;
import com.cryptotrade.user.service.UserService;
import com.cryptotrade.user.service.VerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;
    
    @Autowired
    private RegistrationKeyService registrationKeyService;

    @PostMapping("/register")
    @ApiOperation(value = "用户注册（通用接口）", notes = "支持手机号、邮箱注册，系统自动生成256位注册私钥")
    public Result<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);
        return Result.success("注册成功，请妥善保管注册私钥", response);
    }

    @PostMapping("/register/phone")
    @ApiOperation(value = "手机号注册", notes = "用户通过手机号注册，系统自动生成注册私钥")
    public Result<RegisterResponse> registerByPhone(@Valid @RequestBody PhoneRegisterRequest request) {
        RegisterResponse response = userService.registerByPhone(request);
        return Result.success("注册成功，请妥善保管注册私钥", response);
    }

    @PostMapping("/register/email")
    @ApiOperation(value = "邮箱注册", notes = "用户通过邮箱注册，系统自动生成注册私钥")
    public Result<RegisterResponse> registerByEmail(@Valid @RequestBody EmailRegisterRequest request) {
        RegisterResponse response = userService.registerByEmail(request);
        return Result.success("注册成功，请妥善保管注册私钥", response);
    }

    @PostMapping("/send-verification/phone")
    @ApiOperation(value = "发送手机验证码", notes = "发送短信验证码到指定手机号")
    public Result<Void> sendPhoneVerification(@RequestBody SendVerificationRequest request) {
        verificationService.sendVerificationCode("PHONE", request.getPhone(), request.getCountryCode());
        return Result.success("验证码已发送", null);
    }

    @PostMapping("/send-verification/email")
    @ApiOperation(value = "发送邮箱验证码", notes = "发送验证码到指定邮箱")
    public Result<Void> sendEmailVerification(@RequestBody SendVerificationRequest request) {
        verificationService.sendVerificationCode("EMAIL", request.getEmail(), null);
        return Result.success("验证码已发送", null);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录（通用接口）", notes = "支持密码、私钥、第三方OAuth登录")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success("登录成功", response);
    }

    @PostMapping("/login/registration-key")
    @ApiOperation(value = "注册私钥登录", notes = "使用注册时系统生成的注册私钥登录")
    public Result<LoginResponse> loginByRegistrationKey(@Valid @RequestBody RegistrationKeyLoginRequest request) {
        LoginResponse response = userService.loginByPrivateKey(request.getRegistrationKey());
        return Result.success("登录成功", response);
    }
    
    @GetMapping("/registration-key")
    @ApiOperation(value = "查询注册私钥", notes = "查询当前用户的注册私钥（需登录）")
    public Result<UserRegistrationKey> getRegistrationKey(
            @RequestHeader("X-User-Id") Long userId) {
        UserRegistrationKey registrationKey = registrationKeyService.getRegistrationKey(userId);
        return Result.success("查询成功", registrationKey);
    }

    @PostMapping("/login/google")
    @ApiOperation(value = "Google OAuth登录", notes = "使用Google账号快捷登录")
    public Result<LoginResponse> loginByGoogle(@Valid @RequestBody OAuthLoginRequest request) {
        request.setProvider("GOOGLE");
        LoginResponse response = userService.loginByOAuth(request);
        return Result.success("登录成功", response);
    }

    @PostMapping("/login/facebook")
    @ApiOperation(value = "Facebook OAuth登录", notes = "使用Facebook账号快捷登录")
    public Result<LoginResponse> loginByFacebook(@Valid @RequestBody OAuthLoginRequest request) {
        request.setProvider("FACEBOOK");
        LoginResponse response = userService.loginByOAuth(request);
        return Result.success("登录成功", response);
    }

    @PostMapping("/login/twitter")
    @ApiOperation(value = "Twitter OAuth登录", notes = "使用Twitter账号快捷登录")
    public Result<LoginResponse> loginByTwitter(@Valid @RequestBody OAuthLoginRequest request) {
        request.setProvider("TWITTER");
        LoginResponse response = userService.loginByOAuth(request);
        return Result.success("登录成功", response);
    }

    @PostMapping("/login/telegram")
    @ApiOperation(value = "Telegram OAuth登录", notes = "使用Telegram账号快捷登录")
    public Result<LoginResponse> loginByTelegram(@Valid @RequestBody OAuthLoginRequest request) {
        request.setProvider("TELEGRAM");
        LoginResponse response = userService.loginByOAuth(request);
        return Result.success("登录成功", response);
    }

    @PostMapping("/password-reset")
    @ApiOperation(value = "重置密码", notes = "通过邮箱/手机号发送验证码重置密码")
    public Result<Void> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        userService.resetPassword(request);
        return Result.success("密码重置成功", null);
    }
}
