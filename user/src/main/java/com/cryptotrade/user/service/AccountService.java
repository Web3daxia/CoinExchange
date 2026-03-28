/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service;

import com.cryptotrade.user.dto.request.*;
import com.cryptotrade.user.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountService {
    /**
     * 更新个人资料
     */
    void updateProfile(Long userId, UpdateProfileRequest request);

    /**
     * 上传头像
     */
    String uploadAvatar(Long userId, MultipartFile avatar);

    /**
     * 解绑邮箱
     */
    void unbindEmail(Long userId);

    /**
     * 解绑手机号
     */
    void unbindPhone(Long userId);

    /**
     * 绑定OAuth账户
     */
    void bindOAuth(Long userId, BindOAuthRequest request);

    /**
     * 解绑OAuth账户
     */
    void unbindOAuth(Long userId, String provider);

    /**
     * 修改密码
     */
    void updatePassword(Long userId, UpdatePasswordRequest request);

    /**
     * 获取登录历史
     */
    Page<LoginHistoryResponse> getLoginHistory(Long userId, Pageable pageable);

    /**
     * 获取设备列表
     */
    List<UserDeviceResponse> getDevices(Long userId);

    /**
     * 退出指定设备
     */
    void logoutDevice(Long userId, Long deviceId);

    /**
     * 退出所有设备
     */
    void logoutAllDevices(Long userId);

    /**
     * 添加IP白名单
     */
    void addIpWhitelist(Long userId, AddIpWhitelistRequest request);

    /**
     * 移除IP白名单
     */
    void removeIpWhitelist(Long userId, Long ipWhitelistId);

    /**
     * 获取IP白名单列表
     */
    List<IpWhitelistResponse> getIpWhitelist(Long userId);

    /**
     * 检查IP是否在白名单中
     */
    boolean isIpWhitelisted(Long userId, String ipAddress);

    /**
     * 冻结账户
     */
    void freezeAccount(Long userId, String reason);

    /**
     * 解冻账户
     */
    void unfreezeAccount(Long userId);

    /**
     * 获取账户状态
     */
    String getAccountStatus(Long userId);

    /**
     * 获取OAuth绑定列表
     */
    List<OAuthBindingResponse> getOAuthBindings(Long userId);
}















