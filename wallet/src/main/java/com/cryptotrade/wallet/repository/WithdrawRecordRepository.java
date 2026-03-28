/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.repository;

import com.cryptotrade.wallet.entity.WithdrawRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawRecordRepository extends JpaRepository<WithdrawRecord, Long> {
    Optional<WithdrawRecord> findByWithdrawNo(String withdrawNo);

    List<WithdrawRecord> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<WithdrawRecord> findByUserIdAndStatus(Long userId, String status);

    List<WithdrawRecord> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}















