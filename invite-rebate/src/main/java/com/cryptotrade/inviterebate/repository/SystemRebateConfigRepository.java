/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.inviterebate.repository;

import com.cryptotrade.inviterebate.entity.SystemRebateConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 系统返佣配置Repository
 */
@Repository
public interface SystemRebateConfigRepository extends JpaRepository<SystemRebateConfig, Long> {
    /**
     * 根据用户等级查询配置
     */
    Optional<SystemRebateConfig> findByUserLevelAndStatus(String userLevel, String status);

    /**
     * 查询全局默认配置
     */
    @Query("SELECT src FROM SystemRebateConfig src WHERE src.userLevel IS NULL AND src.status = 'ACTIVE'")
    Optional<SystemRebateConfig> findDefaultConfig();

    /**
     * 查询所有启用的配置
     */
    List<SystemRebateConfig> findByStatus(String status);
}

