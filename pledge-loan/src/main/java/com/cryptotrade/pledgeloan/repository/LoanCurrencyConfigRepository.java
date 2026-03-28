/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.repository;

import com.cryptotrade.pledgeloan.entity.LoanCurrencyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 借款币种配置Repository
 */
@Repository
public interface LoanCurrencyConfigRepository extends JpaRepository<LoanCurrencyConfig, Long> {
    
    Optional<LoanCurrencyConfig> findByCurrencyCode(String currencyCode);
    
    List<LoanCurrencyConfig> findByStatus(String status);
    
    List<LoanCurrencyConfig> findAllByOrderBySortOrderAsc();
}














