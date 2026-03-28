/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service;

import com.cryptotrade.user.dto.request.*;
import com.cryptotrade.user.dto.response.LoginResponse;
import com.cryptotrade.user.dto.response.RegisterResponse;

public interface UserService {
    /**
     * 用户注册（通用接口，支持手机号和邮箱注册）
     * @return 注册响应，包含注册私钥
     */
    RegisterResponse register(RegisterRequest request);

    /**
     * 手机号注册
     * @return 注册响应，包含注册私钥
     */
    RegisterResponse registerByPhone(PhoneRegisterRequest request);

    /**
     * 邮箱注册
     * @return 注册响应，包含注册私钥
     */
    RegisterResponse registerByEmail(EmailRegisterRequest request);

    /**
     * 私钥注册
     */
    void registerByPrivateKey(PrivateKeyRegisterRequest request);

    /**
     * 用户登录（通用接口）
     */
    LoginResponse login(LoginRequest request);

    /**
     * 密码登录
     */
    LoginResponse loginByPassword(LoginRequest request);

    /**
     * 私钥登录
     */
    LoginResponse loginByPrivateKey(String privateKey);

    /**
     * OAuth登录
     */
    LoginResponse loginByOAuth(OAuthLoginRequest request);

    /**
     * 绑定邮箱
     */
    void bindEmail(Long userId, BindEmailRequest request);

    /**
     * 绑定手机号
     */
    void bindPhone(Long userId, BindPhoneRequest request);

    /**
     * 重置密码
     */
    void resetPassword(PasswordResetRequest request);

    /**
     * 启用2FA
     */
    String enable2FA(Long userId);

    /**
     * 验证2FA
     */
    boolean verify2FA(Long userId, String code);

    /**
     * 禁用2FA
     */
    void disable2FA(Long userId, String password);
}
