/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.spot.repository;

import com.cryptotrade.spot.entity.MarketAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketAlertRepository extends JpaRepository<MarketAlert, Long> {
    List<MarketAlert> findByUserIdAndIsEnabledTrue(Long userId);
    List<MarketAlert> findByPairNameAndIsEnabledTrue(String pairName);
}















