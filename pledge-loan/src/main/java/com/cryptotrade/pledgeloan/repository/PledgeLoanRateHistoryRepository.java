/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.repository;

import com.cryptotrade.pledgeloan.entity.PledgeLoanRateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 质押借币利率调整历史Repository
 */
@Repository
public interface PledgeLoanRateHistoryRepository extends JpaRepository<PledgeLoanRateHistory, Long> {
    
    List<PledgeLoanRateHistory> findByCurrencyCode(String currencyCode);
    
    List<PledgeLoanRateHistory> findByConfigType(String configType);
}














