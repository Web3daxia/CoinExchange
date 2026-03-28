/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.repository;

import com.cryptotrade.financeproduct.entity.FinanceRedemption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 理财产品赎回记录Repository
 */
@Repository
public interface FinanceRedemptionRepository extends JpaRepository<FinanceRedemption, Long>, JpaSpecificationExecutor<FinanceRedemption> {
    
    Optional<FinanceRedemption> findByRedemptionOrderNo(String redemptionOrderNo);
    
    List<FinanceRedemption> findByInvestmentId(Long investmentId);
    
    List<FinanceRedemption> findByUserId(Long userId);
    
    List<FinanceRedemption> findByProductId(Long productId);
    
    List<FinanceRedemption> findByStatus(String status);
    
    Page<FinanceRedemption> findByUserId(Long userId, Pageable pageable);
    
    Page<FinanceRedemption> findByInvestmentId(Long investmentId, Pageable pageable);
}














