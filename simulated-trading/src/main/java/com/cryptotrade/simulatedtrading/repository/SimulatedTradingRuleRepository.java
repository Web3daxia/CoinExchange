/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.repository;

import com.cryptotrade.simulatedtrading.entity.SimulatedTradingRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 模拟交易规则配置Repository
 */
@Repository
public interface SimulatedTradingRuleRepository extends JpaRepository<SimulatedTradingRule, Long> {
    
    Optional<SimulatedTradingRule> findByRuleKey(String ruleKey);
    
    List<SimulatedTradingRule> findByRuleType(String ruleType);
    
    List<SimulatedTradingRule> findByStatus(String status);
}














