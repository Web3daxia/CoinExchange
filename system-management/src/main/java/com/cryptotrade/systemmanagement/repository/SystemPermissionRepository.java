/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.SystemPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemPermissionRepository extends JpaRepository<SystemPermission, Long> {
    Optional<SystemPermission> findByPermissionCode(String permissionCode);
    
    List<SystemPermission> findByModule(String module);
    
    @Query("SELECT p FROM SystemPermission p JOIN RolePermission rp ON p.id = rp.permissionId WHERE rp.roleId = :roleId")
    List<SystemPermission> findByRoleId(@Param("roleId") Long roleId);
}














