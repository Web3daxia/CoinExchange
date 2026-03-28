/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.SmsServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmsServiceConfigRepository extends JpaRepository<SmsServiceConfig, Long> {
    Optional<SmsServiceConfig> findByServiceProvider(String serviceProvider);
    
    Optional<SmsServiceConfig> findByEnabledTrueAndIsDefaultTrue();
    
    List<SmsServiceConfig> findByEnabledTrue();
    
    boolean existsByServiceProvider(String serviceProvider);
}














