/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.systemmanagement.repository;

import com.cryptotrade.systemmanagement.entity.TranslationServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 翻译服务配置Repository
 */
@Repository
public interface TranslationServiceConfigRepository extends JpaRepository<TranslationServiceConfig, Long> {
    
    Optional<TranslationServiceConfig> findByServiceCode(String serviceCode);
    
    List<TranslationServiceConfig> findByServiceType(String serviceType);
    
    List<TranslationServiceConfig> findByIsEnabledTrue();
    
    List<TranslationServiceConfig> findByStatus(String status);
    
    Optional<TranslationServiceConfig> findFirstByIsEnabledTrueOrderByPriorityDesc();
    
    List<TranslationServiceConfig> findAllByOrderByPriorityDesc();
}














