/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.futures.usdt.repository;

import com.cryptotrade.futures.usdt.entity.FuturesContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuturesContractRepository extends JpaRepository<FuturesContract, Long> {
    Optional<FuturesContract> findByPairName(String pairName);
    Optional<FuturesContract> findByPairNameAndContractType(String pairName, String contractType);
    List<FuturesContract> findByContractType(String contractType);
    List<FuturesContract> findByContractTypeAndStatus(String contractType, String status);
}


