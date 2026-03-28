/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.controller;

import com.cryptotrade.common.Result;
import com.cryptotrade.systemmanagement.entity.SystemRole;
import com.cryptotrade.systemmanagement.service.SystemRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/system/role")
@Api(tags = "系统角色管理")
public class SystemRoleController {
    
    @Autowired
    private SystemRoleService systemRoleService;
    
    @PostMapping("/create")
    @ApiOperation(value = "创建角色", notes = "创建系统角色")
    public Result<SystemRole> createRole(
            @ApiParam(value = "角色代码", required = true) @RequestParam String roleCode,
            @ApiParam(value = "角色名称", required = true) @RequestParam String roleName,
            @ApiParam(value = "角色描述") @RequestParam(required = false) String description,
            @ApiParam(value = "角色级别", required = true) @RequestParam Integer level) {
        try {
            SystemRole role = systemRoleService.createRole(roleCode, roleName, description, level);
            return Result.success("角色创建成功", role);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/update/{roleId}")
    @ApiOperation(value = "更新角色", notes = "更新角色信息")
    public Result<SystemRole> updateRole(
            @ApiParam(value = "角色ID", required = true) @PathVariable Long roleId,
            @ApiParam(value = "角色名称") @RequestParam(required = false) String roleName,
            @ApiParam(value = "角色描述") @RequestParam(required = false) String description,
            @ApiParam(value = "角色级别") @RequestParam(required = false) Integer level,
            @ApiParam(value = "状态") @RequestParam(required = false) String status) {
        try {
            SystemRole role = systemRoleService.updateRole(roleId, roleName, description, level, status);
            return Result.success("角色更新成功", role);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{roleId}")
    @ApiOperation(value = "删除角色", notes = "删除系统角色")
    public Result<Void> deleteRole(
            @ApiParam(value = "角色ID", required = true) @PathVariable Long roleId) {
        try {
            systemRoleService.deleteRole(roleId);
            return Result.success("角色删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/list")
    @ApiOperation(value = "查询角色列表", notes = "查询所有系统角色")
    public Result<List<SystemRole>> getAllRoles() {
        try {
            List<SystemRole> roles = systemRoleService.getAllRoles();
            return Result.success(roles);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/{roleId}")
    @ApiOperation(value = "查询角色详情", notes = "根据ID查询角色详情")
    public Result<SystemRole> getRoleById(
            @ApiParam(value = "角色ID", required = true) @PathVariable Long roleId) {
        try {
            SystemRole role = systemRoleService.getRoleById(roleId);
            return Result.success(role);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/{roleId}/permission/{permissionId}")
    @ApiOperation(value = "分配权限", notes = "为角色分配权限")
    public Result<Void> assignPermission(
            @ApiParam(value = "角色ID", required = true) @PathVariable Long roleId,
            @ApiParam(value = "权限ID", required = true) @PathVariable Long permissionId) {
        try {
            systemRoleService.assignPermissionToRole(roleId, permissionId);
            return Result.success("权限分配成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @DeleteMapping("/{roleId}/permission/{permissionId}")
    @ApiOperation(value = "移除权限", notes = "移除角色的权限")
    public Result<Void> removePermission(
            @ApiParam(value = "角色ID", required = true) @PathVariable Long roleId,
            @ApiParam(value = "权限ID", required = true) @PathVariable Long permissionId) {
        try {
            systemRoleService.removePermissionFromRole(roleId, permissionId);
            return Result.success("权限移除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}














