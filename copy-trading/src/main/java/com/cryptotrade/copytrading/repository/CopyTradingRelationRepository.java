/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.repository;

import com.cryptotrade.copytrading.entity.CopyTradingRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CopyTradingRelationRepository extends JpaRepository<CopyTradingRelation, Long> {
    List<CopyTradingRelation> findByFollowerIdAndStatus(Long followerId, String status);

    List<CopyTradingRelation> findByTraderIdAndStatus(Long traderId, String status);

    Optional<CopyTradingRelation> findByTraderIdAndFollowerIdAndMarketTypeAndStatus(
            Long traderId, Long followerId, String marketType, String status);

    List<CopyTradingRelation> findByTraderId(Long traderId);

    List<CopyTradingRelation> findByFollowerId(Long followerId);

    List<CopyTradingRelation> findByCopyTypeAndStatus(String copyType, String status);
}















