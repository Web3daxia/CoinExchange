/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeveragedAccountRepository extends JpaRepository<LeveragedAccount, Long> {
    Optional<LeveragedAccount> findByUserId(Long userId);

    Optional<LeveragedAccount> findByUserIdAndPairName(Long userId, String pairName);

    List<LeveragedAccount> findByUserIdAndStatus(Long userId, String status);

    List<LeveragedAccount> findByStatus(String status);
}















