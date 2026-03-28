/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.experiencefund.repository;

import com.cryptotrade.experiencefund.entity.ContractExperienceTrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 合约体验金交易记录Repository
 */
@Repository
public interface ContractExperienceTradeRepository extends JpaRepository<ContractExperienceTrade, Long> {
    
    Optional<ContractExperienceTrade> findByTradeNo(String tradeNo);
    
    List<ContractExperienceTrade> findByAccountId(Long accountId);
    
    List<ContractExperienceTrade> findByUserId(Long userId);
    
    List<ContractExperienceTrade> findByAccountIdAndTradeStatus(Long accountId, String tradeStatus);
    
    Page<ContractExperienceTrade> findByUserId(Long userId, Pageable pageable);
    
    Page<ContractExperienceTrade> findByAccountId(Long accountId, Pageable pageable);
}














