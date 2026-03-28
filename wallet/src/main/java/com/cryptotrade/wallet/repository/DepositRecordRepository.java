/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.repository;

import com.cryptotrade.wallet.entity.DepositRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRecordRepository extends JpaRepository<DepositRecord, Long> {
    Optional<DepositRecord> findByDepositNo(String depositNo);

    List<DepositRecord> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<DepositRecord> findByUserIdAndStatus(Long userId, String status);

    Optional<DepositRecord> findByTransactionHash(String transactionHash);
}















