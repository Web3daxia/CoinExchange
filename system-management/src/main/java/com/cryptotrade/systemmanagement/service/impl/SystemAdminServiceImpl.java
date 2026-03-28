/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.dto.request.ChangePasswordRequest;
import com.cryptotrade.systemmanagement.dto.request.CreateSystemAdminRequest;
import com.cryptotrade.systemmanagement.dto.request.SystemAdminLoginRequest;
import com.cryptotrade.systemmanagement.dto.request.UpdateSystemAdminRequest;
import com.cryptotrade.systemmanagement.dto.response.SystemAdminLoginResponse;
import com.cryptotrade.systemmanagement.dto.response.SystemAdminResponse;
import com.cryptotrade.systemmanagement.entity.SystemAdmin;
import com.cryptotrade.systemmanagement.entity.SystemPermission;
import com.cryptotrade.systemmanagement.entity.SystemRole;
import com.cryptotrade.systemmanagement.repository.SystemAdminRepository;
import com.cryptotrade.systemmanagement.repository.SystemPermissionRepository;
import com.cryptotrade.systemmanagement.repository.SystemRoleRepository;
import com.cryptotrade.systemmanagement.service.SystemAdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统管理员服务实现
 */
@Service
public class SystemAdminServiceImpl implements SystemAdminService {
    
    private static final Logger logger = LoggerFactory.getLogger(SystemAdminServiceImpl.class);
    
    @Autowired
    private SystemAdminRepository systemAdminRepository;
    
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    
    @Autowired
    private SystemPermissionRepository systemPermissionRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public SystemAdminResponse createAdmin(CreateSystemAdminRequest request) {
        // 检查用户名是否已存在
        if (systemAdminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查手机号是否已存在
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            if (systemAdminRepository.existsByPhone(request.getPhone())) {
                throw new RuntimeException("手机号已被使用");
            }
        }
        
        // 检查邮箱是否已存在
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            if (systemAdminRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("邮箱已被使用");
            }
        }
        
        // 验证角色是否存在
        systemRoleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        // 创建管理员
        SystemAdmin admin = new SystemAdmin();
        admin.setRoleId(request.getRoleId());
        admin.setAvatar(request.getAvatar());
        admin.setUsername(request.getUsername());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setPhone(request.getPhone());
        admin.setEmail(request.getEmail());
        admin.setSecurityCode(request.getSecurityCode());
        admin.setEnableGoogleAuth(request.getEnableGoogleAuth() != null ? request.getEnableGoogleAuth() : false);
        admin.setStatus("ACTIVE");
        admin.setCreatedAt(LocalDateTime.now());
        
        admin = systemAdminRepository.save(admin);
        return convertToResponse(admin);
    }
    
    @Override
    @Transactional
    public SystemAdminResponse updateAdmin(Long adminId, UpdateSystemAdminRequest request) {
        SystemAdmin admin = systemAdminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        // 更新角色
        if (request.getRoleId() != null) {
            systemRoleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("角色不存在"));
            admin.setRoleId(request.getRoleId());
        }
        
        // 更新头像
        if (request.getAvatar() != null) {
            admin.setAvatar(request.getAvatar());
        }
        
        // 更新手机号
        if (request.getPhone() != null) {
            if (!request.getPhone().equals(admin.getPhone())) {
                if (systemAdminRepository.existsByPhone(request.getPhone())) {
                    throw new RuntimeException("手机号已被使用");
                }
            }
            admin.setPhone(request.getPhone());
        }
        
        // 更新邮箱
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(admin.getEmail())) {
                if (systemAdminRepository.existsByEmail(request.getEmail())) {
                    throw new RuntimeException("邮箱已被使用");
                }
            }
            admin.setEmail(request.getEmail());
        }
        
        // 更新安全码
        if (request.getSecurityCode() != null) {
            admin.setSecurityCode(request.getSecurityCode());
        }
        
        // 更新谷歌验证码设置
        if (request.getEnableGoogleAuth() != null) {
            admin.setEnableGoogleAuth(request.getEnableGoogleAuth());
        }
        
        // 更新状态
        if (request.getStatus() != null) {
            admin.setStatus(request.getStatus());
        }
        
        admin = systemAdminRepository.save(admin);
        return convertToResponse(admin);
    }
    
    @Override
    @Transactional
    public void deleteAdmin(Long adminId) {
        SystemAdmin admin = systemAdminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        // 不能删除超级管理员（通过角色判断）
        SystemRole role = systemRoleRepository.findById(admin.getRoleId())
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        if ("ROOT".equals(role.getRoleCode())) {
            throw new RuntimeException("不能删除超级管理员");
        }
        
        systemAdminRepository.deleteById(adminId);
    }
    
    @Override
    public SystemAdminResponse getAdminById(Long adminId) {
        SystemAdmin admin = systemAdminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        return convertToResponse(admin);
    }
    
    @Override
    public SystemAdminResponse getAdminByUsername(String username) {
        SystemAdmin admin = systemAdminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        return convertToResponse(admin);
    }
    
    @Override
    public SystemAdminResponse getAdminByPhone(String phone) {
        SystemAdmin admin = systemAdminRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        return convertToResponse(admin);
    }
    
    @Override
    public SystemAdminResponse getAdminByEmail(String email) {
        SystemAdmin admin = systemAdminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        return convertToResponse(admin);
    }
    
    @Override
    public List<SystemAdminResponse> getAllAdmins() {
        return systemAdminRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SystemAdminResponse> getAdminsByRoleId(Long roleId) {
        return systemAdminRepository.findByRoleId(roleId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SystemAdminResponse> getAdminsByStatus(String status) {
        return systemAdminRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public SystemAdminLoginResponse login(SystemAdminLoginRequest request) {
        SystemAdmin admin = systemAdminRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        
        // 检查状态
        if (!"ACTIVE".equals(admin.getStatus())) {
            throw new RuntimeException("管理员账户已被禁用");
        }
        
        // 验证密码
        logger.info("开始验证密码 - 用户名: {}, 输入密码长度: {}, 数据库密码哈希: {}", 
            request.getUsername(), 
            request.getPassword() != null ? request.getPassword().length() : 0,
            admin.getPassword() != null ? admin.getPassword().substring(0, Math.min(20, admin.getPassword().length())) + "..." : "null");
        
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), admin.getPassword());
        logger.info("密码验证结果: {}", passwordMatches);
        
        if (!passwordMatches) {
            logger.warn("密码验证失败 - 用户名: {}, 数据库密码哈希前缀: {}", 
                request.getUsername(),
                admin.getPassword() != null ? admin.getPassword().substring(0, Math.min(30, admin.getPassword().length())) : "null");
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 验证谷歌验证码（如果启用）
        if (admin.getEnableGoogleAuth()) {
            if (request.getGoogleAuthCode() == null || request.getGoogleAuthCode().isEmpty()) {
                throw new RuntimeException("请输入谷歌验证码");
            }
            // TODO: 实现谷歌验证码验证逻辑
            // if (!googleAuthService.verify(admin.getGoogleAuthSecret(), request.getGoogleAuthCode())) {
            //     throw new RuntimeException("谷歌验证码错误");
            // }
        }
        
        // 验证安全码（如果设置了）
        if (admin.getSecurityCode() != null && !admin.getSecurityCode().isEmpty()) {
            if (request.getSecurityCode() == null || !admin.getSecurityCode().equals(request.getSecurityCode())) {
                throw new RuntimeException("安全码错误");
            }
        }
        
        // 更新登录信息
        updateLoginInfo(admin.getId(), request.getLoginIp(), request.getLoginDevice());
        
        // 生成令牌（这里简化处理，实际应该使用JWT）
        String accessToken = generateAccessToken(admin);
        String refreshToken = generateRefreshToken(admin);
        
        SystemAdminLoginResponse response = new SystemAdminLoginResponse();
        response.setAdmin(convertToResponse(admin));
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(3600L); // 1小时
        
        return response;
    }
    
    @Override
    @Transactional
    public void changePassword(Long adminId, ChangePasswordRequest request) {
        SystemAdmin admin = systemAdminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        // 验证旧密码
        if (!passwordEncoder.matches(request.getOldPassword(), admin.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        
        // 更新密码
        admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
        systemAdminRepository.save(admin);
    }
    
    @Override
    @Transactional
    public void resetPassword(Long adminId, String newPassword) {
        SystemAdmin admin = systemAdminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        admin.setPassword(passwordEncoder.encode(newPassword));
        systemAdminRepository.save(admin);
    }
    
    @Override
    public boolean hasPermission(Long adminId, String permissionCode) {
        SystemAdmin admin = systemAdminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        // ROOT角色拥有所有权限
        SystemRole role = systemRoleRepository.findById(admin.getRoleId())
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        if ("ROOT".equals(role.getRoleCode())) {
            return true;
        }
        
        // 检查权限
        List<SystemPermission> permissions = systemPermissionRepository.findByRoleId(admin.getRoleId());
        return permissions.stream()
                .anyMatch(p -> permissionCode.equals(p.getPermissionCode()));
    }
    
    @Override
    @Transactional
    public void updateLoginInfo(Long adminId, String loginIp, String loginDevice) {
        SystemAdmin admin = systemAdminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("管理员不存在"));
        
        admin.setLastLoginIp(loginIp);
        admin.setLastLoginDevice(loginDevice);
        admin.setLastLoginTime(LocalDateTime.now());
        
        systemAdminRepository.save(admin);
    }
    
    /**
     * 转换为响应对象
     */
    private SystemAdminResponse convertToResponse(SystemAdmin admin) {
        SystemAdminResponse response = new SystemAdminResponse();
        BeanUtils.copyProperties(admin, response);
        
        // 获取角色名称
        SystemRole role = systemRoleRepository.findById(admin.getRoleId()).orElse(null);
        if (role != null) {
            response.setRoleName(role.getRoleName());
        }
        
        return response;
    }
    
    /**
     * 生成访问令牌（简化实现，实际应使用JWT）
     */
    private String generateAccessToken(SystemAdmin admin) {
        // TODO: 实现JWT令牌生成
        return "access_token_" + admin.getId() + "_" + System.currentTimeMillis();
    }
    
    /**
     * 生成刷新令牌（简化实现，实际应使用JWT）
     */
    private String generateRefreshToken(SystemAdmin admin) {
        // TODO: 实现JWT刷新令牌生成
        return "refresh_token_" + admin.getId() + "_" + System.currentTimeMillis();
    }
}
