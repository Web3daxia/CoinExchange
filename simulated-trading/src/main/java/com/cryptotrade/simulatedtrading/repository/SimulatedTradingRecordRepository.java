/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.simulatedtrading.repository;

import com.cryptotrade.simulatedtrading.entity.SimulatedTradingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 模拟交易记录Repository
 */
@Repository
public interface SimulatedTradingRecordRepository extends JpaRepository<SimulatedTradingRecord, Long>, JpaSpecificationExecutor<SimulatedTradingRecord> {
    
    Optional<SimulatedTradingRecord> findByTradeNo(String tradeNo);
    
    List<SimulatedTradingRecord> findByAccountId(Long accountId);
    
    List<SimulatedTradingRecord> findByUserId(Long userId);
    
    List<SimulatedTradingRecord> findByTradeType(String tradeType);
    
    List<SimulatedTradingRecord> findByAccountIdAndTradeStatus(Long accountId, String tradeStatus);
    
    Page<SimulatedTradingRecord> findByUserId(Long userId, Pageable pageable);
    
    Page<SimulatedTradingRecord> findByAccountId(Long accountId, Pageable pageable);
    
    Long countByUserIdAndTradeStatus(Long userId, String tradeStatus);
}














