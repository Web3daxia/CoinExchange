/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.repository;

import com.cryptotrade.simulatedtrading.entity.SimulatedTradingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 模拟交易账户Repository
 */
@Repository
public interface SimulatedTradingAccountRepository extends JpaRepository<SimulatedTradingAccount, Long> {
    
    Optional<SimulatedTradingAccount> findByUserId(Long userId);
    
    Optional<SimulatedTradingAccount> findByAccountNo(String accountNo);
}














