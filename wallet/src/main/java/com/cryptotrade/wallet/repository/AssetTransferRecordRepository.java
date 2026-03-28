/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.repository;

import com.cryptotrade.wallet.entity.AssetTransferRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资产划转记录Repository
 */
@Repository
public interface AssetTransferRecordRepository extends JpaRepository<AssetTransferRecord, Long> {
    
    /**
     * 根据用户ID查询划转记录（分页）
     */
    Page<AssetTransferRecord> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和账户类型查询划转记录
     */
    List<AssetTransferRecord> findByUserIdAndFromAccountTypeOrToAccountType(Long userId, String fromAccountType, String toAccountType);
    
    /**
     * 根据用户ID、币种和时间范围查询
     */
    List<AssetTransferRecord> findByUserIdAndCurrencyAndCreatedAtBetween(
        Long userId, String currency, LocalDateTime startTime, LocalDateTime endTime);
}














