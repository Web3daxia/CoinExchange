/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.repository;

import com.cryptotrade.pledgeloan.entity.PledgeLoanTopup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 质押借币补仓记录Repository
 */
@Repository
public interface PledgeLoanTopupRepository extends JpaRepository<PledgeLoanTopup, Long> {
    
    Optional<PledgeLoanTopup> findByTopupNo(String topupNo);
    
    List<PledgeLoanTopup> findByOrderId(Long orderId);
    
    List<PledgeLoanTopup> findByUserId(Long userId);
}














