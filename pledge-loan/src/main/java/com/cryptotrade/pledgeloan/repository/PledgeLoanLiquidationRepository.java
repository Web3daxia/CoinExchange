/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.repository;

import com.cryptotrade.pledgeloan.entity.PledgeLoanLiquidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质押借币平仓记录Repository
 */
@Repository
public interface PledgeLoanLiquidationRepository extends JpaRepository<PledgeLoanLiquidation, Long> {
    
    Optional<PledgeLoanLiquidation> findByLiquidationNo(String liquidationNo);
    
    List<PledgeLoanLiquidation> findByOrderId(Long orderId);
    
    List<PledgeLoanLiquidation> findByUserId(Long userId);
    
    List<PledgeLoanLiquidation> findByLiquidationType(String liquidationType);
}














