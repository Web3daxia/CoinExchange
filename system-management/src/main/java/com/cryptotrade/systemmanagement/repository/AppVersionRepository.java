/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * APP版本管理Repository
 */
@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    
    Optional<AppVersion> findByPlatformAndVersion(String platform, String version);
    
    List<AppVersion> findByPlatform(String platform);
    
    List<AppVersion> findByStatus(String status);
    
    List<AppVersion> findByPlatformAndStatus(String platform, String status);
    
    List<AppVersion> findAllByOrderBySortOrderAsc();
}














