/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.dto.request.ChangePasswordRequest;
import com.cryptotrade.systemmanagement.dto.request.CreateSystemAdminRequest;
import com.cryptotrade.systemmanagement.dto.request.SystemAdminLoginRequest;
import com.cryptotrade.systemmanagement.dto.request.UpdateSystemAdminRequest;
import com.cryptotrade.systemmanagement.dto.response.SystemAdminLoginResponse;
import com.cryptotrade.systemmanagement.dto.response.SystemAdminResponse;
import com.cryptotrade.systemmanagement.entity.SystemAdmin;

import java.util.List;

/**
 * 系统管理员服务接口
 */
public interface SystemAdminService {
    
    /**
     * 创建管理员
     */
    SystemAdminResponse createAdmin(CreateSystemAdminRequest request);
    
    /**
     * 更新管理员
     */
    SystemAdminResponse updateAdmin(Long adminId, UpdateSystemAdminRequest request);
    
    /**
     * 删除管理员
     */
    void deleteAdmin(Long adminId);
    
    /**
     * 根据ID查询管理员
     */
    SystemAdminResponse getAdminById(Long adminId);
    
    /**
     * 根据用户名查询管理员
     */
    SystemAdminResponse getAdminByUsername(String username);
    
    /**
     * 根据手机号查询管理员
     */
    SystemAdminResponse getAdminByPhone(String phone);
    
    /**
     * 根据邮箱查询管理员
     */
    SystemAdminResponse getAdminByEmail(String email);
    
    /**
     * 查询所有管理员
     */
    List<SystemAdminResponse> getAllAdmins();
    
    /**
     * 根据角色ID查询管理员
     */
    List<SystemAdminResponse> getAdminsByRoleId(Long roleId);
    
    /**
     * 根据状态查询管理员
     */
    List<SystemAdminResponse> getAdminsByStatus(String status);
    
    /**
     * 管理员登录
     */
    SystemAdminLoginResponse login(SystemAdminLoginRequest request);
    
    /**
     * 修改密码
     */
    void changePassword(Long adminId, ChangePasswordRequest request);
    
    /**
     * 重置密码（管理员操作）
     */
    void resetPassword(Long adminId, String newPassword);
    
    /**
     * 检查权限
     */
    boolean hasPermission(Long adminId, String permissionCode);
    
    /**
     * 更新登录信息
     */
    void updateLoginInfo(Long adminId, String loginIp, String loginDevice);
}
