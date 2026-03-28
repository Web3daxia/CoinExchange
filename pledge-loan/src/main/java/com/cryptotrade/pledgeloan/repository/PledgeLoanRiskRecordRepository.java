/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.pledgeloan.repository;

import com.cryptotrade.pledgeloan.entity.PledgeLoanRiskRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 质押借币风险监控记录Repository
 */
@Repository
public interface PledgeLoanRiskRecordRepository extends JpaRepository<PledgeLoanRiskRecord, Long> {
    
    List<PledgeLoanRiskRecord> findByOrderId(Long orderId);
    
    List<PledgeLoanRiskRecord> findByUserId(Long userId);
    
    List<PledgeLoanRiskRecord> findByRiskLevel(String riskLevel);
    
    List<PledgeLoanRiskRecord> findByIsProcessedFalse();
    
    List<PledgeLoanRiskRecord> findByIsNotifiedFalse();
}














