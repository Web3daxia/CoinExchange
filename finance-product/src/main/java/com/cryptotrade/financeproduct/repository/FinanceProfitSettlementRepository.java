/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.repository;

import com.cryptotrade.financeproduct.entity.FinanceProfitSettlement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 理财产品收益结算记录Repository
 */
@Repository
public interface FinanceProfitSettlementRepository extends JpaRepository<FinanceProfitSettlement, Long>, JpaSpecificationExecutor<FinanceProfitSettlement> {
    
    List<FinanceProfitSettlement> findByInvestmentId(Long investmentId);
    
    List<FinanceProfitSettlement> findByUserId(Long userId);
    
    List<FinanceProfitSettlement> findByProductId(Long productId);
    
    List<FinanceProfitSettlement> findBySettlementStatus(String settlementStatus);
    
    Page<FinanceProfitSettlement> findByInvestmentId(Long investmentId, Pageable pageable);
    
    Page<FinanceProfitSettlement> findByUserId(Long userId, Pageable pageable);
}














