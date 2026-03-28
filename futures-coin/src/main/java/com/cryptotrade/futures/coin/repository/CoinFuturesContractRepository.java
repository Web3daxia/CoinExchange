/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.coin.repository;

import com.cryptotrade.futures.coin.entity.CoinFuturesContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoinFuturesContractRepository extends JpaRepository<CoinFuturesContract, Long> {
    Optional<CoinFuturesContract> findByPairName(String pairName);
    Optional<CoinFuturesContract> findByPairNameAndContractType(String pairName, String contractType);
    List<CoinFuturesContract> findByContractType(String contractType);
    List<CoinFuturesContract> findByContractTypeAndStatus(String contractType, String status);
}















