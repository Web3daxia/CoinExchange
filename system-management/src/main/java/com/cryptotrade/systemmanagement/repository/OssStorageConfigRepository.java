/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.OssStorageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OssStorageConfigRepository extends JpaRepository<OssStorageConfig, Long> {
    Optional<OssStorageConfig> findByProvider(String provider);
    
    Optional<OssStorageConfig> findByEnabledTrueAndIsDefaultTrue();
    
    List<OssStorageConfig> findByEnabledTrue();
    
    boolean existsByProvider(String provider);
}














