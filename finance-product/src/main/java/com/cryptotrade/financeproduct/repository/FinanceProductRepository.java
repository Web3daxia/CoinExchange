/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.financeproduct.repository;

import com.cryptotrade.financeproduct.entity.FinanceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 理财产品Repository
 */
@Repository
public interface FinanceProductRepository extends JpaRepository<FinanceProduct, Long> {
    
    Optional<FinanceProduct> findByProductCode(String productCode);
    
    List<FinanceProduct> findByStatus(String status);
    
    List<FinanceProduct> findByProductType(String productType);
    
    List<FinanceProduct> findByRiskLevel(String riskLevel);
    
    List<FinanceProduct> findBySupportedCurrency(String supportedCurrency);
    
    List<FinanceProduct> findAllByOrderBySortOrderAsc();
    
    List<FinanceProduct> findByStatusAndProductType(String status, String productType);
}














