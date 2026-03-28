/*
 * 本系统开发者联系方式Tg：@arenjian，如有问题欢迎咨询。拒绝任何国内与大陆项目，请勿打扰
 */

package com.cryptotrade.copytrading.repository;

import com.cryptotrade.copytrading.entity.CopyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CopyOrderRepository extends JpaRepository<CopyOrder, Long> {
    List<CopyOrder> findByTraderIdOrderByCreatedAtDesc(Long traderId);

    List<CopyOrder> findByFollowerIdOrderByCreatedAtDesc(Long followerId);

    List<CopyOrder> findByRelationIdOrderByCreatedAtDesc(Long relationId);

    List<CopyOrder> findByTraderIdAndCreatedAtBetween(Long traderId, LocalDateTime start, LocalDateTime end);

    List<CopyOrder> findByFollowerIdAndCreatedAtBetween(Long followerId, LocalDateTime start, LocalDateTime end);

    List<CopyOrder> findByStatus(String status);
}















