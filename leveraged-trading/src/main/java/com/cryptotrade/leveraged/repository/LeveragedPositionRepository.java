/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.leveraged.repository;

import com.cryptotrade.leveraged.entity.LeveragedPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeveragedPositionRepository extends JpaRepository<LeveragedPosition, Long> {
    List<LeveragedPosition> findByUserIdAndStatus(Long userId, String status);

    List<LeveragedPosition> findByUserId(Long userId);

    List<LeveragedPosition> findByAccountIdAndStatus(Long accountId, String status);

    Optional<LeveragedPosition> findByUserIdAndPairNameAndSideAndStatus(Long userId, String pairName, String side, String status);

    List<LeveragedPosition> findByStatus(String status);
}















