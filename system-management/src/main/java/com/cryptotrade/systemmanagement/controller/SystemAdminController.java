/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.dto.request.ChangePasswordRequest;
import com.cryptotrade.systemmanagement.dto.request.CreateSystemAdminRequest;
import com.cryptotrade.systemmanagement.dto.request.SystemAdminLoginRequest;
import com.cryptotrade.systemmanagement.dto.request.UpdateSystemAdminRequest;
import com.cryptotrade.systemmanagement.dto.response.SystemAdminLoginResponse;
import com.cryptotrade.systemmanagement.dto.response.SystemAdminResponse;
import com.cryptotrade.systemmanagement.service.SystemAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 系统管理员控制器
 */
@RestController
@RequestMapping("/admin/system/admin")
@Api(tags = "系统管理员管理")
public class SystemAdminController {
    
    @Autowired
    private SystemAdminService systemAdminService;
    
    @PostMapping("/login")
    @ApiOperation(value = "管理员登录", notes = "系统管理员登录接口")
    public Result<SystemAdminLoginResponse> login(
            @Valid @RequestBody SystemAdminLoginRequest request,
            HttpServletRequest httpRequest) {
        try {
            // 获取客户端IP和设备信息
            String clientIp = getClientIp(httpRequest);
            String userAgent = httpRequest.getHeader("User-Agent");
            
            if (request.getLoginIp() == null) {
                request.setLoginIp(clientIp);
            }
            if (request.getLoginDevice() == null) {
                request.setLoginDevice(userAgent);
            }
            
            SystemAdminLoginResponse response = systemAdminService.login(request);
            return Result.success("登录成功", response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/create")
    @ApiOperation(value = "创建管理员", notes = "创建系统管理员账户")
    public Result<SystemAdminResponse> createAdmin(
            @Valid @RequestBody CreateSystemAdminRequest request) {
        try {
            SystemAdminResponse admin = systemAdminService.createAdmin(request);
            return Result.success("管理员创建成功", admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{adminId}")
    @ApiOperation(value = "更新管理员", notes = "更新管理员信息")
    public Result<SystemAdminResponse> updateAdmin(
            @ApiParam(value = "管理员ID", required = true) @PathVariable Long adminId,
            @Valid @RequestBody UpdateSystemAdminRequest request) {
        try {
            SystemAdminResponse admin = systemAdminService.updateAdmin(adminId, request);
            return Result.success("管理员更新成功", admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{adminId}")
    @ApiOperation(value = "删除管理员", notes = "删除系统管理员")
    public Result<Void> deleteAdmin(
            @ApiParam(value = "管理员ID", required = true) @PathVariable Long adminId) {
        try {
            systemAdminService.deleteAdmin(adminId);
            return Result.success("管理员删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询管理员列表", notes = "查询所有系统管理员")
    public Result<List<SystemAdminResponse>> getAllAdmins() {
        try {
            List<SystemAdminResponse> admins = systemAdminService.getAllAdmins();
            return Result.success(admins);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{adminId}")
    @ApiOperation(value = "查询管理员详情", notes = "根据ID查询管理员详情")
    public Result<SystemAdminResponse> getAdminById(
            @ApiParam(value = "管理员ID", required = true) @PathVariable Long adminId) {
        try {
            SystemAdminResponse admin = systemAdminService.getAdminById(adminId);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/username/{username}")
    @ApiOperation(value = "根据用户名查询管理员", notes = "根据用户名查询管理员详情")
    public Result<SystemAdminResponse> getAdminByUsername(
            @ApiParam(value = "用户名", required = true) @PathVariable String username) {
        try {
            SystemAdminResponse admin = systemAdminService.getAdminByUsername(username);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/phone/{phone}")
    @ApiOperation(value = "根据手机号查询管理员", notes = "根据手机号查询管理员详情")
    public Result<SystemAdminResponse> getAdminByPhone(
            @ApiParam(value = "手机号", required = true) @PathVariable String phone) {
        try {
            SystemAdminResponse admin = systemAdminService.getAdminByPhone(phone);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/email/{email}")
    @ApiOperation(value = "根据邮箱查询管理员", notes = "根据邮箱查询管理员详情")
    public Result<SystemAdminResponse> getAdminByEmail(
            @ApiParam(value = "邮箱", required = true) @PathVariable String email) {
        try {
            SystemAdminResponse admin = systemAdminService.getAdminByEmail(email);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/role/{roleId}")
    @ApiOperation(value = "根据角色查询管理员", notes = "根据角色ID查询管理员列表")
    public Result<List<SystemAdminResponse>> getAdminsByRoleId(
            @ApiParam(value = "角色ID", required = true) @PathVariable Long roleId) {
        try {
            List<SystemAdminResponse> admins = systemAdminService.getAdminsByRoleId(roleId);
            return Result.success(admins);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/status/{status}")
    @ApiOperation(value = "根据状态查询管理员", notes = "根据状态查询管理员列表")
    public Result<List<SystemAdminResponse>> getAdminsByStatus(
            @ApiParam(value = "状态", required = true) @PathVariable String status) {
        try {
            List<SystemAdminResponse> admins = systemAdminService.getAdminsByStatus(status);
            return Result.success(admins);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/{adminId}/change-password")
    @ApiOperation(value = "修改密码", notes = "管理员修改自己的密码")
    public Result<Void> changePassword(
            @ApiParam(value = "管理员ID", required = true) @PathVariable Long adminId,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            systemAdminService.changePassword(adminId, request);
            return Result.success("密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/{adminId}/reset-password")
    @ApiOperation(value = "重置密码", notes = "管理员重置其他管理员的密码（需要权限）")
    public Result<Void> resetPassword(
            @ApiParam(value = "管理员ID", required = true) @PathVariable Long adminId,
            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {
        try {
            systemAdminService.resetPassword(adminId, newPassword);
            return Result.success("密码重置成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/check-permission")
    @ApiOperation(value = "检查权限", notes = "检查管理员是否有指定权限")
    public Result<Boolean> checkPermission(
            @ApiParam(value = "管理员ID", required = true) @RequestParam Long adminId,
            @ApiParam(value = "权限代码", required = true) @RequestParam String permissionCode) {
        try {
            boolean hasPermission = systemAdminService.hasPermission(adminId, permissionCode);
            return Result.success(hasPermission);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多个IP的情况
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
