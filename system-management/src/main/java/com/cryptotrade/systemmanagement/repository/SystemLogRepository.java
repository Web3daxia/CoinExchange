/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统日志Repository
 */
@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long>, JpaSpecificationExecutor<SystemLog> {
    
    List<SystemLog> findByUserId(Long userId);
    
    List<SystemLog> findByUsername(String username);
    
    List<SystemLog> findByModule(String module);
    
    List<SystemLog> findByStatus(String status);
    
    List<SystemLog> findByIpAddress(String ipAddress);
    
    Page<SystemLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    Page<SystemLog> findByModuleAndCreatedAtBetween(String module, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}














