/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.repository;

import com.cryptotrade.wallet.entity.WalletTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletTransferRepository extends JpaRepository<WalletTransfer, Long> {
    Optional<WalletTransfer> findByTransferNo(String transferNo);

    List<WalletTransfer> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<WalletTransfer> findByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}















