/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.SystemAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemAdminRepository extends JpaRepository<SystemAdmin, Long> {
    
    Optional<SystemAdmin> findByUsername(String username);
    
    Optional<SystemAdmin> findByPhone(String phone);
    
    Optional<SystemAdmin> findByEmail(String email);
    
    List<SystemAdmin> findByRoleId(Long roleId);
    
    List<SystemAdmin> findByStatus(String status);
    
    boolean existsByUsername(String username);
    
    boolean existsByPhone(String phone);
    
    boolean existsByEmail(String email);
}



