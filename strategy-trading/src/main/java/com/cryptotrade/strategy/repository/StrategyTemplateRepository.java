/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.strategy.repository;

import com.cryptotrade.strategy.entity.StrategyTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 策略模板Repository
 */
@Repository
public interface StrategyTemplateRepository extends JpaRepository<StrategyTemplate, Long> {
    /**
     * 根据市场类型查询模板
     */
    List<StrategyTemplate> findByMarketType(String marketType);

    /**
     * 根据策略分类查询模板
     */
    List<StrategyTemplate> findByStrategyCategory(String strategyCategory);

    /**
     * 根据状态查询模板
     */
    List<StrategyTemplate> findByStatus(String status);

    /**
     * 根据市场类型和状态查询模板
     */
    List<StrategyTemplate> findByMarketTypeAndStatus(String marketType, String status);
}













