/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.user.service.impl;

import com.cryptotrade.user.dto.request.*;
import com.cryptotrade.user.dto.response.*;
import com.cryptotrade.user.entity.*;
import com.cryptotrade.user.repository.*;
import com.cryptotrade.user.service.AccountService;
import com.cryptotrade.user.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private UserDeviceRepository userDeviceRepository;

    @Autowired
    private IpWhitelistRepository ipWhitelistRepository;

    @Autowired
    private OAuthBindingRepository oauthBindingRepository;

    @Autowired
    private AccountOperationLogRepository accountOperationLogRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest request) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        String oldValue = user.toString();

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            try {
                String avatarUrl = fileStorageService.storeFile(request.getAvatar(), "avatars/" + userId);
                user.setAvatarUrl(avatarUrl);
            } catch (IOException e) {
                throw new RuntimeException("头像上传失败: " + e.getMessage(), e);
            }
        }

        userRepository.save(user);

        // 记录操作日志
        logAccountOperation(userId, "UPDATE_PROFILE", "更新个人资料", oldValue, user.toString());
    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile avatar) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        try {
            String avatarUrl = fileStorageService.storeFile(avatar, "avatars/" + userId);
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

            return avatarUrl;
        } catch (IOException e) {
            throw new RuntimeException("头像上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void unbindEmail(Long userId) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (user.getEmail() == null) {
            throw new RuntimeException("未绑定邮箱");
        }

        String oldValue = user.getEmail();
        user.setEmail(null);
        userRepository.save(user);

        logAccountOperation(userId, "UNBIND_EMAIL", "解绑邮箱", oldValue, null);
    }

    @Override
    @Transactional
    public void unbindPhone(Long userId) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (user.getPhone() == null) {
            throw new RuntimeException("未绑定手机号");
        }

        String oldValue = user.getPhone();
        user.setPhone(null);
        user.setCountryCode(null);
        userRepository.save(user);

        logAccountOperation(userId, "UNBIND_PHONE", "解绑手机号", oldValue, null);
    }

    @Override
    @Transactional
    public void bindOAuth(Long userId, BindOAuthRequest request) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        // 验证用户存在
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }

        // TODO: 调用OAuth API验证token并获取用户信息
        // 这里需要根据不同的provider调用相应的OAuth API
        
        // 检查是否已绑定
        Optional<OAuthBinding> existing = oauthBindingRepository.findByUserIdAndProvider(userId, request.getProvider());
        if (existing.isPresent()) {
            throw new RuntimeException("该OAuth账户已绑定");
        }

        // 创建OAuth绑定记录
        OAuthBinding binding = new OAuthBinding();
        binding.setUserId(userId);
        binding.setProvider(request.getProvider());
        // TODO: 从OAuth API获取实际信息
        binding.setOauthId("oauth_id_from_api");
        binding.setOauthEmail("email_from_api");
        binding.setOauthUsername("username_from_api");

        oauthBindingRepository.save(binding);
        logAccountOperation(userId, "BIND_OAUTH", "绑定OAuth账户: " + request.getProvider(), null, request.getProvider());
    }

    @Override
    @Transactional
    public void unbindOAuth(Long userId, String provider) {
        OAuthBinding binding = oauthBindingRepository.findByUserIdAndProvider(userId, provider)
                .orElseThrow(() -> new RuntimeException("未绑定该OAuth账户"));

        binding.setIsActive(false);
        oauthBindingRepository.save(binding);

        logAccountOperation(userId, "UNBIND_OAUTH", "解绑OAuth账户: " + provider, provider, null);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 验证旧密码
        if (user.getPassword() == null || !passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 设置新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        logAccountOperation(userId, "CHANGE_PASSWORD", "修改密码", null, null);
    }

    @Override
    public Page<LoginHistoryResponse> getLoginHistory(Long userId, Pageable pageable) {
        Page<LoginHistory> histories = loginHistoryRepository.findByUserIdOrderByLoginAtDesc(userId, pageable);
        return histories.map(this::convertToLoginHistoryResponse);
    }

    @Override
    public List<UserDeviceResponse> getDevices(Long userId) {
        List<UserDevice> devices = userDeviceRepository.findByUserIdAndIsActiveTrue(userId);
        return devices.stream()
                .map(this::convertToUserDeviceResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void logoutDevice(Long userId, Long deviceId) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        Objects.requireNonNull(deviceId, "设备ID不能为空");
        UserDevice device = userDeviceRepository.findById(deviceId)
                .filter(d -> d.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("设备不存在"));

        device.setIsActive(false);
        userDeviceRepository.save(device);
    }

    @Override
    @Transactional
    public void logoutAllDevices(Long userId) {
        List<UserDevice> devices = userDeviceRepository.findByUserIdAndIsActiveTrue(userId);
        devices.forEach(device -> device.setIsActive(false));
        userDeviceRepository.saveAll(devices);
    }

    @Override
    @Transactional
    public void addIpWhitelist(Long userId, AddIpWhitelistRequest request) {
        IpWhitelist whitelist = new IpWhitelist();
        whitelist.setUserId(userId);
        
        // 解析IP地址或IP段
        String ipAddress = request.getIpAddress();
        if (ipAddress.contains("/")) {
            // CIDR格式
            String[] parts = ipAddress.split("/");
            whitelist.setIpAddress(parts[0]);
            whitelist.setIpRange(ipAddress);
        } else {
            whitelist.setIpAddress(ipAddress);
        }
        
        whitelist.setDescription(request.getDescription());
        whitelist.setIsEnabled(true);
        
        ipWhitelistRepository.save(whitelist);
    }

    @Override
    @Transactional
    public void removeIpWhitelist(Long userId, Long ipWhitelistId) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        Objects.requireNonNull(ipWhitelistId, "IP白名单ID不能为空");
        IpWhitelist whitelist = ipWhitelistRepository.findById(ipWhitelistId)
                .filter(w -> w.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("IP白名单记录不存在"));

        ipWhitelistRepository.delete(Objects.requireNonNull(whitelist, "IP白名单记录不能为空"));
    }

    @Override
    public List<IpWhitelistResponse> getIpWhitelist(Long userId) {
        List<IpWhitelist> whitelists = ipWhitelistRepository.findByUserIdAndIsEnabledTrue(userId);
        return whitelists.stream()
                .map(this::convertToIpWhitelistResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isIpWhitelisted(Long userId, String ipAddress) {
        List<IpWhitelist> whitelists = ipWhitelistRepository.findByUserIdAndIsEnabledTrue(userId);
        
        if (whitelists.isEmpty()) {
            // 如果没有设置白名单，则允许所有IP
            return true;
        }

        for (IpWhitelist whitelist : whitelists) {
            if (whitelist.getIpRange() != null) {
                // 检查CIDR格式的IP段
                if (isIpInRange(ipAddress, whitelist.getIpRange())) {
                    return true;
                }
            } else {
                // 精确匹配IP地址
                if (ipAddress.equals(whitelist.getIpAddress())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    @Transactional
    public void freezeAccount(Long userId, String reason) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setAccountStatus("FROZEN");
        userRepository.save(user);

        logAccountOperation(userId, "FREEZE_ACCOUNT", "冻结账户: " + reason, "ACTIVE", "FROZEN");
    }

    @Override
    @Transactional
    public void unfreezeAccount(Long userId) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setAccountStatus("ACTIVE");
        userRepository.save(user);

        logAccountOperation(userId, "UNFREEZE_ACCOUNT", "解冻账户", "FROZEN", "ACTIVE");
    }

    @Override
    public String getAccountStatus(Long userId) {
        Objects.requireNonNull(userId, "用户ID不能为空");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return user.getAccountStatus();
    }

    @Override
    public List<OAuthBindingResponse> getOAuthBindings(Long userId) {
        List<OAuthBinding> bindings = oauthBindingRepository.findByUserIdAndIsActiveTrue(userId);
        return bindings.stream()
                .map(this::convertToOAuthBindingResponse)
                .collect(Collectors.toList());
    }

    // 辅助方法

    private LoginHistoryResponse convertToLoginHistoryResponse(LoginHistory history) {
        LoginHistoryResponse response = new LoginHistoryResponse();
        response.setId(history.getId());
        response.setLoginIp(history.getLoginIp());
        response.setDeviceType(history.getDeviceType());
        response.setLoginLocation(history.getLoginLocation());
        response.setLoginStatus(history.getLoginStatus());
        response.setLoginAt(history.getLoginAt());
        return response;
    }

    private UserDeviceResponse convertToUserDeviceResponse(UserDevice device) {
        UserDeviceResponse response = new UserDeviceResponse();
        response.setId(device.getId());
        response.setDeviceName(device.getDeviceName());
        response.setDeviceType(device.getDeviceType());
        response.setLastLoginIp(device.getLastLoginIp());
        response.setLastLoginAt(device.getLastLoginAt());
        response.setIsActive(device.getIsActive());
        response.setCreatedAt(device.getCreatedAt());
        return response;
    }

    private IpWhitelistResponse convertToIpWhitelistResponse(IpWhitelist whitelist) {
        IpWhitelistResponse response = new IpWhitelistResponse();
        response.setId(whitelist.getId());
        response.setIpAddress(whitelist.getIpAddress());
        response.setIpRange(whitelist.getIpRange());
        response.setDescription(whitelist.getDescription());
        response.setIsEnabled(whitelist.getIsEnabled());
        response.setCreatedAt(whitelist.getCreatedAt());
        return response;
    }

    private OAuthBindingResponse convertToOAuthBindingResponse(OAuthBinding binding) {
        OAuthBindingResponse response = new OAuthBindingResponse();
        response.setId(binding.getId());
        response.setProvider(binding.getProvider());
        response.setOauthUsername(binding.getOauthUsername());
        response.setOauthEmail(binding.getOauthEmail());
        response.setOauthAvatar(binding.getOauthAvatar());
        response.setIsActive(binding.getIsActive());
        response.setCreatedAt(binding.getCreatedAt());
        return response;
    }

    private void logAccountOperation(Long userId, String operationType, String operationDesc,
                                     String oldValue, String newValue) {
        AccountOperationLog log = new AccountOperationLog();
        log.setUserId(userId);
        log.setOperationType(operationType);
        log.setOperationDesc(operationDesc);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        // TODO: 从请求上下文获取IP和UserAgent
        accountOperationLogRepository.save(log);
    }

    /**
     * 检查IP是否在CIDR范围内
     */
    private boolean isIpInRange(String ipAddress, String cidr) {
        try {
            String[] parts = cidr.split("/");
            String networkIp = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);

            long ip = ipToLong(ipAddress);
            long network = ipToLong(networkIp);
            long mask = (-1L << (32 - prefixLength));

            return (ip & mask) == (network & mask);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * IP地址转Long
     */
    private long ipToLong(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        long ip = 0;
        for (int i = 0; i < 4; i++) {
            ip = (ip << 8) + Long.parseLong(parts[i]);
        }
        return ip;
    }
}














