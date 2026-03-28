/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.repository;

import com.cryptotrade.wallet.entity.WalletBalanceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalletBalanceSnapshotRepository extends JpaRepository<WalletBalanceSnapshot, Long> {
    List<WalletBalanceSnapshot> findByUserIdAndSnapshotTimeBetweenOrderBySnapshotTimeAsc(
            Long userId, LocalDateTime start, LocalDateTime end);

    List<WalletBalanceSnapshot> findByUserIdOrderBySnapshotTimeDesc(Long userId, org.springframework.data.domain.Pageable pageable);
}















