/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.repository;

import com.cryptotrade.wallet.entity.WithdrawAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawAddressRepository extends JpaRepository<WithdrawAddress, Long> {
    List<WithdrawAddress> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);

    List<WithdrawAddress> findByUserIdAndCurrencyAndChainAndStatus(Long userId, String currency, String chain, String status);
}















