/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.wallet.repository;

import com.cryptotrade.wallet.entity.DepositAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositAddressRepository extends JpaRepository<DepositAddress, Long> {
    List<DepositAddress> findByStatus(String status);

    List<DepositAddress> findByCurrencyAndChainAndStatus(String currency, String chain, String status);

    Optional<DepositAddress> findByAddress(String address);

    List<DepositAddress> findByCurrencyAndStatus(String currency, String status);
}















