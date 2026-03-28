/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.repository;

import com.cryptotrade.pledgeloan.entity.PledgeCurrencyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质押币种配置Repository
 */
@Repository
public interface PledgeCurrencyConfigRepository extends JpaRepository<PledgeCurrencyConfig, Long> {
    
    Optional<PledgeCurrencyConfig> findByCurrencyCode(String currencyCode);
    
    List<PledgeCurrencyConfig> findByStatus(String status);
    
    List<PledgeCurrencyConfig> findAllByOrderBySortOrderAsc();
}














