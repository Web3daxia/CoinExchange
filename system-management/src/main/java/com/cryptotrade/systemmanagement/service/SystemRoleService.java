/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.service;

import com.cryptotrade.systemmanagement.entity.SystemRole;
import java.util.List;

public interface SystemRoleService {
    SystemRole createRole(String roleCode, String roleName, String description, Integer level);
    
    SystemRole updateRole(Long roleId, String roleName, String description, Integer level, String status);
    
    void deleteRole(Long roleId);
    
    SystemRole getRoleById(Long roleId);
    
    SystemRole getRoleByCode(String roleCode);
    
    List<SystemRole> getAllRoles();
    
    void assignPermissionToRole(Long roleId, Long permissionId);
    
    void removePermissionFromRole(Long roleId, Long permissionId);
    
    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);
}














