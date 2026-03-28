/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service.impl;

import com.cryptotrade.systemmanagement.entity.RolePermission;
import com.cryptotrade.systemmanagement.entity.SystemRole;
import com.cryptotrade.systemmanagement.repository.RolePermissionRepository;
import com.cryptotrade.systemmanagement.repository.SystemRoleRepository;
import com.cryptotrade.systemmanagement.service.SystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemRoleServiceImpl implements SystemRoleService {
    
    @Autowired
    private SystemRoleRepository systemRoleRepository;
    
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    
    @Override
    @Transactional
    public SystemRole createRole(String roleCode, String roleName, String description, Integer level) {
        if (systemRoleRepository.existsByRoleCode(roleCode)) {
            throw new RuntimeException("角色代码已存在: " + roleCode);
        }
        
        SystemRole role = new SystemRole();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setDescription(description);
        role.setLevel(level);
        role.setStatus("ACTIVE");
        
        return systemRoleRepository.save(role);
    }
    
    @Override
    @Transactional
    public SystemRole updateRole(Long roleId, String roleName, String description, Integer level, String status) {
        SystemRole role = systemRoleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        role.setRoleName(roleName);
        role.setDescription(description);
        role.setLevel(level);
        role.setStatus(status);
        
        return systemRoleRepository.save(role);
    }
    
    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        // 检查是否有管理员使用该角色
        // TODO: 添加检查逻辑
        
        systemRoleRepository.deleteById(roleId);
    }
    
    @Override
    public SystemRole getRoleById(Long roleId) {
        return systemRoleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
    }
    
    @Override
    public SystemRole getRoleByCode(String roleCode) {
        return systemRoleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
    }
    
    @Override
    public List<SystemRole> getAllRoles() {
        return systemRoleRepository.findAll();
    }
    
    @Override
    @Transactional
    public void assignPermissionToRole(Long roleId, Long permissionId) {
        if (rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            return; // 已经存在，无需重复添加
        }
        
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(roleId);
        rolePermission.setPermissionId(permissionId);
        
        rolePermissionRepository.save(rolePermission);
    }
    
    @Override
    @Transactional
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        rolePermissionRepository.findByRoleId(roleId).stream()
                .filter(rp -> rp.getPermissionId().equals(permissionId))
                .findFirst()
                .ifPresent(rp -> rolePermissionRepository.deleteById(rp.getId()));
    }
    
    @Override
    @Transactional
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        // 先删除该角色的所有权限
        rolePermissionRepository.deleteByRoleId(roleId);
        
        // 再添加新权限
        for (Long permissionId : permissionIds) {
            assignPermissionToRole(roleId, permissionId);
        }
    }
}














