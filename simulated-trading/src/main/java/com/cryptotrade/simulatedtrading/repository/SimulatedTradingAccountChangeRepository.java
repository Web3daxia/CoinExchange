/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.repository;

import com.cryptotrade.simulatedtrading.entity.SimulatedTradingAccountChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 模拟交易账户变更记录Repository
 */
@Repository
public interface SimulatedTradingAccountChangeRepository extends JpaRepository<SimulatedTradingAccountChange, Long> {
    
    List<SimulatedTradingAccountChange> findByAccountId(Long accountId);
    
    List<SimulatedTradingAccountChange> findByUserId(Long userId);
    
    List<SimulatedTradingAccountChange> findByChangeType(String changeType);
}














